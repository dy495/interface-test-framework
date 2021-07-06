package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.shop;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 11.1. 权限门店列表
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class ListScene extends BaseScene {
    /**
     * 描述 区县编码
     * 是否必填 false
     * 版本 v1.0
     */
    private final String districtCode;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("district_code", districtCode);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/shop/list";
    }
}