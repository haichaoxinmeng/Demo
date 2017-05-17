package cn.roc.dm.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerFactoryUtil {

	private static Logger accessLog = LoggerFactory.getLogger("accessLog");
    private static Logger bizLog = LoggerFactory.getLogger("bizLog");
    private static Logger infoLog = LoggerFactory.getLogger("infoLog");
    private static Logger errorLog = LoggerFactory.getLogger("errorLog");

    public static void access(String message) {
        access(LogLevel.DEBUG, message);
    }

    public static void access(LogLevel logLevel, String message) {
        if (logLevel == null) {
            accessLog.info(message);
        }
        if (logLevel == LogLevel.DEBUG) {
            accessLog.debug(message);
        } else if (logLevel == LogLevel.INFO) {
            accessLog.info(message);
        } else if (logLevel == LogLevel.ERROR) {
            accessLog.error(message);
        } else {
            accessLog.info(message);
        }
    }

    public static void biz(String message) {
        biz(LogLevel.DEBUG, message);
    }

    public static void biz(LogLevel logLevel, String message) {
        if (logLevel == null) {
            bizLog.info(message);
        }
        if (logLevel == LogLevel.DEBUG) {
            bizLog.debug(message);
        } else if (logLevel == LogLevel.INFO) {
            bizLog.info(message);
        } else if (logLevel == LogLevel.ERROR) {
            bizLog.error(message);
        } else {
            bizLog.info(message);
        }
    }

    public static void info(String message) {
        info(LogLevel.DEBUG, message);
    }

    public static void info(LogLevel logLevel, String message) {
        if (logLevel == null) {
            infoLog.info(message);
        }
        if (logLevel == LogLevel.DEBUG) {
            infoLog.debug(message);
        } else if (logLevel == LogLevel.INFO) {
            infoLog.info(message);
        } else if (logLevel == LogLevel.ERROR) {
            infoLog.error(message);
        } else {
            infoLog.info(message);
        }
    }

    public static void error(String message, Throwable t) {
        errorLog.error(message, t);
    }
    
    public static void error(String message) {
        errorLog.error(message);
    }
}
