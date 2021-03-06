package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.9. 商品品牌分页 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class IntegralMallBrandPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 品牌名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String brandName;

    /**
     * 描述 品牌状态
     * 是否必填 false
     * 版本 v2.0
     */
    private final Boolean brandStatus;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("brand_name", brandName);
        object.put("brand_status", brandStatus);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/integral-mall/brand-page";
    }
}