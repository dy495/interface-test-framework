package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/message-manage/search-customer-phone的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class SearchCustomerPhoneScene extends BaseScene {
    /**
     * 描述 客户类型(MESSAGE_CUSTOMER_TYPE_LIST 枚举名称)
     * 是否必填 false
     * 版本 v3.0
     */
    private final String customerType;

    /**
     * 描述 门店列表
     * 是否必填 false
     * 版本 v3.0
     */
    private final JSONArray shopList;

    /**
     * 描述 品牌列表
     * 是否必填 false
     * 版本 v3.0
     */
    private final JSONArray brandList;

    /**
     * 描述 售前客户类型
     * 是否必填 false
     * 版本 v3.0
     */
    private final String preCustomerType;

    /**
     * 描述 意向/购买车系
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long carStyle;

    /**
     * 描述 客户等级
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long customerLevel;

    /**
     * 描述 售后客户类型(MESSAGE_AFTER_CUSTOMER_TYPE_LIST 枚举名称)
     * 是否必填 false
     * 版本 v3.0
     */
    private final String afterCustomerType;

    /**
     * 描述 消费价格
     * 是否必填 false
     * 版本 v3.0
     */
    private final Integer consume;

    /**
     * 描述 售后客户等级
     * 是否必填 false
     * 版本 v3.0
     */
    private final String afterCustomerLevel;

    /**
     * 描述 车龄类型（MESSAGE_DRIVE_YEAR_LIST 车龄类型枚举）
     * 是否必填 false
     * 版本 v3.0
     */
    private final String driveYear;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("customer_type", customerType);
        object.put("shop_list", shopList);
        object.put("brand_list", brandList);
        object.put("pre_customer_type", preCustomerType);
        object.put("car_style", carStyle);
        object.put("customer_level", customerLevel);
        object.put("after_customer_type", afterCustomerType);
        object.put("consume", consume);
        object.put("after_customer_level", afterCustomerLevel);
        object.put("drive_year", driveYear);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/message-manage/search-customer-phone";
    }
}