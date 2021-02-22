package com.haisheng.framework.testng.bigScreen.crm.wm.base.scenario;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;

/**
 * 接口抽象
 *
 * @author wangmin
 * @date 2021/02/20
 */

public abstract class BaseScenario implements IScenario {
    public Visitor visitor;
    public Boolean checkCode ;

    @Override
    public abstract JSONObject getRequestBody();

    @Override
    public abstract String getPath();

    @Override
    public void setPage(Integer page) {

    }

    @Override
    public void setSize(Integer size) {

    }

    @Override
    public JSONObject getResponse() {
        return visitor.invokeApi(getPath(), getRequestBody(), checkCode);
    }
}
