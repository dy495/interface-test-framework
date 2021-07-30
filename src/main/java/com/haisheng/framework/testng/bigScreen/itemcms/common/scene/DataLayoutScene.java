package com.haisheng.framework.testng.bigScreen.itemcms.common.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class DataLayoutScene extends BaseScene {
    private final String name;
    private final String description;
    private final Long subjectId;
    private final Integer floorId;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("description", description);
        object.put("subject_id", subjectId);
        object.put("floor_id", floorId);
        return object;
    }

    @Override
    public String getPath() {
        return "/admin/data/layout/";
    }


    @Override
    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }
}
