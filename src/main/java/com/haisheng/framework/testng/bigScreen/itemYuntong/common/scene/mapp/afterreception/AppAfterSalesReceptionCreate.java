package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.afterreception;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 创建销售虚拟卡片
 *
 */
@Builder
public class AppAfterSalesReceptionCreate extends BaseScene {

    @Override
    protected JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/after-customer-manager/after-customer";
    }
}