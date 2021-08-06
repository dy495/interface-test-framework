package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.retention;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.1. 获取接待前留资二维码 V5
 *
 * @author wangmin
 * @date 2021-08-06 16:38:24
 */
@Builder
public class AppRetentionQueryQrCodeScene extends BaseScene {
    /**
     * 描述 接待前传该字段 算法人脸Id 无人脸接待不传 协议不必填
     * 是否必填 false
     * 版本 v5.0
     */
    private final JSONArray analysisCustomerIds;

    /**
     * 描述 接待中传该字段
     * 是否必填 false
     * 版本 v5.0
     */
    private final Long customerId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("analysis_customer_ids", analysisCustomerIds);
        object.put("customer_id", customerId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/retention/query-qr-code";
    }
}