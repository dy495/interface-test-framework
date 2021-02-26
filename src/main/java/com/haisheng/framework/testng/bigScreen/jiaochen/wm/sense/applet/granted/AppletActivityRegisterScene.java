package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class AppletActivityRegisterScene extends BaseScene {
    private final Long id;
    private final String name;
    private final String phone;
    private final Integer num;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);
        object.put("phone", phone);
        object.put("num", num);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/article/activity/register";
    }
}
