package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.5. app接待编辑资料
 *
 * @author wangmin
 * @date 2021-03-31 13:03:23
 */
@Builder
public class AppCustomerEditV4Scene extends BaseScene {
    private final String shopId;
    private final String id;
    private final String customerId;
    private final String customerName;
    private final Integer sexId;
    private final String customerPhone;
    private final String intentionCarModelId;
    private final String estimateBuyCarDate;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("id", id);
        object.put("customer_id", customerId);
        object.put("customer_name", customerName);
        object.put("sex_id", sexId);
        object.put("customer_phone", customerPhone);
        object.put("intention_car_model_id", intentionCarModelId);
        object.put("estimate_buy_car_date", estimateBuyCarDate);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/pre-sales-reception/customer/edit-v4";
    }
}