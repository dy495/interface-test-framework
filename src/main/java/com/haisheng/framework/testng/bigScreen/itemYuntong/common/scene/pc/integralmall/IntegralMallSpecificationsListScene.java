package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.22. 规格下拉 (张小龙 2020-01-20) v2.0
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class IntegralMallSpecificationsListScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long firstCategory;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("first_category", firstCategory);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/integral-mall/specifications-list";
    }
}