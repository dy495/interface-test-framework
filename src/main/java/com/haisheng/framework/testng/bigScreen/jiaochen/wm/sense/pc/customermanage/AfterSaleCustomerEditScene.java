package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.17. 售后客户编辑 (杨)v3.0的接口
 *
 * @author wangmin
 * @date 2021-03-24 14:32:26
 */
@Builder
public class AfterSaleCustomerEditScene extends BaseScene {
    /**
     * 描述 客户Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long customerId;

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
     * 描述 车牌号
     * 是否必填 true
     * 版本 v3.0
     */
    private final String plateNumber;

    /**
     * 描述 送修人手机号
     * 是否必填 true
     * 版本 v3.0
     */
    private final String customerPhone;

    /**
     * 描述 底盘号
     * 是否必填 true
     * 版本 v3.0
     */
    private final String vehicleChassisCode;

    /**
     * 描述 最新里程数
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long newestMiles;

    /**
     * 描述 性别
     * 是否必填 true
     * 版本 v3.0
     */
    private final String sex;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        object.put("car_id", carId);
        object.put("shop_id", shopId);
        object.put("plate_number", plateNumber);
        object.put("customer_phone", customerPhone);
        object.put("vehicle_chassis_code", vehicleChassisCode);
        object.put("newest_miles", newestMiles);
        object.put("sex", sex);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/customer-manage/after-sale-customer/edit";
    }
}