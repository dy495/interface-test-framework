package com.haisheng.framework.testng.bigScreen.crm.wm.base.scene;

import com.alibaba.fastjson.JSONObject;

/**
 * @author wangmin
 */
public abstract class BaseScene implements IScene {

    @Override
    public abstract JSONObject getJSONObject();

    @Override
    public abstract String getPath();

    @Override
    public String getIpPort() {
        return null;
    }

    @Override
    public void setPage(Integer page) {

    }

    @Override
    public void setSize(Integer size) {

    }
}
