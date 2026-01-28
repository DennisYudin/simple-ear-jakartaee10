package com.packt.cookbook;


import com.packt.cookbook.common.logging.LogHelper;

import java.util.concurrent.TimeUnit;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

@Startup
@Singleton
public class TestInject {

	@Inject
	private LogHelper log;

	@PostConstruct
	public void init() {
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.debug("");
		log.debug("");
		log.debug("Inject success");
		log.debug("");
		log.debug("");
	}
}
