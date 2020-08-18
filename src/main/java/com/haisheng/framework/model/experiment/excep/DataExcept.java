package com.haisheng.framework.model.experiment.excep;

/**
 * 错误提示
 *
 * @author wangmin
 */
public class DataExcept extends RuntimeException {
    public DataExcept(String message) {
        super(message);
    }
}
