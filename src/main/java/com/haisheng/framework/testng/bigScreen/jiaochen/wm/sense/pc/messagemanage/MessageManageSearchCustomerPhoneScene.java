package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 14.5. 消息管理客户查询（张小龙）（2021-03-09）v3.0
 *
 * @author wangmin
 * @date 2021-08-30 14:26:54
 */
@Builder
public class MessageManageSearchCustomerPhoneScene extends BaseScene {
    /**
     * 描述 客户类型(MESSAGE_CUSTOMER_TYPE_LIST 枚举名称)
     * 是否必填 true
     * 版本 v3.0
     */
    private final String customerType;

    /**
     * 描述 门店列表
     * 是否必填 false
     * 版本 v3.0
     */
    private final JSONArray shopIds;

    /**
     * 描述 品牌列表
     * 是否必填 false
     * 版本 v3.0
     */
    private final JSONArray brandIds;

    /**
     * 描述 售前客户类型(PRE_CUSTOMER_TYPE_LIST 枚举名称)
     * 是否必填 false
     * 版本 v3.0
     */
    private final JSONArray preCustomerType;

    /**
     * 描述 售后客户类型(MESSAGE_AFTER_CUSTOMER_TYPE_LIST 枚举名称)
     * 是否必填 false
     * 版本 v3.0
     */
    private final JSONArray afterCustomerType;

    /**
     * 描述 消费价格(CONSUME_TYPE_LIST 枚举名称)
     * 是否必填 false
     * 版本 v3.0
     */
    private final JSONArray consumeType;

    /**
     * 描述 消费次数类型(CONSUME_TIMES_LIST 枚举名称)
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray consumeTimeType;

    /**
     * 描述 车龄类型（MESSAGE_DRIVE_YEAR_LIST 车龄类型枚举）
     * 是否必填 false
     * 版本 v3.0
     */
    private final JSONArray driveYear;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_type", customerType);
        object.put("shop_ids", shopIds);
        object.put("brand_ids", brandIds);
        object.put("pre_customer_type", preCustomerType);
        object.put("after_customer_type", afterCustomerType);
        object.put("consume_type", consumeType);
        object.put("consume_time_type", consumeTimeType);
        object.put("drive_year", driveYear);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/message-manage/search-customer-phone";
    }
}