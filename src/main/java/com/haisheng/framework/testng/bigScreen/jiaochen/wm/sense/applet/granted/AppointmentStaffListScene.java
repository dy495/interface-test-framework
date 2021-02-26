package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * @author wangmin
 * @date 2021/1/29 12:09
 */
@Builder
public class AppointmentStaffListScene extends BaseScene {
    private final Integer shopId;
    private final String type;

    @Override
    public JSONObject getRequest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("shop_id", shopId);
        jsonObject.put("type", type);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/appointment/staff/list";
    }
}
