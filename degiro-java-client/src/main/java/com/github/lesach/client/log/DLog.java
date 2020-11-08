package com.github.lesach.client.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author indiketa
 */
public class DLog {

    private static final Logger logger = LogManager.getLogger(DLog.class);

    public static void debug(Object message) {
        logger.debug(message);
    }

    public static void debug(Object message, Throwable t) {
        logger.debug(message, t);
    }

    public static void error(Object message) {
        logger.error(message);
    }

    public static void error(Object message, Throwable t) {
        logger.error(message, t);
    }

    public static boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    public static void warn(Object message) {
        logger.warn(message);
    }

    public static void warn(Object message, Throwable t) {
        logger.warn(message, t);
    }
    
    public static void info(Object message) {
        logger.info(message);
    }

    public static void info(Object message, Throwable t) {
        logger.info(message, t);
    }

    public static boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    public static void fatal(Object message) {
        logger.fatal(message);
    }

    public static void fatal(Object message, Throwable t) {
        logger.fatal(message, t);
    }

    public static boolean isFatalEnabled() {
        return logger.isFatalEnabled();
    }

    public static boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public static void trace(Object message) {
        logger.trace(message);
    }

    public static void trace(Object message, Throwable t) {
        logger.trace(message, t);
    }

    public static boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

}
