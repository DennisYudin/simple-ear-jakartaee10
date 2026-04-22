package com.mobimoney.pcl.configuration;

import com.packt.cookbook.libraries.common.configuration.AbstractConfiguration;

import java.io.InputStream;

/**
 * Fake Configurator implementation
 * 
 * @param <S> - base type of configuration that can be read by Configurator
 * @see Configurator
 * @see AbstractConfiguration
 */
public class FakeConfigurator<S extends AbstractConfiguration> implements Configurator {

	private S conf;

	/**
	 * Empty constructor
	 */
	public FakeConfigurator() {
	}

	/**
	 * Construct instance with specified configuration
	 * 
	 * @param conf - configuration instance
	 */
	public FakeConfigurator(S conf) {
		this.conf = conf;
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractConfiguration> T getConfiguration(Class<T> clazz, T defaultConf) {
		return (T) (conf != null ? conf : defaultConf);
	}

	/**
	 * @see com.mobimoney.pcl.configuration.Configurator#getConfiguration(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractConfiguration> T getConfiguration(Class<T> clazz) {
		return (T) conf;
	}

	/**
	 * @see com.mobimoney.pcl.configuration.Configurator#getConfigurationAsStream(java.lang.String)
	 */
	@Override
	public <T extends AbstractConfiguration> InputStream getConfigurationAsStream(String configurationTagName) {
		return null;
	}


	@Override
	public void init(ConfigurationFileWrapper... is) {

	}

}
