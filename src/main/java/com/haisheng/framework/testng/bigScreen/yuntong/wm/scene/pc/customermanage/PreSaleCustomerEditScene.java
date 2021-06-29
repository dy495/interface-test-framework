package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.11. 售前客户编辑 (杨)v3.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class PreSaleCustomerEditScene extends BaseScene {
    /**
     * 描述 门店Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long shopId;

    /**
     * 描述 客户Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long customerId;

    /**
     * 描述 车主类型
     * 是否必填 true
     * 版本 v3.0
     */
    private final String subjectType;

    /**
     * 描述 客户名称
     * 是否必填 true
     * 版本 v3.0
     */
    private final String customerName;

    /**
     * 描述 客户手机号
     * 是否必填 true
     * 版本 v3.0
     */
    private final String customerPhone;

    /**
     * 描述 客户性别
     * 是否必填 true
     * 版本 v3.0
     */
    private final Integer sex;

    /**
     * 描述 意向车型Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long intentionCarModelId;

    /**
     * 描述 意向车系Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long carStyleId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("customer_id", customerId);
        object.put("subject_type", subjectType);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("sex_id", sex);
        object.put("intention_car_model_id", intentionCarModelId);
        object.put("intention_car_style_id", carStyleId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/customer-manage/pre-sale-customer/edit";
    }
}