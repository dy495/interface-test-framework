package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.userange;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class Detail extends BaseScene {
    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("subject_key", "BRAND");
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/use-range/detail";
    }
}
