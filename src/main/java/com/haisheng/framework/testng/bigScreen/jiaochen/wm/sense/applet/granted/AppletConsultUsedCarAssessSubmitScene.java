package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.4. 二手车评估提交 （池） （2021-03-12）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletConsultUsedCarAssessSubmitScene extends BaseScene {
    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long shopId;

    /**
     * 描述 咨询内容
     * 是否必填 true
     * 版本 v3.0
     */
    private final String content;

    /**
     * 描述 联系人
     * 是否必填 true
     * 版本 v3.0
     */
    private final String customerName;

    /**
     * 描述 联系电话
     * 是否必填 true
     * 版本 v3, 0
     */
    private final String customerPhone;

    /**
     * 描述 车辆品牌
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long brandId;

    /**
     * 描述 车型
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long modelId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("content", content);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("brand_id", brandId);
        object.put("model_id", modelId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/consult/used-car-assess-submit";
    }
}