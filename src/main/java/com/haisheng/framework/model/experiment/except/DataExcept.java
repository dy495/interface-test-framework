package com.haisheng.framework.model.experiment.except;

/**
 * 错误信息
 *
 * @author wangmin
 * @date 2020/7/21 10:06
 */
public class DataExcept extends RuntimeException {
    public DataExcept(String message) {
        super(message);
    }
}
