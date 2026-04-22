package com.mobimoney.pcl.persistence.impl;


import com.packt.cookbook.libraries.common.configuration.AbstractConfiguration;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Configuration for jdbc driver proxy
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "JDBCDriverProxyConfiguration")
public class JDBCDriverProxyConfiguration implements AbstractConfiguration {

	/**
	 * Configuration mapping unit
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "Mapping")
	public static class MappingConfig {

		@XmlAttribute(name = "fakeUrl", required = true)
		private String fakeUrl;
		@XmlAttribute(name = "realUrl", required = true)
		private String realUrl;
		@XmlAttribute(name = "login", required = true)
		private String login;
		@XmlAttribute(name = "password", required = true)
		private String password;
		@XmlAttribute(name = "appRole", required = false)
		private String appRole;
		@XmlAttribute(name = "appPassword", required = false)
		private String appPassword;
		@XmlAttribute(name = "driverClass", required = true)
		private String driverClass;

		/**
		 * @return the appPassword
		 */
		public String getAppPassword() {
			return appPassword;
		}

		/**
		 * @return the appRole
		 */
		public String getAppRole() {
			return appRole;
		}

		/**
		 * @return the driverClass
		 */
		public String getDriverClass() {
			return driverClass;
		}

		/**
		 * @return the fakeUrl
		 */
		public String getFakeUrl() {
			return fakeUrl;
		}

		/**
		 * @return the login
		 */
		public String getLogin() {
			return login;
		}

		/**
		 * @return the password
		 */
		public String getPassword() {
			return password;
		}

		/**
		 * @return the realUrl
		 */
		public String getRealUrl() {
			return realUrl;
		}

		/**
		 * @param appPassword the appPassword to set
		 */
		public void setAppPassword(String appPassword) {
			this.appPassword = appPassword;
		}

		/**
		 * @param appRole the appRole to set
		 */
		public void setAppRole(String appRole) {
			this.appRole = appRole;
		}

		/**
		 * @param driverClass the driverClass to set
		 */
		public void setDriverClass(String driverClass) {
			this.driverClass = driverClass;
		}

		/**
		 * @param fakeUrl the fakeUrl to set
		 */
		public void setFakeUrl(String fakeUrl) {
			this.fakeUrl = fakeUrl;
		}

		/**
		 * @param login the login to set
		 */
		public void setLogin(String login) {
			this.login = login;
		}

		/**
		 * @param password the password to set
		 */
		public void setPassword(String password) {
			this.password = password;
		}

		/**
		 * @param realUrl the realUrl to set
		 */
		public void setRealUrl(String realUrl) {
			this.realUrl = realUrl;
		}
	}

	@XmlElement(name = "Mapping")
	private List<MappingConfig> mappings = new ArrayList<>();

	/**
	 * @return the mappings
	 */
	public List<MappingConfig> getMappings() {
		return mappings;
	}

	/**
	 * @param mappings the mappings to set
	 */
	public void setMappings(List<MappingConfig> mappings) {
		this.mappings = mappings;
	}

}