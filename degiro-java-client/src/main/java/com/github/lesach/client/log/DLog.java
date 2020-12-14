package com.github.lesach.client.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author indiketa
 */
public class DLog {

    private static final Logger logger = LoggerFactory.getLogger(DLog.class);

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void debug(String message, Throwable t) {
        logger.debug(message, t);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void error(String message, Throwable t) {
        logger.error(message, t);
    }

    public static boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    public static void warn(String message) {
        logger.warn(message);
    }

    public static void warn(String message, Throwable t) {
        logger.warn(message, t);
    }
    
    public static void info(String message) {
        logger.info(message);
    }

    public static void info(String message, Throwable t) {
        logger.info(message, t);
    }

    public static boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    public static void fatal(String message) {
        logger.error(message);
    }

    public static void fatal(String message, Throwable t) {
        logger.error(message, t);
    }

    public static boolean isFatalEnabled() {
        return logger.isErrorEnabled();
    }

    public static boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public static void trace(String message) {
        logger.trace(message);
    }

    public static void trace(String message, Throwable t) {
        logger.trace(message, t);
    }

    public static boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

}
