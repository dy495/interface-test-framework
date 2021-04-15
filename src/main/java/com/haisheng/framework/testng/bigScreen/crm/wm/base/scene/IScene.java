package com.haisheng.framework.testng.bigScreen.crm.wm.base.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;

import java.util.List;

/**
 * 场景接口
 */
public interface IScene {

    JSONObject getBody();

    String getPath();

    String getIpPort();

    void setPage(Integer page);

    void setSize(Integer size);

    JSONObject invoke(VisitorProxy visitor, boolean checkCode);

    JSONObject invoke(VisitorProxy visitor);

    IScene remove(String... keys);

    List<String> getKeyList();

}
