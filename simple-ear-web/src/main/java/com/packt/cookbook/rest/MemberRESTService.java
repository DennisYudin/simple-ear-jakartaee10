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
package com.packt.cookbook.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.packt.cookbook.common.html.MessageConverter;
import com.packt.cookbook.common.logging.Log4jHelper;
import com.packt.cookbook.common.logging.LogHelper;
import com.packt.cookbook.common.xml.Contract;
import com.packt.cookbook.data.MemberRepository;

import com.packt.cookbook.model.Member;
import com.packt.cookbook.service.Registration;
//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.Logger;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("members")
@RequestScoped
public class MemberRESTService {
//	@Inject
//	private Logger log;

//	@Inject
//	private LogHelper log;

	private static final LogHelper log = Log4jHelper.getLogger(MemberRESTService.class);


	@Inject
	private Validator validator;
	@Inject
	private MemberRepository repository;
	@Inject
	private Registration registration;

	@PostConstruct
	public void init() {
		log.info("WEB module: logging is working...");
	}

	@GET
	@Path("/formParams")
	public Response getFormParams() {
		String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
				"<html>\n" +
				"<head>\n" +
				"    <META name=\"viewport\" content=\"width=device-width\"/>\n" +
				"</head>\n" +
				"\n" +
				"<body onLoad=\"javascript:post()\">\n" +
				"\n" +
				"<form name=\"postform\" method=\"POST\" action=\"https://test.ru:444/sc1/authreq\">\n" +
				"\n" +
				"    <input type=\"hidden\" name=\"lastEventGmtTime\" value=\"2020-10-14 09:54:54.958\"/>\n" +
				"\n" +
				"    <!-- To support javascript unaware/disabled browsers -->\n" +
				"    <noscript>\n" +
				"        <center>Please click Submit to continue.<br>\n" +
				"            <input type=\"submit\" name=\"submit\" value=\"Submit\"/></center>\n" +
				"    </noscript>\n" +
				"</form>\n" +
				"</body>\n" +
				"</html>";
		String result = MessageConverter.getFormParams(html);
		return Response.ok(result).build();
	}

	@GET
	@Path("/contract")
	public Response xStreamLibTest() {
		Contract contract;
		try {
			contract = Contract.fromString("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
					"<contract>\n" +
					"\t<param id=\"code1\" label=\"Номер билета\">10</param>\n" +
					"\t<param id=\"code2\" label=\"Дата\">10</param>\n" +
					"\t<param id=\"code3\" label=\"Дата\">10</param>\n" +
					"</contract>");
		} catch (Exception e) {
			log.error("Error during creating Contract object");
			throw new RuntimeException("Error during creating Contract object", e);
		}
		return Response.ok("Contract object is created code1: " + contract.getValue("code1")).build();
	}

	@GET
	@Path("/ping")
	public Response ping() {
		return Response.ok("Pong").build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Member> listAllMembers() {
		return repository.findAllOrderedByName();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Member lookupMemberById(@PathParam("id") long id) {
		Member member = repository.findById(id);
		if (member == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return member;
	}

	/**
	 * Creates a new member from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
	 * or with a map of fields, and related errors.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createMember(Member member) {

		Response.ResponseBuilder builder;
		try {
			validateMember(member);

			registration.register(member);

			// Create an "ok" response
			builder = Response.ok();
		} catch (ConstraintViolationException ce) {
			// Handle bean validation issues
			builder = createViolationResponse(ce.getConstraintViolations());
		} catch (ValidationException e) {
			// Handle the unique constrain violation
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("email", "Email taken");
			builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
		} catch (Exception e) {
			// Handle generic exceptions
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}
		return builder.build();
	}

	/**
	 * <p>
	 * Validates the given Member variable and throws validation exceptions based on the type of error. If the error is standard
	 * bean validation errors then it will throw a ConstraintValidationException with the set of the constraints violated.
	 * </p>
	 * <p>
	 * If the error is caused because an existing member with the same email is registered it throws a regular validation
	 * exception so that it can be interpreted separately.
	 * </p>
	 *
	 * @param member Member to be validated
	 * @throws ConstraintViolationException If Bean Validation errors exist
	 * @throws ValidationException          If member with the same email already exists
	 */
	private void validateMember(Member member) throws ConstraintViolationException, ValidationException {
		// Create a bean validator and check for issues.
		Set<ConstraintViolation<Member>> violations = validator.validate(member);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}

		// Check the uniqueness of the email address
		if (emailAlreadyExists(member.getEmail())) {
			throw new ValidationException("Unique Email Violation");
		}
	}

	/**
	 * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can then be used
	 * by clients to show violations.
	 *
	 * @param violations A set of violations that needs to be reported
	 * @return JAX-RS response containing all violations
	 */
	private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
		log.info("Validation completed. violations found: " + violations.size());

		Map<String, String> responseObj = new HashMap<>();

		for (ConstraintViolation<?> violation : violations) {
			responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
		}

		return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
	}

	/**
	 * Checks if a member with the same email address is already registered. This is the only way to easily capture the
	 * "@UniqueConstraint(columnNames = "email")" constraint from the Member class.
	 *
	 * @param email The email to check
	 * @return True if the email already exists, and false otherwise
	 */
	public boolean emailAlreadyExists(String email) {
		Member member = null;
		try {
			member = repository.findByEmail(email);
		} catch (NoResultException e) {
			// ignore
		}
		return member != null;
	}
}
