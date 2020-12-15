package com.haisheng.framework.testng.bigScreen.crmDaily.wm.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crmDaily.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 发送提车消息
 *
 * @author wangmin
 */
@Builder
public class SendPickUpNewsScene extends BaseScene {
    private final String afterRecordId;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("after_record_id", afterRecordId);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/app/after_sale/send-pick-up-news";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
