package com.haisheng.framework.testng.bigScreen.itemXundian.scene.pc.integralmall;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.3. 品类下拉列表 (张小龙) (2020-12-28)
 *
 * @author wangmin
 * @date 2021-03-31 12:29:35
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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("category_level", categoryLevel);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-mall/category-list";
    }
}