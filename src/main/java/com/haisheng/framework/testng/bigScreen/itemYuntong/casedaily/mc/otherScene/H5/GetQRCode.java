package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.H5;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class GetQRCode extends BaseScene {
    /**
     * 请求源域名
     * Required : false
     **/
    private final String apiDomain;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject obj = new JSONObject();
        obj.put("apiDomain",apiDomain);
        return obj;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/pre-sales-reception/query_retention_qr_code";
    }
}
