package com.mobimoney.securityservice.protocol;




import com.packt.cookbook.libraries.common.logging.Log4jHelper;
import com.packt.cookbook.libraries.common.logging.LogHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Class provides transport functionality for security service protocol
 */
public class Transport implements ProtocolConstants {
	private static final LogHelper LOG = Log4jHelper.getLogger(Transport.class);

	private static final int BUFFER_SIZE = 16;

	//max count of trying read least bytes
	private static final int MAX_READS_COUNT = 10;
	//from witch try need Thread.sleep before reading
	private static final int SLEEP_FROM_TRY = 3;
	//from sleep in ms
	private static final int SLEEP_TIME_MS = 5;

	//transport block of data
	//first two bytes used for service usage (flag of completing and size of data)
	private byte[] buffer = new byte[BUFFER_SIZE + 2];

	/**
	 * Read bytes of data
	 * 
	 * @param in - {@link InputStream}
	 * @return byte array of receiving data
	 * @throws IOException if communication error occurred
	 */
	public byte[] read(InputStream in) throws IOException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		byte flag;
		do {
			int total = in.read(buffer);
			int tries = 0;
			while (total > -1 && total < buffer.length) {
				if (LOG.isTraceEnabled()) {
					LOG.trace("Received just " + total + " bytes, trying to read another " + (buffer.length - total));
				}
				tries++;
				if (tries > MAX_READS_COUNT) {
					LOG.warn("Max read count exceeded.");
					break;
				}
				if (tries >= SLEEP_FROM_TRY) {
					try {
						Thread.sleep(SLEEP_TIME_MS);
					} catch (InterruptedException e) {
						LOG.warn("Interrupted while waiting least bytes.");
						break;
					}
				}
				int cnt = in.read(buffer, total, buffer.length - total);
				if (cnt > -1) {
					total += cnt;
				} else {
					break;
				}
			}
			if (total < buffer.length) {
				throw new IOException("Error reading record from input stream.");
			}
			flag = buffer[0];
			int size = buffer[1];
			data.write(buffer, 2, size);
		} while (flag == FLAG_CONTINUE);

		return data.toByteArray();
	}

	/**
	 * Read string from socket
	 * 
	 * @param in - {@link InputStream}
	 * @return received string
	 * @throws IOException if communication error occurred
	 */
	public String readString(InputStream in) throws IOException {
		return new String(read(in));
	}

	/**
	 * Sends error in convention with protocol
	 * 
	 * @param out - {@link OutputStream} of socket
	 * @param msg - error message
	 * @throws IOException if communication error occurred
	 */
	public void sendError(OutputStream out, String msg) throws IOException {
		write(out, MESSAGE_ERROR);
		write(out, msg);
	}

	/**
	 * Writes byte array to socket
	 * 
	 * @param out - {@link OutputStream} of socket
	 * @param data - bytes to send
	 * @throws IOException if communication error occurred
	 */
	public void write(OutputStream out, byte[] data) throws IOException {
		int len = data.length;
		int offset = 0;
		int nextOffset;

		while (offset < len) {
			Arrays.fill(buffer, (byte) 0);
			nextOffset = offset + BUFFER_SIZE;
			buffer[0] = (nextOffset >= len ? FLAG_END : FLAG_CONTINUE);
			int portionSize = Math.min(nextOffset, len) - offset;
			buffer[1] = (byte) portionSize;
			System.arraycopy(data, offset, buffer, 2, portionSize);
			out.write(buffer);
			offset = nextOffset;
		}
	}

	/**
	 * Writes string to socket
	 * 
	 * @param out - {@link OutputStream} of socket
	 * @param data - string to send
	 * @throws IOException if communication error occurred
	 */
	public void write(OutputStream out, String data) throws IOException {
		write(out, data.getBytes());
	}
}
