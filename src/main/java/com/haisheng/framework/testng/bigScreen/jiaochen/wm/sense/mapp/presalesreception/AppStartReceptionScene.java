package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.2. 开始接待车主车辆（谢）v3.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-03-24 14:44:47
 */
@Builder
public class AppStartReceptionScene extends BaseScene {
    /**
     * 描述 接待顾客id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long customerId;
    private final String customerPhone;

    /**
     * 描述 意向车系
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long carStyle;

    /**
     * 描述 意向车型
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long carModel;

    /**
     * 描述 预计购车时间
     * 是否必填 true
     * 版本 v3.0
     */
    private final String estimatedBuyTime;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        object.put("car_style", carStyle);
        object.put("car_model", carModel);
        object.put("estimated_buy_time", estimatedBuyTime);
        object.put("customer_phone", customerPhone);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/pre-sales-reception/start-reception";
    }
}