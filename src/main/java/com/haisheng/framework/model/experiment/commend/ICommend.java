package com.haisheng.framework.model.experiment.commend;

import com.haisheng.framework.model.experiment.core.Api;
import okhttp3.Response;

import java.io.IOException;

/**
 * 建立网络链接接口
 *
 * @author wangmin
 * @date 2020/7/21 9:48
 */
public interface ICommend {
    /**
     * 建立连接
     *
     * @param api api参数
     * @param url url
     * @return Response
     * @throws IllegalAccessException
     * @throws IOException
     */
    Response execute(Api api, String url) throws IllegalAccessException, IOException;
}
