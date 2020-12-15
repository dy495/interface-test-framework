package com.haisheng.framework.testng.bigScreen.jiaochenDaily.wm.sense.pc.receptionmanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crmDaily.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 接待管理 -> 点击接待
 */
@Builder
public class Reception extends BaseScene {
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
