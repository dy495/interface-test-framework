package com.haisheng.framework.model.experiment.checker;

/**
 * @author wangmin
 * @date 2020/7/20 10:54
 */
public interface IChecker {
    /**
     * 进行check
     */
    void check();

    /**
     * 获取报错信息
     *
     * @return String
     */

    String getErrorMsg();

    /**
     * 获取checker详情
     *
     * @return String
     */
    String getCheckerInfo();
}
