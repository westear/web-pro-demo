package com.learn.log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MyJulLogImpl implements org.apache.ibatis.logging.Log {

    private final Logger logger;

    public MyJulLogImpl(String clazz) {
        logger = Logger.getLogger(clazz);
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isLoggable(Level.FINER);
    }

    @Override
    public void error(String s, Throwable e) {
        logger.log(Level.SEVERE, s, e);
    }

    @Override
    public void error(String s) {
        logger.log(Level.SEVERE, s);
    }

    @Override
    public void debug(String s) {
        logger.info(s);
    }

    @Override
    public void trace(String s) {
        logger.log(Level.FINER, s);
    }

    @Override
    public void warn(String s) {
        logger.log(Level.WARNING, s);
    }
}
