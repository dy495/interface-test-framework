package com.haisheng.framework.testng.bigScreen.itemCms.common.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 创建新区域
 *
 * @author wangmin
 * @date 2021/08/03
 */
@Builder
public class DataRegionScene extends BaseScene {
    private final String regionName;
    private final String regionType;
    private final Long layoutId;
    private final Long subjectId;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("region_name", regionName);
        object.put("region_type", regionType);
        object.put("layout_id", layoutId);
        object.put("subject_id", String.valueOf(subjectId));
        return object;
    }

    @Override
    public String getPath() {
        return "/admin/data/region/";
    }
}
