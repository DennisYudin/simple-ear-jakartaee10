package com.packt.cookbook.libraries.producermethods;




import com.packt.cookbook.libraries.producermethods.entities.Chosen;
import com.packt.cookbook.libraries.producermethods.entities.Coder;
import com.packt.cookbook.libraries.producermethods.entities.CoderImpl;
import com.packt.cookbook.libraries.producermethods.entities.CoderType;
//import com.packt.cookbook.libraries.producermethods.entities.TestCoderImpl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.Annotated;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;

@ApplicationScoped
public class CoderBeanProducer {

//	@Produces
//	@Chosen
//	public Coder create(CoderBeanFactory factory, InjectionPoint injectionPoint) {
//		Annotated annotated = injectionPoint.getAnnotated();
//		Chosen chosenType = annotated.getAnnotation(Chosen.class);
//		CoderType type = chosenType.type();
//		return factory.getCoder(type);
//	}

	@Produces
	@Chosen
	public Coder create(CoderBeanFactory factory, InjectionPoint injectionPoint) {
//		return new CoderImpl();
		Annotated annotated = injectionPoint.getAnnotated();
		Chosen chosenType = annotated.getAnnotation(Chosen.class);
		CoderType type = chosenType.type();
		return factory.getCoder(type);
	}
}
