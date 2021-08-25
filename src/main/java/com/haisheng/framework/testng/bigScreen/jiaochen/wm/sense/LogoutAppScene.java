package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import lombok.Builder;

/**
 * 15.1. APP退出登录（谢）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class LogoutAppScene extends BaseScene {

    @Override
    public JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/login-user/logout";
    }


    @Override
    public String getIpPort() {
        return getVisitor().isDaily() ? EnumTestProduct.JC_DAILY_ZH.getIp() : EnumTestProduct.JC_ONLINE_ZH.getIp();
    }
}