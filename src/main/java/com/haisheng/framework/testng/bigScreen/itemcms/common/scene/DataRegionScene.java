package com.haisheng.framework.testng.bigScreen.itemcms.common.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class DataRegionScene extends BaseScene {
    private final String regionName;
    private final String regionType;
    private final Long layoutId;
    private final String subjectId;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("region_name", regionName);
        object.put("region_type", regionType);
        object.put("layout_id", layoutId);
        object.put("subject_id", subjectId);
        return object;
    }

    @Override
    public String getPath() {
        return "/admin/data/region/";
    }
}
