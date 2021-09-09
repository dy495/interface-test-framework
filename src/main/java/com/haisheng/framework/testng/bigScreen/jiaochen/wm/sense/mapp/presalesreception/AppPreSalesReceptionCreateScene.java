package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.5. app接待分页（谢）v3.0 （2021-03-29）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:23
 */
@Builder
public class AppPreSalesReceptionCreateScene extends BaseScene {
    private final String customerName;
    private final String customerPhone;
    private final String sexId;
    private final String intentionCarModelId;
    private final String estimateBuyCarTime;

    @Override
    public JSONObject getRequestBody() {
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