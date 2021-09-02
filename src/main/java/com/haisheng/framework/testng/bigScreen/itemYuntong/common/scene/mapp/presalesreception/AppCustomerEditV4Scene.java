package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 9.15. 编辑客户（杨）v4.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppCustomerEditV4Scene extends BaseScene {
    /**
     * 描述 接待id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long id;

    /**
     * 描述 门店Id
     * 是否必填 true
     * 版本 v4.0
     */
    private final Long shopId;

    /**
     * 描述 顾客Id
     * 是否必填 true
     * 版本 v4.0
     */
    private final Long customerId;

    /**
     * 描述 顾客名称
     * 是否必填 true
     * 版本 v4.0
     */
    private final String customerName;

    /**
     * 描述 性别
     * 是否必填 true
     * 版本 v4.0
     */
    private final Integer sexId;

    /**
     * 描述 顾客手机号
     * 是否必填 true
     * 版本 v4.0
     */
    private final String customerPhone;

    /**
     * 描述 意向车型Id
     * 是否必填 false
     * 版本 v4.0
     */
    private final Long intentionCarModelId;

    /**
     * 描述 预计购车日期
     * 是否必填 false
     * 版本 v4.0
     */
    private final String estimateBuyCarDate;
    private final String customerSource;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shop_id", shopId);
        object.put("customer_id", customerId);
        object.put("customer_name", customerName);
        object.put("sex_id", sexId);
        object.put("customer_phone", customerPhone);
        object.put("intention_car_model_id", intentionCarModelId);
        object.put("estimate_buy_car_date", estimateBuyCarDate);
        object.put("customer_source",customerSource);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/pre-sales-reception/customer/edit-v4";
    }
}