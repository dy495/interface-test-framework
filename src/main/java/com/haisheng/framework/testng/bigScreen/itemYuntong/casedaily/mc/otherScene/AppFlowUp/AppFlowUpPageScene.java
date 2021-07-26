package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.AppFlowUp;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class AppFlowUpPageScene extends BaseScene {
    /**
     * 页大小 范围为[1,100]
     * Required : true
     **/
    private final int size;
    /**
     * 上次请求最后值 - Object
     * 跟进id,isComplete
     * Required : false
     **/
    private final JSONObject lastValue;

    /**
     * 跟进类型
     * Required : false
     **/
    private final String type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject obj = new JSONObject();
        obj.put("size",size);
        obj.put("last_value",lastValue);
        obj.put("type",type);
        return obj;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/follow-up/page-v4";
    }
}
