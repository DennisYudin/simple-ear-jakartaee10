package com.packt.cookbook.libraries.log.injection;


import com.packt.cookbook.libraries.common.logging.LogHelper;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

@Startup
@Singleton
public class TestInject {

	public static final String LOGGING_MESSAGE = "<= TEST %s LOGGING LEVEL";

	@Inject
	private LogHelper log;

	@PostConstruct
	public void init() {
		log.debug(LOGGING_MESSAGE, "'DEBUG'");
		log.info(LOGGING_MESSAGE, "'INFO'");
		log.warn(LOGGING_MESSAGE, "'WARN'");
		log.error(LOGGING_MESSAGE, "'ERROR'");
		log.fatal(LOGGING_MESSAGE, "'FATAL'");
	}
}
