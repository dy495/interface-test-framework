package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

@Builder
public class ArticlePage extends BaseScene {
    private final String startDate;
    private final String endDate;
    private final String registerStartDate;
    private final String registerEndDate;
    private final String title;
    @Builder.Default
    private final Integer page = 1;
    @Builder.Default
    private final Integer size = 10;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("start_date", startDate);
        object.put("end_date", endDate);
        object.put("register_start_date", registerStartDate);
        object.put("register_end_date", registerEndDate);
        object.put("title", title);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/operation/article/page";
    }
}