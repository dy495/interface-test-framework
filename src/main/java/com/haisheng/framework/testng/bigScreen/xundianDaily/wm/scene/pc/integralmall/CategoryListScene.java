package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 41.3. 品类下拉列表
 *
 * @author wangmin
 * @date 2021-03-30 14:00:03
 */
@Builder
public class CategoryListScene extends BaseScene {
    /**
     * 描述 品类级别
     * 是否必填 false
     * 版本 v2.0
     */
    private final String categoryLevel;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("category_level", categoryLevel);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-mall/category-list";
    }
}