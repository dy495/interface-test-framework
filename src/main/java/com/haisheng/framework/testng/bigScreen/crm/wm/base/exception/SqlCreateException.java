package com.haisheng.framework.testng.bigScreen.crm.wm.base.exception;

/**
 * sql创建错误
 */
public class SqlCreateException extends RuntimeException {
    public SqlCreateException(String message) {
        super(message);
    }
}