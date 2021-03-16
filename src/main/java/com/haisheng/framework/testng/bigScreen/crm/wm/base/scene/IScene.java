package com.haisheng.framework.testng.bigScreen.crm.wm.base.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;

/**
 * 场景接口
 */
public interface IScene {

    JSONObject getRequestBody();

    String getPath();

    String getIpPort();

    void setPage(Integer page);

    void setSize(Integer size);

    /**
     * 执行场景
     *
     * @param visitor   要执行的产品
     * @param checkCode 是否校验code
     * @return response.data|response 校验时返回response.data/不校验时返回response
     */
    JSONObject execute(VisitorProxy visitor, boolean checkCode);

}
