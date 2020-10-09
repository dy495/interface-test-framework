package com.haisheng.framework.testng.bigScreen.crm.wm.scene.base;

import com.alibaba.fastjson.JSONObject;

/**
 * @author wangmin
 */
public abstract class BaseScene implements IScene {

    @Override
    public abstract JSONObject getJson();

    @Override
    public abstract String getPath();

    @Override
    public abstract String getIpPort();
}
