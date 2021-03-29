package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.10. 售前客户编辑 (杨)v3.0的接口
 *
 * @author wangmin
 * @date 2021-03-24 14:32:26
 */
@Builder
public class PreSaleCustomerEditScene extends BaseScene {
    /**
     * 描述 客户Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long customerId;

    /**
     * 描述 客户手机号
     * 是否必填 true
     * 版本 v3.0
     */
    private final String customerName;

    /**
     * 描述 客户性别
     * 是否必填 true
     * 版本 v3.0
     */
    private final String sex;

    /**
     * 描述 意向车型Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long carModelId;

    /**
     * 描述 意向车系Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long carStyleId;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        object.put("customer_name", customerName);
        object.put("sex", sex);
        object.put("car_model_id", carModelId);
        object.put("car_style_id", carStyleId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/customer-manage/pre-sale-customer/edit";
    }
}