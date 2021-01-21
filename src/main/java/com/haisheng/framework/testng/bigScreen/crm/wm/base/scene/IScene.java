package com.haisheng.framework.testng.bigScreen.crm.wm.base.scene;

import com.alibaba.fastjson.JSONObject;

/**
 * 场景接口
 */
public interface IScene {

    JSONObject getJSONObject();

    String getPath();

    String getIpPort();

    void setPage(Integer page);

    void setSize(Integer size);
}
