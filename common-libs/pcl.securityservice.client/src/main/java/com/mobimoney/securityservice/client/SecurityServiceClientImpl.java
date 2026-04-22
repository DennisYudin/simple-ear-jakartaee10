package com.mobimoney.securityservice.client;


import com.mobimoney.securityservice.protocol.ProtocolConstants;
import com.mobimoney.securityservice.protocol.Transport;
import com.mobimoney.securityservice.protocol.exceptions.SecurityServiceException;


import com.packt.cookbook.libraries.common.logging.Log4jHelper;
import com.packt.cookbook.libraries.common.logging.LogHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Client gives api for interaction with security service.<br>
 * It allows many actions on one socket connection, this cause <b>necessary
 * invoke {@link #disconnect()} method at the end of session</b>.
 * <b>This class is NOT thread safe!</b> Methods must be invoked sequentially.
 */
public class SecurityServiceClientImpl implements ProtocolConstants, SecurityServiceClient {

	private static final LogHelper LOG = Log4jHelper.getLogger(SecurityServiceClientImpl.class);

	private final String client;
	private InputStream in;
	private OutputStream out;
	private final int port;
	private Socket socket;
	private Transport t;

	/**
	 * Constructs instance with specified parameters
	 *
	 * @param port       - port of service
	 * @param clientName - name of client
	 */
	public SecurityServiceClientImpl(int port, String clientName) {
		this.port = port;
		client = clientName;
	}

	@Override
	public void connect() throws SecurityServiceException {
		if (socket != null) {
			return;
		}
		try {
			socket = new Socket("127.0.0.1", port);
			in = socket.getInputStream();
			out = socket.getOutputStream();

			t = new Transport();
			t.write(out, MESSAGE_HELLO + " " + client);
			String answer = t.readString(in);
			if (answer.equals(MESSAGE_ERROR)) {
				String msg = t.readString(in);
				disconnect();
				throw new SecurityServiceException(msg);
			}
		} catch (IOException e) {
			disconnect();
			throw new SecurityServiceException("Error connection to service", e);
		}
	}

	@Override
	public byte[] decrypt(byte[] data) throws SecurityServiceException {
		if (data == null || data.length == 0) {
			throw new SecurityServiceException("No data specified");
		}

		sendCommand(COMMAND_DECRYPT);

		try {
			t.write(out, data);
			String answer = t.readString(in);
			if (answer.equals(MESSAGE_ERROR)) {
				throw new SecurityServiceException(t.readString(in));
			}
			return t.read(in);
		} catch (Exception e) {
			throw new SecurityServiceException("Error data decryption", e);
		}
	}

	@Override
	public void disconnect() {
		if (socket != null) {
			try {
				if (!socket.isClosed()) {
					t.write(out, MESSAGE_BYE);
					socket.close();
				}
			} catch (IOException e) {
				LOG.error("Closing socket", e);
			}
			socket = null;
		}
	}

	@Override
	public void close() {
		disconnect();
	}

	@Override
	public byte[] encrypt(byte[] data) throws SecurityServiceException {
		if (data == null || data.length == 0) {
			throw new SecurityServiceException("No data specified");
		}

		sendCommand(COMMAND_ENCRYPT);

		try {
			t.write(out, data);
			String answer = t.readString(in);
			if (answer.equals(MESSAGE_ERROR)) {
				throw new SecurityServiceException(t.readString(in));
			}
			return t.read(in);
		} catch (Exception e) {
			throw new SecurityServiceException("Error data encryption", e);
		}
	}

	private void sendCommand(String command) throws SecurityServiceException {
		connect();
		try {
			t.write(out, command);
			if (t.readString(in).equals(MESSAGE_ERROR)) {
				throw new SecurityServiceException(t.readString(in));
			}
		} catch (IOException e) {
			throw new SecurityServiceException("Error sending command", e);
		}
	}

}