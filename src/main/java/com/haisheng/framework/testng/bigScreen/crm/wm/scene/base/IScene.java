package com.haisheng.framework.testng.bigScreen.crm.wm.scene.base;

import com.alibaba.fastjson.JSONObject;

/**
 * 场景接口
 */
public interface IScene {

    JSONObject getJSONObject();

    String getPath();

    String getIpPort();
}
