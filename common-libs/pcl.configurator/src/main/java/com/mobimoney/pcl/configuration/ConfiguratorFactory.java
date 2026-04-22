package com.mobimoney.pcl.configuration;


import com.packt.cookbook.libraries.common.ApplicationType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Configurator factory
 */
@ApplicationScoped
public class ConfiguratorFactory {

	private static boolean isTest = false;

	private final Map<ApplicationType, Configurator> mapper = new ConcurrentHashMap<>();

	/**
	 * @param type application type
	 * @return configurator instance
	 */
	//@SuppressWarnings({ "rawtypes" })
	public Configurator getConfigurator(ApplicationType type) {
		if (isTest) {
			return new FakeConfigurator();
		}
		Configurator configurator = mapper.get(type);
		if (configurator == null) {
			synchronized (this) {
				configurator = mapper.get(type);
				if (configurator == null) {
					if (type != ApplicationType.COMMON) {
						configurator = new ConfiguratorImpl(ConfigurationHelper
								.getConfigurations(type.getPath(), ApplicationType.COMMON.getPath()));
					} else {
						configurator = new ConfiguratorImpl(ConfigurationHelper
								.getConfigurations(ApplicationType.COMMON.getPath()));
					}
					mapper.put(type, configurator);
				}
			}
		}
		return configurator;
	}

	/**
	 * Reinitialize configurator of type specified
	 *
	 * @param type - application type
	 */
	public void reInitConfigurator(ApplicationType type) {
		Configurator configurator = mapper.get(type);
		if (configurator != null) {
			if (type != ApplicationType.COMMON) {
				configurator.init(
						ConfigurationHelper.getConfigurations(type.getPath(), ApplicationType.COMMON.getPath()));
			} else {
				configurator.init(
						ConfigurationHelper.getConfigurations(ApplicationType.COMMON.getPath()));

			}
		}
	}

	/**
	 * refresh configurator instances
	 */
	public void clear() {
		mapper.clear();
	}

	/**
	 * refresh configurator of type specified
	 *
	 * @param appType - application type
	 */
	public void clear(ApplicationType appType) {
		mapper.remove(appType);
	}

	/**
	 * @param isTest the isTest to set
	 */
	public static void setTest(boolean isTest) {
		ConfiguratorFactory.isTest = isTest;
	}

}