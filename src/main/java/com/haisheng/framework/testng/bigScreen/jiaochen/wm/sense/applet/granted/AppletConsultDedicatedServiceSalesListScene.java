package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 小程序车列表
 *
 * @author wangmin
 * @date 2021/1/29 11:58
 */
@Builder
public class AppletConsultDedicatedServiceSalesListScene extends BaseScene {
    @Override
    public JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/consult/dedicated-service-sales-list";
    }
}
