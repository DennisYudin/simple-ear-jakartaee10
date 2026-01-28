package com.packt.cookbook.common.logging;


import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

/**
 * Log4jHelper producer
 */
public class LogHelperProducer {

	@Produces
	public LogHelper createLogHelper(InjectionPoint injectionPoint) {
		return Log4jHelper.getLogger(injectionPoint.getMember().getDeclaringClass());
	}
}