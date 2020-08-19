package com.haisheng.framework.model.experiment.checker;

/**
 * 校验接口
 */
public interface IChecker {
    /**
     * 进行校验
     */
    void check();

    /**
     * 获取错误信息
     *
     * @return errorMsg
     */
    String getErrorMsg();
}
