package com.mobimoney.securityservice.protocol.exceptions;

/**
 * Exception describes error of communication with security service
 */
public class SecurityServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7298457700794113088L;

	/**
	 * Empty constructor
	 */
	public SecurityServiceException() {

	}

	/**
	 * @param msg - error description
	 */
	public SecurityServiceException(String msg) {
		super(msg);
	}

	/**
	 * @param msg - error description
	 * @param t - error reason
	 */
	public SecurityServiceException(String msg, Throwable t) {
		super(msg, t);
	}
}
