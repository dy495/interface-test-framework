package com.haisheng.framework.model.experiment.except;

/**
 * 校验错误
 *
 * @author wangmin
 * @date 2020/7/22 10:21
 */
public class CheckExcept extends AssertionError {
    public CheckExcept(String message) {
        super(message);
    }
}
