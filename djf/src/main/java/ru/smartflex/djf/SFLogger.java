package ru.smartflex.djf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SFLogger {

    private static Logger LOGGER = LoggerFactory.getLogger(SFLogger.class);
    private static final String ACTIVITY_PREFIX = "*** Activity *** ";

    private SFLogger() {
    }

    public static void info(Object... messages) {
        if (LOGGER.isInfoEnabled()) {
            StringBuilder sb = formMessage(messages);
            if (sb != null) {
                LOGGER.info(sb.toString());
            }
        }
    }

    public static void error(String message, Throwable t) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error(message, t);
        }
    }

    public static void activityInfo(Object... msg) {
        if (LOGGER.isInfoEnabled()) {
            StringBuilder sb = formMessage(msg);
            if (sb != null) {
                StringBuilder sbOut = new StringBuilder();
                sbOut.append(ACTIVITY_PREFIX);
                sbOut.append(sb.toString());
                LOGGER.info(sbOut.toString());
            }
        }
    }

    public static void error(String... messages) {
        if (messages != null) {
            StringBuilder sb = new StringBuilder();
            for (String msg : messages) {
                if (msg != null) {
                    sb.append(msg);
                }
            }
            LOGGER.error(sb.toString());
        }
    }

    public static void warn(@SuppressWarnings("rawtypes") Class clazz,
                            Object... messages) {
        if (LOGGER.isWarnEnabled()) {
            StringBuilder sb = formMessage(clazz, messages);
            if (sb != null) {
                LOGGER.warn(sb.toString());
            }
        }
    }

    public static void error(@SuppressWarnings("rawtypes") Class clazz,
                             Object... messages) {
        if (LOGGER.isErrorEnabled()) {
            StringBuilder sb = formMessage(clazz, messages);
            if (sb != null) {
                LOGGER.error(sb.toString());
            }
        }
    }

    private static StringBuilder formMessage(
            @SuppressWarnings("rawtypes") Class clazz, Object... messages) {
        StringBuilder sb = null;
        if (messages != null) {
            sb = new StringBuilder();
            sb.append("[");
            sb.append(clazz.getSimpleName());
            sb.append("] ");
            for (Object obj : messages) {
                if (obj != null) {
                    sb.append(obj);
                }
            }
        }
        return sb;
    }

    private static StringBuilder formMessage(Object... messages) {
        StringBuilder sb = null;
        if (messages != null) {
            sb = new StringBuilder();
            for (Object obj : messages) {
                if (obj != null) {
                    sb.append(obj);
                }
            }
        }
        return sb;
    }

    public static void debug(@SuppressWarnings("rawtypes") Class clazz,
                             Object... messages) {
        if (LOGGER.isDebugEnabled()) {
            StringBuilder sb = formMessage(clazz, messages);
            if (sb != null) {
                LOGGER.debug(sb.toString());
            }
        }
    }

    public static void debug(Object... messages) {
        if (LOGGER.isDebugEnabled()) {
            StringBuilder sb;
            if (messages != null) {
                sb = new StringBuilder();
                for (Object obj : messages) {
                    if (obj != null) {
                        sb.append(obj);
                    }
                }
                LOGGER.debug(sb.toString());
            }
        }
    }

    @SuppressWarnings("unused")
    public static boolean isDebugEnabled() {
        return LOGGER.isDebugEnabled();
    }

}
