package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 41.22. 规格下拉
 *
 * @author wangmin
 * @date 2021-03-30 14:00:03
 */
@Builder
public class SpecificationsListScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long firstCategory;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("first_category", firstCategory);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-mall/specifications-list";
    }
}