package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.saveInfo;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class GetReceivingQRCode extends BaseScene {
    /**
     * 接待前传该字段 算法人脸Id 无人脸接待不传 协议不必填
     * Required : false
     **/
    private final String analysis_customer_id;
    /**
     * 接待中传该字段
     * Required : false
     **/
    private final int customer_id;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject obj = new JSONObject();
        obj.put("analysis_customer_id",analysis_customer_id);
        obj.put("customer_id",customer_id);
        return obj;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/retention/query-reception-qr-code";
    }
}
