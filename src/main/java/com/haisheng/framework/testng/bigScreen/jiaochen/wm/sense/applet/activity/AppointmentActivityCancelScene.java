package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;
/**
 * 小程序-我的报名中取消报名
 */
@Builder
public class AppointmentActivityCancelScene extends BaseScene {
    private  final Long id;
    private  final String type;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("id",id);
        object.put("type",type);
        return object;
    }
    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/appointment/activity/cancel";
    }
    @Override
    public String getIpPort() {
        return null;
    }

}