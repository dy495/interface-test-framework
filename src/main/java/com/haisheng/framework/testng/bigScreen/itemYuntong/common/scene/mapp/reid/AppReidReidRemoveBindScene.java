package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.reid;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 12.8. 前台：解除绑定（华）v5.0（2021-07-09）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppReidReidRemoveBindScene extends BaseScene {
    /**
     * 描述 客户进入门类型（PRE_SALE/AFTER_SALE 销售门/售后门)
     * 是否必填 true
     * 版本 v5.0
     */
    private final String enterType;

    /**
     * 描述 客户信息ID
     * 是否必填 true
     * 版本 v5.0
     */
    private final Long customerId;

    /**
     * 描述 人脸ID
     * 是否必填 true
     * 版本 v5.0
     */
    private final String reid;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("enter_type", enterType);
        object.put("customer_id", customerId);
        object.put("reid", reid);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/reid/reid-remove-bind";
    }
}