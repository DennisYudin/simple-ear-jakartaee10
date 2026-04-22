package com.mobimoney.pcl.configuration;

import com.packt.cookbook.libraries.common.configuration.AbstractConfiguration;

import java.io.InputStream;



/**
 * Provides configuration for system components specified
 */
public interface Configurator {

	/**
	 * Fill in configuration class with information from configuration file and returns it.
	 * Configuration for defined class searches in xml tag with same name.
	 * If configuration cannot be read null will be returned.
	 * 
	 * @param <T> - configuration class
	 * @param clazz - configuration class object
	 * @return - configuration value object
	 */
	<T extends AbstractConfiguration> T getConfiguration(Class<T> clazz);

	/**
	 * Fill in configuration class with information from configuration file and returns it.
	 * Configuration for defined class searches in xml tag with same name.
	 * If configuration cannot be read default configuration object will be returned.
	 * 
	 * @param <T> - configuration class
	 * @param clazz - configuration class object
	 * @param defaultConf - default configuration object
	 * @return - configuration value object
	 */
	<T extends AbstractConfiguration> T getConfiguration(Class<T> clazz, T defaultConf);

	/**
	 * Reads a part of configuration with defined class name and returns it as input stream
	 * You are responsible for closing input stream got
	 * 
	 * @param <T> - configuration class
	 * @param configurationTagName - the name of the XML element storing configuration information
	 * @return - input stream with configuration XML
	 */
	<T extends AbstractConfiguration> InputStream getConfigurationAsStream(String configurationTagName);

	/**
	 * Initialize instance with sources specified
	 * 
	 * @param is - array of {@link InputStream} with configuration
	 */
	void init(ConfigurationFileWrapper... is);
}
