package com.packt.cookbook;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.packt.cookbook.common.xml.Contract;
import org.junit.jupiter.api.Test;

/**
 * Contract serialization/deserialization test
 */
class ContractTest {

	private static final String CONTRACT = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
			"<contract>\n" +
			"\t<param id=\"code1\" label=\"Номер билета\">10</param>\n" +
			"\t<param id=\"code2\" label=\"Дата\">10</param>\n" +
			"\t<param id=\"code3\" label=\"Дата\">10</param>\n" +
			"</contract>";

	@Test
	void test() throws Exception {
		Contract contract = Contract.fromString(CONTRACT);
		assertNotNull(contract);
		assertEquals("10", contract.getValue("code2"));
	}
}
