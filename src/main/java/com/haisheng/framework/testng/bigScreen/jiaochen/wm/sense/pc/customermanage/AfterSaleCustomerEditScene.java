package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.18. 售后客户编辑 (杨)v3.0
 *
 * @author wangmin
 * @date 2021-03-31 12:32:56
 */
@Builder
public class AfterSaleCustomerEditScene extends BaseScene {
    /**
     * 描述 车辆Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long carId;

    /**
     * 描述 门店Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long shopId;

    /**
     * 描述 送修人手机号
     * 是否必填 true
     * 版本 v3.0
     */
    private final String customerPhone;

    /**
     * 描述 送修人名称
     * 是否必填 true
     * 版本 v3.0
     */
    private final String customerName;

    /**
     * 描述 性别
     * 是否必填 true
     * 版本 v3.0
     */
    private final String sex;

    /**
     * 描述 底盘号
     * 是否必填 true
     * 版本 v3.0
     */
    private final String vehicleChassisCode;

    /**
     * 描述 车牌号
     * 是否必填 true
     * 版本 v3.0
     */
    private final String plateNumber;

    /**
     * 描述 最新里程数
     * 是否必填 true
     * 版本 v3.0
     */
    private final Integer newestMiles;

    /**
     * 描述 车系
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long carStyleId;

    /**
     * 描述 车型
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long carModelId;

    /**
     * 描述 客户id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long customerId;

    /**
     * 描述 购车日期
     * 是否必填 false
     * 版本 v3.0
     */
    private final String buyCarTime;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("car_id", carId);
        object.put("shop_id", shopId);
        object.put("customer_phone", customerPhone);
        object.put("customer_name", customerName);
        object.put("sex", sex);
        object.put("vehicle_chassis_code", vehicleChassisCode);
        object.put("plate_number", plateNumber);
        object.put("newest_miles", newestMiles);
        object.put("car_style_id", carStyleId);
        object.put("car_model_id", carModelId);
        object.put("customer_id", customerId);
        object.put("buy_car_time", buyCarTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/customer-manage/after-sale-customer/edit";
    }
}