package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 接待管理 -> 点击接待
 */
@Builder
public class ReceptionScene extends BaseScene {
    private final String platNumber;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("plate_number", platNumber);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/reception-manage/reception";
    }
}
