package com.haisheng.framework.testng.bigScreen.crm.wm.base.scenario;

import com.alibaba.fastjson.JSONObject;

/**
 * 场景接口
 *
 * @author wangmin
 * @date 2021/02/20
 */
public interface IScenario {

    void setPage(Integer page);

    void setSize(Integer size);

    JSONObject getRequestBody();

    String getPath();

    JSONObject getResponse();
}
