package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.3. 创建订单 (池) v1.0（2021-3-15）
 *
 * @author wangmin
 * @date 2021-03-31 12:32:56
 */
@Builder
public class PreSaleCustomerCreateCustomerScene extends BaseScene {
    /**
     * 描述 客户名称
     * 是否必填 true
     * 版本 v1.0
     */
    private final String customerName;

    /**
     * 描述 客户手机
     * 是否必填 true
     * 版本 v1.0
     */
    private final String customerPhone;

    /**
     * 描述 客户类型 通用枚举中 SUBJECT_TYPE
     * 是否必填 true
     * 版本 v1.0
     */
    private final String customerType;

    /**
     * 描述 性别
     * 是否必填 true
     * 版本 v1.0
     */
    private final String sex;

    /**
     * 描述 意向车型
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long carModelId;

    /**
     * 描述 意向车系
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long carStyleId;

    /**
     * 描述 所属门店
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long shopId;

    /**
     * 描述 销售顾问id
     * 是否必填 true
     * 版本 v2.0
     */
    private final String salesId;

    /**
     * 描述 购车日期
     * 是否必填 false
     * 版本 v2.0
     */
    private final String purchaseCarDate;

    /**
     * 描述 车辆底盘号
     * 是否必填 false
     * 版本 v2.0
     */
    private final String vehicleChassisCode;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("customer_type", customerType);
        object.put("sex", sex);
        object.put("car_model_id", carModelId);
        object.put("car_style_id", carStyleId);
        object.put("shop_id", shopId);
        object.put("salesId", salesId);
        object.put("purchase_car_date", purchaseCarDate);
        object.put("vehicle_chassis_code", vehicleChassisCode);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/customer-manage/pre-sale-customer/create-customer";
    }
}