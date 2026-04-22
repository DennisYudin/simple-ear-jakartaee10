package com.packt.cookbook.libraries.log.injection;


import com.mobimoney.pcl.configuration.Configurator;
import com.mobimoney.pcl.configuration.ConfiguratorType;

import com.packt.cookbook.libraries.common.ApplicationType;
import com.packt.cookbook.libraries.common.DatabaseHelper;
import com.packt.cookbook.libraries.common.configuration.DbMasterSlaveConfiguration;
import com.packt.cookbook.libraries.common.logging.LogHelper;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.DependsOn;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

@Startup
@Singleton
public class TestInject {

	@Inject
	private LogHelper log;
	@Inject
	@ConfiguratorType(type = ApplicationType.COMMON)
	private Configurator configurator;
	@Resource(lookup="java:/jdbc/postgre")
	private DataSource mainDataSource;

	@PostConstruct
	public void init() throws SQLException {
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.debug("");
		log.debug("Successful log injection");
		log.debug("");

		DbMasterSlaveConfiguration cfg = configurator.getConfiguration(DbMasterSlaveConfiguration.class,
				new DbMasterSlaveConfiguration());
//		DataSource mainDataSource;
		try {
			log.debug("Getting data source %s", cfg.getMainDbJndiName());
//			mainDataSource = DatabaseHelper.createDataSource(cfg.getMainDbJndiName());
		} catch (Exception e) {
			log.error("Couldn't get main datasource for: " + cfg.getMainDbJndiName());
			throw new RuntimeException("Couldn't get main datasource for: " + cfg.getMainDbJndiName());
		}
		log.debug("Got mainDataSource without any errors..." + mainDataSource);
	}
}
