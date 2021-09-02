package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 创建销售虚拟卡片
 *
 */
@Builder
public class AppPreSalesReceptionCreateV7 extends BaseScene {

    @Override
    protected JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/pre-sales-reception/create-v7";
    }
}