package com.haisheng.framework.testng.bigScreen.itemCms.common.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 创建平面
 *
 * @author wangmin
 * @date 2021-07-29
 */
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
}
