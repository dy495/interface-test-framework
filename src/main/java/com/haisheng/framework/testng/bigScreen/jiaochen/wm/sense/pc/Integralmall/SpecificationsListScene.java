package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/integral-mall/specifications-list的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
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
        return "/jiaochen/pc/integral-mall/specifications-list";
    }
}