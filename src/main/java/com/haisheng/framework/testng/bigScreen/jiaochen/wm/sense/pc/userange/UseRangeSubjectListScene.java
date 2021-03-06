package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.userange;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class UseRangeSubjectListScene extends BaseScene {
    @Override
    public JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/use-range/subject-list";
    }
}
