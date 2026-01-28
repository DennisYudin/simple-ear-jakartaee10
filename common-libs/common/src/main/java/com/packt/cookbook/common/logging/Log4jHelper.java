package com.packt.cookbook.common.logging;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of {@link LogHelper}
 */
public class Log4jHelper implements LogHelper {

	private static final String TR_EXT_ID_NAME = "[extTrId: %d] ";

	private static final String TR_ID_N_EXT_ID_NAME = "[trId: %d, extTrId: %d] ";

	private static final String TR_ID_NAME = "[trId: %d] ";

	private static final Map<Class<? extends Throwable>, Level> exceptions = new ConcurrentHashMap<>();

	private final Logger log;

	private Log4jHelper(Logger log) {
		this.log = log;
	}

	/**
	 * Creates instance of {@link LogHelper} with specified class
	 *
	 * @param clazz - class
	 * @return {@link LogHelper} instance
	 */
	public static LogHelper getLogger(Class<?> clazz) {
		return new Log4jHelper(LogManager.getLogger(clazz));
	}

	/**
	 * Creates instance of {@link LogHelper} with specified logger
	 *
	 * @param log - {@link Logger}
	 * @return {@link LogHelper} instance
	 */
	public static LogHelper getLogger(Logger log) {
		return new Log4jHelper(log);
	}

	/**
	 * Register exception class and corresponding log level for it
	 *
	 * @param clazz - class of exception
	 * @param level - log level for specified exception class
	 */
	public static void registerException(Class<? extends Throwable> clazz, Level level) {
		exceptions.put(clazz, level);
	}

	@Override
	public void debug(Long trId, Long extTrId, String msg, Object... params) {
		log(Level.DEBUG, trId, extTrId, null, msg, params);
	}

	@Override
	public void debug(Long trId, String msg, Object... params) {
		debug(trId, null, msg, params);
	}

	@Override
	public void debug(String msg, Object... params) {
		debug(null, msg, params);
	}

	@Override
	public void error(Long trId, Long extTrId, String msg, Object... params) {
		error(trId, extTrId, null, msg, params);
	}

	@Override
	public void error(Long trId, Long extTrId, Throwable t, String msg, Object... params) {
		log(Level.ERROR, trId, extTrId, t, msg, params);
	}

	@Override
	public void error(Long trId, String msg, Object... params) {
		error(trId, (Throwable) null, msg, params);
	}

	@Override
	public void error(Long trId, Throwable t, String msg, Object... params) {
		error(trId, null, t, msg, params);
	}

	@Override
	public void error(String msg, Object... params) {
		error((Throwable) null, msg, params);
	}

	@Override
	public void error(Throwable t, String msg, Object... params) {
		error(null, t, msg, params);
	}

	@Override
	public void fatal(String msg, Object... params) {
		log(Level.FATAL, null, null, null, msg, params);

	}

	@Override
	public void fatal(Throwable t, String msg, Object... params) {
		log(Level.FATAL, null, null, t, msg, params);
	}

	@Override
	public void info(Long trId, Long extTrId, String msg, Object... params) {
		info(trId, extTrId, null, msg, params);
	}

	@Override
	public void info(Long trId, Long extTrId, Throwable t, String msg, Object... params) {
		log(Level.INFO, trId, extTrId, t, msg, params);
	}

	@Override
	public void info(Long trId, String msg, Object... params) {
		info(trId, (Throwable) null, msg, params);
	}

	@Override
	public void info(Long trId, Throwable t, String msg, Object... params) {
		info(trId, null, t, msg, params);
	}

	@Override
	public void info(String msg, Object... params) {
		info((Throwable) null, msg, params);
	}

	@Override
	public void info(Throwable t, String msg, Object... params) {
		info(null, t, msg, params);
	}

	@Override
	public boolean isDebugEnabled() {
		return log.isEnabled(Level.DEBUG);
	}

	@Override
	public boolean isEnabledFor(Level level) {
		return log.isEnabled(level);
	}

	@Override
	public boolean isErrorEnabled() {
		return log.isEnabled(Level.ERROR);
	}

	@Override
	public boolean isFatalEnabled() {
		return log.isEnabled(Level.FATAL);
	}

	@Override
	public boolean isInfoEnabled() {
		return log.isEnabled(Level.INFO);
	}

	@Override
	public boolean isTraceEnabled() {
		return log.isEnabled(Level.TRACE);
	}

	@Override
	public boolean isWarnEnabled() {
		return log.isEnabled(Level.WARN);
	}

	@Override
	public void log(Level level, Long trId, Long extTrId, Throwable t, String msg, Object... params) {
		Level actualLevel = null;
		if (t != null && !exceptions.isEmpty()) {
			actualLevel = exceptions.get(t.getClass());
		}
		if (actualLevel == null) {
			actualLevel = level;
		}
		if (log.isEnabled(actualLevel)) {
			String logStr = "";
			if (trId != null && extTrId != null) {
				logStr = String.format(TR_ID_N_EXT_ID_NAME, trId, extTrId);
			} else if (trId != null) {
				logStr = String.format(TR_ID_NAME, trId);
			} else if (extTrId != null) {
				logStr = String.format(TR_EXT_ID_NAME, extTrId);
			}
			if (msg != null) {
				logStr += (params != null && params.length > 0) ? String.format(msg, params) : msg;
			}
			if (t == null) {
				log.log(actualLevel, logStr);
			} else {
				log.log(actualLevel, logStr, t);
			}
		}
	}

	@Override
	public void log(Level level, String msg, Object... params) {
		if (log.isEnabled(level)) {
			String logStr;
			if (msg != null) {
				logStr = (params != null && params.length > 0) ? String.format(msg, params) : msg;
			} else {
				logStr = "null";
			}
			log.log(level, logStr);
		}
	}

	@Override
	public void log(Level level, String msg) {
		if (log.isEnabled(level)) {
			log.log(level, msg);
		}
	}

	@Override
	public void trace(Long trId, Long extTrId, String msg, Object... params) {
		log(Level.TRACE, trId, extTrId, null, msg, params);
	}

	@Override
	public void trace(Long trId, String msg, Object... params) {
		trace(trId, null, msg, params);
	}

	@Override
	public void trace(String msg, Object... params) {
		trace(null, msg, params);
	}

	@Override
	public void warn(Long trId, Long extTrId, String msg, Object... params) {
		warn(trId, extTrId, null, msg, params);
	}

	@Override
	public void warn(Long trId, Long extTrId, Throwable t, String msg, Object... params) {
		log(Level.WARN, trId, extTrId, t, msg, params);
	}

	@Override
	public void warn(Long trId, String msg, Object... params) {
		warn(trId, (Throwable) null, msg, params);
	}

	@Override
	public void warn(Long trId, Throwable t, String msg, Object... params) {
		warn(trId, null, t, msg, params);
	}

	@Override
	public void warn(String msg, Object... params) {
		warn((Throwable) null, msg, params);
	}

	@Override
	public void warn(Throwable t, String msg, Object... params) {
		warn(null, t, msg, params);
	}

}