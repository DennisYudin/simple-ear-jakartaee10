package com.packt.cookbook.libraries.common.logging;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;


@ApplicationScoped
public class LogHelperProducer {

	@Produces
	public LogHelper createLogHelper(InjectionPoint injectionPoint) {
		return Log4jHelper.getLogger(injectionPoint.getMember().getDeclaringClass());
	}
}