package com.mobimoney.securityservice.protocol;

/**
 * Useful constants for protocol of security service
 */
public interface ProtocolConstants {

	/** Flag indicates what additional data available */
	byte FLAG_CONTINUE = (byte) 0;
	/** Flag indicates what this final block of data */
	byte FLAG_END = (byte) 1;

	/** Command used by client for getting process key */
	String COMMAND_GET_KEY = "GET_KEY";
	/** Command used by client for encrypt data */
	String COMMAND_ENCRYPT = "ENCRYPT";
	/** Command used by client for decrypt data */
	String COMMAND_DECRYPT = "DECRYPT";

	/** Message, starts session */
	String MESSAGE_HELLO = "HELLO";
	/** Message, ends session */
	String MESSAGE_BYE = "BYE";
	/** Message indicates what service ready for subsequent action */
	String MESSAGE_OK = "OK";
	/** Message indicates error, after this message must follow error description */
	String MESSAGE_ERROR = "ERROR";
}
