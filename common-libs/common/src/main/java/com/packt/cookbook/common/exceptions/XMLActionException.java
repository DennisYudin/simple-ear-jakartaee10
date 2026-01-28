package com.packt.cookbook.common.exceptions;

/**
 * used in XMLToolkit services
 */
public class XMLActionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3020635270764397279L;

	/**
	 * 
	 */
	public XMLActionException() {
		super();
	}

	/**
	 * @param message message
	 */
	public XMLActionException(String message) {
		super(message);
	}

	/**
	 * @param message message
	 * @param cause cause
	 */
	public XMLActionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause cause
	 */
	public XMLActionException(Throwable cause) {
		super(cause);
	}

}
