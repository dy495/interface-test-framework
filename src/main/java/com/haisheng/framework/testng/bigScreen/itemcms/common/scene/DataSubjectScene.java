package com.haisheng.framework.testng.bigScreen.itemcms.common.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class DataSubjectScene extends BaseScene {
    private final String appId;
    private final Long brandId;
    private final Integer subjectType;
    private final String subjectName;
    private final String local;
    private final String manager;
    private final String telephone;
    private final JSONObject region;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("app_id", appId);
        object.put("brand_id", brandId);
        object.put("subject_type", subjectType);
        object.put("subject_name", subjectName);
        object.put("manager", manager);
        object.put("telephone", telephone);
        return object;
    }

    @Override
    public String getPath() {
        return "/admin/data/subject/";
    }
}
