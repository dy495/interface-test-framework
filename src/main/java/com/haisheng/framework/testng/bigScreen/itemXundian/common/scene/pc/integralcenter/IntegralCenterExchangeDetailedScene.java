package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 37.15. 积分兑换明细
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class IntegralCenterExchangeDetailedScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 当前页
     * 是否必填 true
     * 版本 -
     */
    private final Integer page;

    /**
     * 描述 当前页的数量
     * 是否必填 true
     * 版本 -
     */
    private final Integer size;

    /**
     * 描述 兑换商品唯一id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 客户名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String exchangeCustomerName;

    /**
     * 描述 兑换类型（ADD:增加 MINUS:减少）
     * 是否必填 false
     * 版本 v2.0
     */
    private final String exchangeType;

    /**
     * 描述 兑换开始时间
     * 是否必填 false
     * 版本 v2.0
     */
    private final String exchangeStartTime;

    /**
     * 描述 兑换结束时间
     * 是否必填 false
     * 版本 v2.0
     */
    private final String exchangeEndTime;

    /**
     * 描述 手机号
     * 是否必填 false
     * 版本 v2.2
     */
    private final String phone;

    /**
     * 描述 用户id
     * 是否必填 false
     * 版本 v2.2
     */
    private final Long customerId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("id", id);
        object.put("exchange_customer_name", exchangeCustomerName);
        object.put("exchange_type", exchangeType);
        object.put("exchange_start_time", exchangeStartTime);
        object.put("exchange_end_time", exchangeEndTime);
        object.put("phone", phone);
        object.put("customer_id", customerId);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-center/exchange-detailed";
    }
}