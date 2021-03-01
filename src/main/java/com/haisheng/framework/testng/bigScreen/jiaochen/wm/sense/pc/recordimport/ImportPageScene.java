package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.recordimport;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class ImportPageScene extends BaseScene {
    private final String type;
    private final String user;
    @Builder.Default
    private final Integer size = 10;
    @Builder.Default
    private final Integer page = 1;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("user", user);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/record/import/page";
    }
}
