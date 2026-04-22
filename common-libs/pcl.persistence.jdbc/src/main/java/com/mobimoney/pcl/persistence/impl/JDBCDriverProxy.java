package com.mobimoney.pcl.persistence.impl;


import com.mobimoney.pcl.configuration.ConfigurationHelper;
import com.mobimoney.pcl.configuration.Configurator;
import com.mobimoney.pcl.configuration.ConfiguratorImpl;
import com.mobimoney.pcl.persistence.impl.JDBCDriverProxyConfiguration.MappingConfig;

import com.packt.cookbook.libraries.common.ApplicationType;
import com.packt.cookbook.libraries.common.Pair;
import com.packt.cookbook.libraries.common.logging.Log4jHelper;
import com.packt.cookbook.libraries.common.logging.LogHelper;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Singleton;

/**
 * Proxy for real jdbc driver, needs for hide actual connection url's and database user name and password.
 */
@ApplicationScoped
public class JDBCDriverProxy implements Driver {

	private static final LogHelper log = Log4jHelper.getLogger(JDBCDriverProxy.class);
	private static final String ACCEPT_URLS = "jdbc:mobimoney";
	private static final Map<String, Pair<String, Pair<String, Properties>>> map = new ConcurrentHashMap<>();
	private static final Map<String, Driver> delegates = new ConcurrentHashMap<>();

	@PostConstruct
	public void post() {
		log.debug("Initializing JDBCDriverProxy");
		init();
		try {
			DriverManager.registerDriver(new JDBCDriverProxy());
			log.debug("JDBCDriverProxy registered as JDBC driver");
		} catch (SQLException e) {
			String msg = "Can't register JDBCDriverProxy as JDBC driver";
			log.error(e, msg);
		}
	}
//	static {
//		log.debug("Initializing JDBCDriverProxy");
//		init();
//		try {
//			DriverManager.registerDriver(new JDBCDriverProxy());
//			log.debug("JDBCDriverProxy registered as JDBC driver");
//		} catch (SQLException e) {
//			String msg = "Can't register JDBCDriverProxy as JDBC driver";
//			log.error(e, msg);
//		}
//	}

	/**
	 * Default constructor
	 */
	public JDBCDriverProxy() {
	}

	/**
	 * JDBC Proxy initialization
	 */
	private void init() {
		Configurator conf =
				new ConfiguratorImpl(ConfigurationHelper.getConfigurations(ApplicationType.COMMON.getPath()));
		JDBCDriverProxyConfiguration config = conf.getConfiguration(JDBCDriverProxyConfiguration.class,
				new JDBCDriverProxyConfiguration());
		for (MappingConfig mapping : config.getMappings()) {
			String driverClass = mapping.getDriverClass(); //org.postgresql.Driver

			if (registerDriver(driverClass)) {
				Properties p = new Properties();
				p.put("user", mapping.getLogin());
				p.put("password", mapping.getPassword());
				if (mapping.getAppRole() != null) {
					p.put("app_role", mapping.getAppRole());
				}
				if (mapping.getAppPassword() != null) {
					p.put("app_passwd", mapping.getAppPassword());
				}
//				{jdbc:mobimoney:postgre=realUrl}={jdbc:postgresql://192.168.77.54:5432/oltp?currentSchema=pc = {org.postgresql.Driver=password and login}
				map.put(mapping.getFakeUrl(), new Pair<>(mapping.getRealUrl(),
						new Pair<>(driverClass, p)));
			}
		}
	}

	private static boolean registerDriver(String driverClass) {
		if (delegates.get(driverClass) == null) {
			log.debug("Trying to register driver: %s", driverClass);

			Driver driver;
			Class<?> clazz;

			try {
				clazz = Class.forName(driverClass);
			} catch (ClassNotFoundException e) {
				log.error("Class %s not found, skipping driver", driverClass);
				return false;
			}

			try {
				driver = (Driver) clazz.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				log.error(e, "Can't instantiate driver: %s, skipping driver", driverClass);
				return false;
			}
			try {
				DriverManager.registerDriver(driver);
			} catch (SQLException e) {
				log.error(e, "Can't register driver: %s", driverClass);
				return false;
			}
			delegates.put(driverClass, driver);
			log.debug("Driver registered successfully: %s", driverClass);
			return true;
		}
		return true;
	}

	@Override
	public boolean acceptsURL(String url) throws SQLException {
		return url.startsWith(ACCEPT_URLS);
	}


	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		Pair<String, Pair<String, Properties>> pair = map.get(url);
		if (pair != null) {
			String realUrl = pair.getFirst();
			String className = pair.getSecond().getFirst();
			Properties props = pair.getSecond().getSecond();

			Connection conn = delegates.get(className).connect(realUrl, props);

			//execute procedure for app_role
			final String appRole = (String) props.get("app_role");
			final String appPasswd = (String) props.get("app_passwd");
			if (conn != null && appRole != null && appPasswd != null) {
				try (Statement st = conn.createStatement()) {
					//CallableStatement и PreparedStatement не работают с JDBC драйвером от Microsoft
					st.execute(String.format("EXEC sp_setapprole '%s', '%s'", appRole, appPasswd));
				} catch (Exception e) {
					log.error(e, "Error applying APP_ROLE for connection");
				}
			}

			return conn;
		}
		throw new SQLException("Mapping for url: '" + url + "' not registered!");
	}

	@Override
	public int getMajorVersion() {
		return 1;
	}

	@Override
	public int getMinorVersion() {
		return 0;
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		Pair<String, Pair<String, Properties>> pair = map.get(url);
		if (pair != null) {
			String realUrl = pair.getFirst();
			String className = pair.getSecond().getFirst();
			return delegates.get(className).getPropertyInfo(realUrl, info);
		}
		throw new SQLException("Mapping for url: '" + url + "' not registered!");
	}

	@Override
	public boolean jdbcCompliant() {
		return true;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

}