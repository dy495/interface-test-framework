package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.loginuser;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class ShopListScene extends BaseScene {
    @Override
    public JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/shop/list";
    }
}
