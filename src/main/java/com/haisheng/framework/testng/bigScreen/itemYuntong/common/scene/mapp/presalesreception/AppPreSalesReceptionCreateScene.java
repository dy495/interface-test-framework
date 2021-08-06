package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.1. 手动创建销售接待
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppPreSalesReceptionCreateScene extends BaseScene {
    /**
     * 描述 客户名称
     * 是否必填 false
     * 版本 v4.0
     */
    private final String customerName;

    /**
     * 描述 客户手机号
     * 是否必填 false
     * 版本 v4.0
     */
    private final String customerPhone;

    /**
     * 描述 客户性别
     * 是否必填 false
     * 版本 v4.0
     */
    private final Integer sexId;

    /**
     * 描述 意向车型Id
     * 是否必填 false
     * 版本 v4.0
     */
    private final Long intentionCarModelId;

    /**
     * 描述 预计购车时间
     * 是否必填 false
     * 版本 v4.0
     */
    private final String estimateBuyCarTime;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("sex_id", sexId);
        object.put("intention_car_model_id", intentionCarModelId);
        object.put("estimate_buy_car_time", estimateBuyCarTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/pre-sales-reception/create";
    }
}