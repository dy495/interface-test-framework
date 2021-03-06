package com.haisheng.framework.testng.bigScreen.itemPorsche.common.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 展厅热区分析--变更记录
 *
 * @author wangmin
 */
@Builder
public class CarStyleChangeListScene extends BaseScene {
    @Builder.Default
    private final int page = 1;
    @Builder.Default
    private final int size = 10;
    private final Integer regionId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("region_id", regionId);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/analysis2/region/car-style-change-list";
    }


}
