package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 11.12. 获取留资二维码 v3.1 （2021-05-06）<p>2021-08-17 增加接待id
 *
 * @author wangmin
 * @date 2021-08-30 14:35:38
 */
@Builder
public class AppPreSalesReceptionQueryRetentionQrCodeScene extends BaseScene {
    /**
     * 描述 接待id
     * 是否必填 false
     * 版本 7.0
     */
    private final String receptionId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("reception_id", receptionId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/pre-sales-reception/query_retention_qr_code";
    }
}