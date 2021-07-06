package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.mapp;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 门店详情-实时客流-用户画像
 */
@Builder
public class AppRealHourAgeGenderDistributionScene extends BaseScene {
    private final String shopId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/store/m-app/auth/shop/real-hour/age-gender/distribution";
    }
}
