package com.haisheng.framework.model.experiment.scene;

import com.haisheng.framework.model.experiment.base.RequestLog;
import com.haisheng.framework.model.experiment.core.IUser;

/**
 * 场景接口
 *
 * @author wangmin
 * @date 2020/7/20 21:03
 */
public interface IScene<T> {

    /**
     * 执行场景
     */
    RequestLog<T> run() throws IllegalAccessException;

    /**
     * 获取接口调用的用户
     *
     * @return the user 执行接口调用的用户
     */
    IUser getUser();
}
