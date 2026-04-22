package com.mobimoney.pcl.configuration;

/**
 * Occurs when cannot reading config file
 */
public class ConfiguratorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4652369697902548081L;

	/**
	 * @param message - error description
	 */
	public ConfiguratorException(String message) {
		super(message);
	}

	/**
	 * @param message - error description
	 * @param cause - error reason
	 */
	public ConfiguratorException(String message, Throwable cause) {
		super(message, cause);
	}
}
