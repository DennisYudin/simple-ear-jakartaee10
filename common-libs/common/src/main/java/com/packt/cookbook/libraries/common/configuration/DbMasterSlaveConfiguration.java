package com.packt.cookbook.libraries.common.configuration;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Configuration for master and slave data source
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DbMasterSlaveConfiguration")
public class DbMasterSlaveConfiguration implements AbstractConfiguration {

	@XmlElement(name = "mainDbJndiName")
	protected String mainDbJndiName = "jdbc/pc";

	@XmlElement(name = "slaveDbJndiName")
	protected String slaveDbJndiName = null;

	@XmlElement(name = "rdDbJndiName")
	protected String rdDbJndiName = "jdbc/pc";

	public String getMainDbJndiName() {
		return mainDbJndiName;
	}

	public String getSlaveDbJndiName() {
		return slaveDbJndiName;
	}

	public String getRdDbJndiName() {
		return rdDbJndiName;
	}

}