/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.packt.cookbook.service;


import com.packt.cookbook.common.logging.Log4jHelper;
import com.packt.cookbook.common.logging.LogHelper;
import com.packt.cookbook.model.Member;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
//import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class MemberRegistration implements Registration {

	private static final LogHelper log = Log4jHelper.getLogger(MemberRegistration.class);

	@Inject
	private EntityManager em;

	@Inject
	private Event<Member> memberEventSrc;

	@PostConstruct
	public void init() {
		log.info("LogHelper log message from EJB");
	}

	@Override
	public void register(Member member) throws Exception {
		log.info("Registering " + member.getName());
		em.persist(member);
		memberEventSrc.fire(member);
	}
}
