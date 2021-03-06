package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.saveInfo;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class GetBeforeQRCode extends BaseScene {
    /**
     * 接待前传该字段 算法人脸Id 无人脸接待不传 协议不必填
     * Required : false
     **/
    private final String analysisCustomerId;
    /**
     * 接待中传该字段
     * Required : false
     **/
    private final int customerId;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject obj = new JSONObject();
        obj.put("analysis_customer_id",analysisCustomerId);
        obj.put("customer_id",customerId);
        return obj;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/retention/query-qr-code";
    }
}
