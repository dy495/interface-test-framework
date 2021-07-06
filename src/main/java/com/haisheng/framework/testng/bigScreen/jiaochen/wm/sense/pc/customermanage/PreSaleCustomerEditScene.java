package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.11. 售前客户编辑 (杨)v3.0
 *
 * @author wangmin
 * @date 2021-03-31 12:32:56
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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shopId", shopId);
        object.put("customerId", customerId);
        object.put("subjectType", subjectType);
        object.put("customerName", customerName);
        object.put("customerPhone", customerPhone);
        object.put("sex", sex);
        object.put("carModelId", carModelId);
        object.put("carStyleId", carStyleId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/customer-manage/pre-sale-customer/edit";
    }
}