package com.packt.cookbook.common.logging;


import org.apache.logging.log4j.Level;

/**
 * Interface of helper for logging
 */
public interface LogHelper {

	/**
	 * @param trId    - id of transaction
	 * @param extTrId - external id of transaction
	 * @param msg     - message to log
	 * @param params  - parameters for message
	 */
	void debug(Long trId, Long extTrId, String msg, Object... params);

	/**
	 * @param trId   - id of transaction
	 * @param msg    - message to log
	 * @param params - parameters for message
	 */
	void debug(Long trId, String msg, Object... params);

	/**
	 * @param msg    - message to log
	 * @param params - parameters for message
	 */
	void debug(String msg, Object... params);

	/**
	 * @param trId    - id of transaction
	 * @param extTrId - external id of transaction
	 * @param msg     - message to log
	 * @param params  - parameters for message
	 */
	void error(Long trId, Long extTrId, String msg, Object... params);

	/**
	 * @param trId    - id of transaction
	 * @param extTrId - external id of transaction
	 * @param t       - {@link Throwable}
	 * @param msg     - message to log
	 * @param params  - parameters for message
	 */
	void error(Long trId, Long extTrId, Throwable t, String msg, Object... params);

	/**
	 * @param trId   - id of transaction
	 * @param msg    - message to log
	 * @param params - parameters for message
	 */
	void error(Long trId, String msg, Object... params);

	/**
	 * @param trId   - id of transaction
	 * @param t      - {@link Throwable}
	 * @param msg    - message to log
	 * @param params - parameters for message
	 */
	void error(Long trId, Throwable t, String msg, Object... params);

	/**
	 * @param msg    - message to log
	 * @param params - parameters for message
	 */
	void error(String msg, Object... params);

	/**
	 * @param t      - {@link Throwable}
	 * @param msg    - message to log
	 * @param params - parameters for message
	 */
	void error(Throwable t, String msg, Object... params);

	/**
	 * @param msg    - message to log
	 * @param params - parameters for message
	 */
	void fatal(String msg, Object... params);

	/**
	 * @param t      - {@link Throwable}
	 * @param msg    - message to log
	 * @param params - parameters for message
	 */
	void fatal(Throwable t, String msg, Object... params);

	/**
	 * @param trId    - id of transaction
	 * @param extTrId - external id of transaction
	 * @param msg     - message to log
	 * @param params  - parameters for message
	 */
	void info(Long trId, Long extTrId, String msg, Object... params);

	/**
	 * @param trId    - id of transaction
	 * @param extTrId - external id of transaction
	 * @param t       - {@link Throwable}
	 * @param msg     - message to log
	 * @param params  - parameters for message
	 */
	void info(Long trId, Long extTrId, Throwable t, String msg, Object... params);

	/**
	 * @param trId   - id of transaction
	 * @param msg    - message to log
	 * @param params - parameters for message
	 */
	void info(Long trId, String msg, Object... params);

	/**
	 * @param trId   - id of transaction
	 * @param t      - {@link Throwable}
	 * @param msg    - message to log
	 * @param params - parameters for message
	 */
	void info(Long trId, Throwable t, String msg, Object... params);

	/**
	 * @param msg    - message to log
	 * @param params - parameters for message
	 */
	void info(String msg, Object... params);

	/**
	 * @param t      - {@link Throwable}
	 * @param msg    - message to log
	 * @param params - parameters for message
	 */
	void info(Throwable t, String msg, Object... params);

	/**
	 * @return true if "DEBUG" level enabled
	 */
	boolean isDebugEnabled();

	/**
	 * @param level - log level
	 * @return true if log for level enabled
	 */
	boolean isEnabledFor(Level level);

	/**
	 * @return true if "ERROR" level enabled
	 */
	boolean isErrorEnabled();

	/**
	 * @return true if "FATAL" level enabled
	 */
	boolean isFatalEnabled();

	/**
	 * @return true if "INFO" level enabled
	 */
	boolean isInfoEnabled();

	/**
	 * @return true if "TRACE" level enabled
	 */
	boolean isTraceEnabled();

	/**
	 * @return true if "WARN" level enabled
	 */
	boolean isWarnEnabled();

	/**
	 * @param level   level of log
	 * @param msg     - string template
	 * @param params  - parameters
	 */
	void log(Level level, String msg, Object... params);

	/**
	 * @param level   level of log
	 * @param msg     - string template
	 */
	void log(Level level, String msg);

	/**
	 * @param level   level of log
	 * @param trId    - transaction id
	 * @param extTrId - external transaction Id
	 * @param t       - exception
	 * @param msg     - string template
	 * @param params  - parameters
	 */
	void log(Level level, Long trId, Long extTrId, Throwable t, String msg, Object... params);

	/**
	 * @param trId    - id of transaction
	 * @param extTrId - external id of transaction
	 * @param msg     - message to log
	 * @param params  - parameters for message
	 */
	void trace(Long trId, Long extTrId, String msg, Object... params);

	/**
	 * @param trId   - id of transaction
	 * @param msg    - message to log
	 * @param params - parameters for message
	 */
	void trace(Long trId, String msg, Object... params);

	/**
	 * @param msg    - message to log
	 * @param params - parameters for message
	 */
	void trace(String msg, Object... params);

	/**
	 * @param trId    - id of transaction
	 * @param extTrId - external id of transaction
	 * @param msg     - message to log
	 * @param params  - parameters for message
	 */
	void warn(Long trId, Long extTrId, String msg, Object... params);

	/**
	 * @param trId    - id of transaction
	 * @param extTrId - external id of transaction
	 * @param t       - {@link Throwable}
	 * @param msg     - message to log
	 * @param params  - parameters for message
	 */
	void warn(Long trId, Long extTrId, Throwable t, String msg, Object... params);

	/**
	 * @param trId   - id of transaction
	 * @param msg    - message to log
	 * @param params - parameters for message
	 */
	void warn(Long trId, String msg, Object... params);

	/**
	 * @param trId   - id of transaction
	 * @param t      - {@link Throwable}
	 * @param msg    - message to log
	 * @param params - parameters for message
	 */
	void warn(Long trId, Throwable t, String msg, Object... params);

	/**
	 * @param msg    - message to log
	 * @param params - parameters for message
	 */
	void warn(String msg, Object... params);

	/**
	 * @param t      - {@link Throwable}
	 * @param msg    - message to log
	 * @param params - parameters for message
	 */
	void warn(Throwable t, String msg, Object... params);
}
