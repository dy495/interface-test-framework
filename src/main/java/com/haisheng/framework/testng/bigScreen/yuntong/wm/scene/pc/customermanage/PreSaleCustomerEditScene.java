package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.customermanage;

import com.alibaba.fastjson.JSONArray;
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
        object.put("shopId", shopId);
        object.put("customerId", customerId);
        object.put("subjectType", subjectType);
        object.put("customerName", customerName);
        object.put("customerPhone", customerPhone);
        object.put("sex", sex);
        object.put("intention_car_model_id", intentionCarModelId);
        object.put("carStyleId", carStyleId);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/customer-manage/pre-sale-customer/edit";
    }
}