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
package com.packt.cookbook.controller;

import jakarta.enterprise.inject.Model;
import jakarta.enterprise.inject.Produces;


import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import com.packt.cookbook.common.logging.Log4jHelper;
import com.packt.cookbook.common.logging.LogHelper;
import com.packt.cookbook.model.Member;
import com.packt.cookbook.service.Registration;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import jakarta.annotation.PostConstruct;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://www.cdi-spec.org/faq/#accordion6
@Model
public class MemberController {

//    protected final Logger log = LogManager.getLogger(getClass());
    private static final LogHelper log = Log4jHelper.getLogger(MemberController.class);

    @Inject
    private FacesContext facesContext;
    @Inject
    private Registration memberRegistration;

    private Member newMember;

    @Produces
    @Named
    public Member getNewMember() {
        return newMember;
    }

    public void register() throws Exception {
        try {
            memberRegistration.register(newMember);

            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful"));
            initNewMember();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration Unsuccessful");
            facesContext.addMessage(null, m);
        }
    }

    @PostConstruct
    public void initNewMember() {
        log.info("-------------------");
        log.info("START NEW REQUEST");
        log.trace("TRACE: Logger log = Logger.getLogger(getClass()) from WEB");
        log.debug("DEBUG: Logger log = Logger.getLogger(getClass()) from WEB");
        log.info("INFO: Logger log = Logger.getLogger(getClass()) from WEB");
        log.warn("WARN: Logger log = Logger.getLogger(getClass()) from WEB");
        log.error("ERROR: Logger log = Logger.getLogger(getClass()) from WEB");
        log.fatal("FATAL: Logger log = Logger.getLogger(getClass()) from WEB");

        newMember = new Member();
    }

    private String getRootErrorMessage(Exception e) {
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            return errorMessage;
        }
        Throwable t = e;
        while (t != null) {
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        return errorMessage;
    }
}
