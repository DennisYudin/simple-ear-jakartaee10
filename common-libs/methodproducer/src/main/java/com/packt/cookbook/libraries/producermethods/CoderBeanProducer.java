package com.packt.cookbook.libraries.producermethods;


import com.packt.cookbook.libraries.test.Chosen;
import com.packt.cookbook.libraries.test.Coder;
import com.packt.cookbook.libraries.test.CoderType;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.Annotated;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;

@ApplicationScoped
public class CoderBeanProducer {

	@Produces
	@Chosen
	public Coder create(CoderBeanFactory factory, InjectionPoint injectionPoint) {
		Annotated annotated = injectionPoint.getAnnotated();
		Chosen chosenType = annotated.getAnnotation(Chosen.class);
		CoderType type = chosenType.type();
		return factory.getCoder(type);
	}
}
