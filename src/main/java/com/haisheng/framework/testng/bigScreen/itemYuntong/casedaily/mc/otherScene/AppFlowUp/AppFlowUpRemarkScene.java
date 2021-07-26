package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.AppFlowUp;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class AppFlowUpRemarkScene extends BaseScene {
    /**
     * 跟进id
     * Required : true
     **/
    private final int followId;
    /**
     * 备注内容
     * Required : true
     **/
    private final String remark;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject obj = new JSONObject();
        obj.put("follow_id",followId);
        obj.put("remark",remark);
        return obj;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/follow-up/remark-v4";
    }
}
