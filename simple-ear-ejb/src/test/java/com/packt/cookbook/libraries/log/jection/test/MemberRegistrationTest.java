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
package com.packt.cookbook.libraries.log.jection.test;

//import static org.junit.Assert.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.logging.Logger;

import jakarta.inject.Inject;

import com.packt.cookbook.ejb.data.MemberListProducer;
import com.packt.cookbook.ejb.data.MemberRepository;
import com.packt.cookbook.ejb.util.Resource;
import org.jboss.arquillian.container.test.api.Deployment;
import com.packt.cookbook.ejb.model.Member;
import com.packt.cookbook.ejb.service.MemberRegistration;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Disabled
@ExtendWith(ArquillianExtension.class)
public class MemberRegistrationTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class, "simple-ear-ejb-1.0-SNAPSHOT.jar")
                .addClasses(
                        Member.class,
                        MemberRegistration.class, Resource.class, MemberRepository.class, MemberListProducer.class)
                .addAsResource("META-INF/beans.xml", "META-INF/persistence.xml");
                // Deploy our test datasource
//                .
//                .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }

    @Inject
    MemberRegistration memberRegistration;
    @Inject
    Logger log;

    @Test
    void testRegister() throws Exception {
        Member newMember = new Member();
        newMember.setName("Jane Doe");
        newMember.setEmail("jane@mailinator.com");
        newMember.setPhoneNumber("2125551234");

        memberRegistration.register(newMember);

        assertNotNull(newMember.getId());
        log.info(newMember.getName() + " was persisted with id " + newMember.getId());
    }
}
