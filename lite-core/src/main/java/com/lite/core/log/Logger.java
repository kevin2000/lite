package com.lite.core.log;

import java.util.Date;

import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;

import com.lite.core.dto.Log;

public class Logger {

	protected String className;
	protected org.slf4j.Logger logger;
	
	protected static final Logger genericLogger = new Logger(Logger.class); // Static logger for generic use

	//private static LogRepo logRepo = null;
	
	/**
	 * Initialize inner logger
	 */
	public Logger(Class klass) {
		className = klass.getSimpleName();
		logger = LoggerFactory.getLogger(klass);
	}
	
	/**
	 * Get generic logger
	 */
	public static Logger generic() {
		return genericLogger;
	}
	
	/**
	 * Init beans
	 */
	protected static void init() {
		/*
		 * if (logRepo == null) { logRepo = SpringUtil.getBean(LogRepo.class); }
		 */
	}
	
	/**
	 * Save to db
	 */
	protected Log save(String type, String method, boolean findUser, String target, String message, String extra) {
		init();
		/*
		 * Log log = new Log(type, className, method, findUser ?
		 * SessionUtils.getCurrentUserName() : null, target, message, extra, new
		 * Date()); logRepo.save(log); return log;
		 */
		return null;
	}

	/**
	 * Log anything, always save db
	 */
	public void log(String type, String method, boolean findUser, String target, String message, String extra) {
		boolean output = false;
		if ("error".equals(type))
			logger.error(message);
		else if ("warn".equals(type))
			logger.warn(message);
		else if ("info".equals(type))
			logger.info(message);
		else
			output = true;
		
		Log log = save(type, method, findUser, target, message, extra);
		
		if (output && log != null)
			logger.info(String.format("%s, %s, %s, %s, %s, %s", log.getType(), log.getMethod(), log.getUser(), log.getTarget(), log.getMessage(), log.getExtra()));
	}
	
	/**
	 * info
	 */
	public void info(String message) {
		logger.info(message);
	}

	/**
	 * warn
	 */
	public void warn(String message) {
		logger.warn(message);
	}

	/**
	 * debug
	 */
	public void debug(String message) {
		logger.debug(message);
	}

	/**
	 * error
	 */
	public void error(String message) {
		error(message, null);
	}

	/**
	 * error
	 */
	public void error(String message, Throwable e) {
		logger.error(message, e);
		//save("error", null, false, null, message, e == null ? null : (e.getMessage() + "\r\n" + Util.arrayJoin(e.getStackTrace(), "\r\n")));
	}

	/**
	 * access
	 */
	public void access(String method, String target, String message) {
		Log log = save("access", method, true, target, message, null);
		logger.info(String.format("Access: %s, %s, %s, %s", method, log.getUser(), target, message));
	}

	/**
	 * audit
	 */
	public void audit(String method, String target, String message) {
		Log log = save("audit", method, true, target, message, null);
		logger.info(String.format("Audit: %s, %s, %s, %s", method, log.getUser(), target, message));
	}
}
