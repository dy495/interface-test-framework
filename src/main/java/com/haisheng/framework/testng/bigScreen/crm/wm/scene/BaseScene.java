package com.haisheng.framework.testng.bigScreen.crm.wm.scene;

import com.alibaba.fastjson.JSONObject;

/**
 * @author wangmin
 */
public abstract class BaseScene implements IScene {
    public int page;

    @Override
    public abstract JSONObject getJSONObject();

    @Override
    public abstract String getPath();

    @Override
    public abstract String getIpPort();
}
