package com.haisheng.framework.testng.bigScreen.crm.wm.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 确定交车消息
 *
 * @author wangmin
 */
@Builder
public class ConfirmCarScene extends BaseScene {
    private final String afterRecordId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("after_record_id", afterRecordId);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/app/after_sale/confirm-car";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
