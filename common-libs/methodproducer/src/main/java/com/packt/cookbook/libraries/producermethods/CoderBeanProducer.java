package com.packt.cookbook.libraries.producermethods;

import com.packt.cookbook.libraries.coder.Chosen;
import com.packt.cookbook.libraries.coder.Coder;
import com.packt.cookbook.libraries.coder.CoderType;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.Annotated;
import jakarta.enterprise.inject.spi.InjectionPoint;

@ApplicationScoped
public class CoderBeanProducer {

	@Produces
//	@Chosen
	public Coder create(CoderBeanFactory factory, InjectionPoint injectionPoint) {
		Annotated annotated = injectionPoint.getAnnotated();
		Chosen chosenType = annotated.getAnnotation(Chosen.class);
		CoderType type = chosenType.type();
		return factory.getCoder(type);
	}
}
