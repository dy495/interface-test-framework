package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.14. 修改商品品牌 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class IntegralMallEditBrandScene extends BaseScene {
    /**
     * 描述 品牌id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 品牌名称
     * 是否必填 true
     * 版本 v2.0
     */
    private final String brandName;

    /**
     * 描述 品牌说明
     * 是否必填 true
     * 版本 v2.0
     */
    private final String brandDescription;

    /**
     * 描述 品牌logo
     * 是否必填 false
     * 版本 v2.0
     */
    private final String brandPic;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("brand_name", brandName);
        object.put("brand_description", brandDescription);
        object.put("brand_pic", brandPic);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/integral-mall/edit-brand";
    }
}