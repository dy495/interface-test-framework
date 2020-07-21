package com.haisheng.framework.model.experiment.core;

import com.haisheng.framework.model.experiment.base.RequestLog;

/**
 * @author wangmin
 * @date 2020/7/21 13:04
 */
public interface IUser<T> {

    RequestLog<T> invoke(Api api, String address) throws IllegalAccessException;
}
