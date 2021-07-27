package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.4. 所属品类列表 (张小龙) (2020-12-28)
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class IntegralMallBelongsCategoryScene extends BaseScene {
    /**
     * 描述 品类级别
     * 是否必填 false
     * 版本 v2.0
     */
    private final String categoryLevel;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("category_level", categoryLevel);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/integral-mall/belongs-category";
    }
}