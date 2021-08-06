package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.customermanager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.5. 客户备注（杨）v4.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppCustomerManagerPreCustomerAddRemarkScene extends BaseScene {
    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v4.0
     */
    private final Long customerId;

    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long shopId;

    /**
     * 描述 客户备注信息
     * 是否必填 true
     * 版本 v3.0
     */
    private final String remark;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        object.put("shop_id", shopId);
        object.put("remark", remark);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/customer-manager/pre-customer-add-remark";
    }
}