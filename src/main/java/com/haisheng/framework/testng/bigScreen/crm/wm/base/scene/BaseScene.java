package com.haisheng.framework.testng.bigScreen.crm.wm.base.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;

/**
 * 场景抽象类
 *
 * @author wangmin
 */
public abstract class BaseScene implements IScene {

    @Override
    public abstract JSONObject getRequestBody();

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

    @Override
    public JSONObject execute(Visitor visitor, boolean checkCode) {
        return visitor.invokeApi(getPath(), getRequestBody(), checkCode);
    }
}
