package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.afterreception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.4. 编辑客户资料（刘）v7.0
 *
 * @author wangmin
 * @date 2021-08-30 14:35:38
 */
@Builder
public class AppCustomerEditScene extends BaseScene {
    /**
     * 描述 接待id
     * 是否必填 true
     * 版本 v7.0
     */
    private final Long id;

    /**
     * 描述 门店Id
     * 是否必填 true
     * 版本 v7.0
     */
    private final Long shopId;

    /**
     * 描述 顾客Id
     * 是否必填 true
     * 版本 v7.0
     */
    private final Long customerId;

    /**
     * 描述 顾客名称
     * 是否必填 true
     * 版本 v7.0
     */
    private final String customerName;

    /**
     * 描述 性别
     * 是否必填 true
     * 版本 v7.0
     */
    private final Integer sexId;

    /**
     * 描述 顾客手机号
     * 是否必填 true
     * 版本 v7.0
     */
    private final String customerPhone;

    /**
     * 描述 接待车牌号
     * 是否必填 true
     * 版本 v7.0
     */
    private final String plateNumber;

    /**
     * 描述 服务内容 （机修/理赔）
     * 是否必填 true
     * 版本 v7.0
     */
    private final String afterSalesType;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shop_id", shopId);
        object.put("customer_id", customerId);
        object.put("customer_name", customerName);
        object.put("sex_id", sexId);
        object.put("customer_phone", customerPhone);
        object.put("plate_number", plateNumber);
        object.put("after_sales_type", afterSalesType);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/after-reception/customer/edit";
    }
}