package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.4. 创建潜客 (池) （2021-3-15）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class PreSaleCustomerCreatePotentialCustomerScene extends BaseScene {
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
     * 描述 销售名称
     * 是否必填 false
     * 版本 -
     */
    private final String salesName;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("customer_type", customerType);
        object.put("sex", sex);
        object.put("car_model_id", carModelId);
        object.put("car_style_id", carStyleId);
        object.put("shop_id", shopId);
        object.put("salesId", salesId);
        object.put("salesName", salesName);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/customer-manage/pre-sale-customer/create-potential-customer";
    }
}