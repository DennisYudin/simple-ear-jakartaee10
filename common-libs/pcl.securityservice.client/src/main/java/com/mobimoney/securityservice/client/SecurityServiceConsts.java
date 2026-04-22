package com.mobimoney.securityservice.client;

/**
 * Security service constants
 */
public class SecurityServiceConsts {

	public static final String SERVICE_PORT_PROPERTY = "com.mobimoney.securityservice.port";
	public static final int SERVICE_PORT;

	static {
		String port = System.getProperty(SERVICE_PORT_PROPERTY);
		if (port == null || port.isEmpty()) {
			SERVICE_PORT = 8989;
		} else {
			SERVICE_PORT = Integer.parseInt(port);
		}
	}

	private SecurityServiceConsts() {
	}
}
