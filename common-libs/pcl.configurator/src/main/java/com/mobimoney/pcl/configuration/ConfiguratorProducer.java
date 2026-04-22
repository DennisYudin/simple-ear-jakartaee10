package com.mobimoney.pcl.configuration;


import com.packt.cookbook.libraries.common.ApplicationType;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.Annotated;
import jakarta.enterprise.inject.spi.InjectionPoint;

/**
 * Configurator producer
 */
@ApplicationScoped
public class ConfiguratorProducer {

	@Produces
	@ConfiguratorType
	public Configurator createArticleConfigurator(ConfiguratorFactory factory, InjectionPoint injectionPoint) {
		Annotated annotated = injectionPoint.getAnnotated();
		ConfiguratorType configuratorType = annotated.getAnnotation(ConfiguratorType.class);
		ApplicationType appType = configuratorType.type();
		return factory.getConfigurator(appType);
	}

}
