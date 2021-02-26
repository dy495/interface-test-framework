package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.userange;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class SubjectList extends BaseScene {
    @Override
    public JSONObject getRequest() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/use-range/subject-list";
    }
}
