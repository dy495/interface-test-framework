package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.11. 编辑客户（谢）v3.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-03-24 14:44:47
 */
@Builder
public class AppCustomerEditScene extends BaseScene {
    private final String shopId;
    /**
     * 描述 接待id
     * 是否必填 true
     * 版本 v3.0
     */
    private final String id;

    /**
     * 描述 客户名称
     * 是否必填 false
     * 版本 v3.0
     */
    private final String customerName;

    /**
     * 描述 客户手机号
     * 是否必填 false
     * 版本 v3.0
     */
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

    private final String gender;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("id", id);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("car_style", carStyle);
        object.put("car_model", carModel);
        object.put("estimated_buy_time", estimatedBuyTime);
        object.put("gender", gender);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/pre-sales-reception/customer/edit";
    }
}