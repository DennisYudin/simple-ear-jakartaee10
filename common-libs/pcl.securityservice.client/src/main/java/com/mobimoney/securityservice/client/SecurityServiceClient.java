package com.mobimoney.securityservice.client;

import com.mobimoney.securityservice.protocol.exceptions.SecurityServiceException;

/**
 * Interface for clients of SecurityService
 */
public interface SecurityServiceClient extends AutoCloseable {

	/**
	 * Connects to service
	 * 
	 * @throws SecurityServiceException if error of connecting to service
	 */
	void connect() throws SecurityServiceException;

	/**
	 * Decrypts given bytes
	 * 
	 * @param data - bytes to be decrypted
	 * @return - original byte array
	 * @throws SecurityServiceException if any communication error occurred
	 */
	byte[] decrypt(byte[] data) throws SecurityServiceException;

	/**
	 * Disconnects client from service
	 */
	void disconnect();

	/**
	 * Encrypts given bytes
	 * 
	 * @param data - bytes to be encrypted
	 * @return - encrypted byte array
	 * @throws SecurityServiceException if any communication error occurred
	 */
	byte[] encrypt(byte[] data) throws SecurityServiceException;

	@Override
	void close();

}