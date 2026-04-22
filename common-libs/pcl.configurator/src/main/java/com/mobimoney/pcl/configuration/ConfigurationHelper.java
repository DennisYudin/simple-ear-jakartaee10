package com.mobimoney.pcl.configuration;


import com.mobimoney.securityservice.client.SecurityServiceClient;
import com.mobimoney.securityservice.client.SecurityServiceClientImpl;
import com.mobimoney.securityservice.client.SecurityServiceConsts;
import com.mobimoney.securityservice.protocol.exceptions.SecurityServiceException;

import com.packt.cookbook.libraries.common.logging.Log4jHelper;
import com.packt.cookbook.libraries.common.logging.LogHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Helper class for reading configuration files
 */
public class ConfigurationHelper {

	private static final LogHelper log = Log4jHelper.getLogger(ConfigurationHelper.class);

	private static final String CORE_DIR = "core";
	private static final String SECRET_DIR = "secret";

	/**
	 * Hide constructor for utility class
	 */
	private ConfigurationHelper() {
	}

	// decrypts secret file and return as InputStream
	private static ConfigurationFileWrapper decryptFile(File file, SecurityServiceClient sClient) throws Exception {
		try (FileInputStream fin = new FileInputStream(file);
			 ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
			byte[] buf = new byte[256];
			int len;
			while ((len = fin.read(buf)) > -1) {
				bout.write(buf, 0, len);
			}

			byte[] decrypted = sClient.decrypt(bout.toByteArray());
			return new ConfigurationFileWrapper(new ByteArrayInputStream(decrypted), file.getAbsolutePath());
		}
	}

	// returns directory with configuration files
	private static String getConfigurationPath() {
		StringBuilder sb = new StringBuilder(getJbossConfigurationPath());
		sb.append(File.separator).append("app_config").append(File.separator);
		return sb.toString();
	}

	// after switching to WildFly, part for JBoss can be removed
	private static String getJbossConfigurationPath() {
		//trying to get JBoss config url like "file:/srv/jboss-pc/server/default/conf/"
		String jbossCfg = System.getProperty("jboss.server.config.url");
		//if found, removing file: and slash at end
		if (jbossCfg != null) {
			int start = jbossCfg.indexOf(':');
			return jbossCfg.substring(start + 1, jbossCfg.length() - 1);
		}
		//returning WildFly config dir
		return System.getProperty("jboss.server.config.dir");
	}

	/**
	 * Returns array of {@link InputStream} with configurations
	 *
	 * @return array of {@link InputStream}
	 */
	public static ConfigurationFileWrapper[] getConfigurations() {
		SecurityServiceClient sClient = null;
		try {
			sClient = new SecurityServiceClientImpl(SecurityServiceConsts.SERVICE_PORT, "PC");
			return process(sClient, null, (String[]) null);
		} finally {
			if (sClient != null) {
				sClient.disconnect();
			}
		}
	}

	/**
	 * Returns array of {@link InputStream} with configurations
	 *
	 * @param subdirs - configuration sub directories
	 * @return array of {@link InputStream}
	 */
	public static ConfigurationFileWrapper[] getConfigurations(String... subdirs) {
		SecurityServiceClient sClient = null;
		try {
			sClient = new SecurityServiceClientImpl(SecurityServiceConsts.SERVICE_PORT, "PC");
			return process(sClient, null, subdirs);
		} finally {
			if (sClient != null) {
				sClient.disconnect();
			}
		}
	}

	/**
	 * Returns array of {@link InputStream} with configurations<br>
	 * <b>Note: security service client not disconnects at end of method execution</b>
	 *
	 * @param sClient - {@link SecurityServiceClient}
	 * @return array of {@link InputStream}
	 */
	public static ConfigurationFileWrapper[] getConfigurations(SecurityServiceClient sClient) {
		return process(sClient, null, (String[]) null);
	}

	/**
	 * Returns array of {@link InputStream} with configurations<br>
	 * <b>Note: security service client not disconnects at end of method execution</b>
	 *
	 * @param sClient           - {@link SecurityServiceClient}
	 * @param configurationPath base path for configuration files
	 * @return array of {@link InputStream}
	 */
	public static ConfigurationFileWrapper[] getConfigurations(SecurityServiceClient sClient, String configurationPath) {
		return process(sClient, configurationPath, (String[]) null);
	}

	/**
	 * Returns array of {@link InputStream} with configurations<br>
	 * <b>Note: security service client not disconnects at end of method execution</b>
	 *
	 * @param sClient           - {@link SecurityServiceClient}
	 * @param configurationPath base path for configuration files
	 * @param subdirs           - configuration sub directories
	 * @return array of {@link InputStream}
	 */
	public static ConfigurationFileWrapper[] getConfigurations(SecurityServiceClient sClient, String configurationPath,
															   String... subdirs) {
		return process(sClient, configurationPath, subdirs);
	}

	// returns array of Files for directory
	private static List<File> getFiles(File dir) {
		if (!dir.exists() || dir.isFile()) {
			log.error("Configuration directory '%s' not found, skipped!", dir);
			return Collections.emptyList();
		}
		File[] files = dir.listFiles(file -> {
			String name = file.getName();
			return file.isFile() && (name.endsWith("-config.xml") || name.endsWith("-config-secret.xml"));
		});
		List<File> ret = new ArrayList<>();
		if (files != null) {
			Collections.addAll(ret, files);
		}
		return ret;
	}

	private static ConfigurationFileWrapper[] process(SecurityServiceClient sClient, String configurationPath, String... subdirs) {
		String path = configurationPath == null ? getConfigurationPath() : configurationPath;

		Set<String> directories = new HashSet<>();
		if (subdirs == null) {
			directories.add(path);
		} else {
			for (String subdir : subdirs) {
				directories.add(path + File.separator + subdir);
			}
		}

		List<File> coreFiles = new ArrayList<>();
		List<File> secretFiles = new ArrayList<>();

		for (String dir : directories) {
			coreFiles.addAll(getFiles(new File(dir + File.separator + CORE_DIR)));
			secretFiles.addAll(getFiles(new File(dir + File.separator + SECRET_DIR)));
		}

		List<ConfigurationFileWrapper> streams = new ArrayList<>();
		for (File file : coreFiles) {
			try {
				log.info("Reading configuration from file: '%s'", file.getAbsolutePath());
				ConfigurationFileWrapper plainWrapper = new ConfigurationFileWrapper(new FileInputStream(file), file.getAbsolutePath());
				streams.add(plainWrapper);
			} catch (FileNotFoundException e) {
				log.error("Error getting stream for file: " + file.getAbsolutePath(), e);
			}
		}

		for (File file : secretFiles) {
			try {
				log.info("Reading configuration from encrypted file: '%s'", file.getAbsolutePath());
				streams.add(decryptFile(file, sClient));
			} catch (SecurityServiceException se) {
				log.error(
						"Error file decryption, maybe service not started, file not encrypted, corrupted or encrypted by another key, file: "
								+ file, se);
			} catch (Exception e) {
				log.error("Error wile decrypting file: " + file, e);
			}
		}

		ConfigurationFileWrapper[] array = new ConfigurationFileWrapper[streams.size()];
		return streams.toArray(array);
	}
}
