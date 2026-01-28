package com.packt.cookbook.test;

//import static org.junit.Assert.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.packt.cookbook.model.Member;
import com.packt.cookbook.service.MemberRegistration;
import com.packt.cookbook.service.Registration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.logging.Logger;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

class MockitoTest {

	@InjectMocks
	MemberRegistration memberRegistration;

	@Mock
	private Logger log;
	@Mock
	private EntityManager em;
	@Mock
	private Event<Member> memberEventSrc;

	private AutoCloseable closeable;
	@BeforeEach
	public void openMocks() {
		closeable = MockitoAnnotations.openMocks(this);
	}
	@AfterEach
	public void releaseMocks() throws Exception {
		closeable.close();
	}

//	@BeforeEach
//	public void init() {
//		MockitoAnnotations.initMocks(this);
//	}

	@Disabled
	@Test
	void name() throws Exception {
		Member newMember = new Member();
		newMember.setName("Jane Doe");
		newMember.setEmail("jane@mailinator.com");
		newMember.setPhoneNumber("2125551234");

		memberRegistration.register(newMember);

		assertNotNull(newMember.getId());
		log.info(newMember.getName() + " was persisted with id " + newMember.getId());
	}
}
