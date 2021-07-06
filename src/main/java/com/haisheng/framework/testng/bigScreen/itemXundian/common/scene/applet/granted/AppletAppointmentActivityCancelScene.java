package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 16.4. 小程序-我的-报名取消 （谢）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletAppointmentActivityCancelScene extends BaseScene {
    /**
     * 描述 报名id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/appointment/activity/cancel";
    }
}