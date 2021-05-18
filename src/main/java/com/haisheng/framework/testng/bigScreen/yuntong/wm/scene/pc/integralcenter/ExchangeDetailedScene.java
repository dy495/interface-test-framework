package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 38.15. 积分兑换明细 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class ExchangeDetailedScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
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
     * 描述 手机号
     * 是否必填 false
     * 版本 v2.2
     */
    private final Long customerId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("id", id);
        object.put("exchange_customer_name", exchangeCustomerName);
        object.put("exchange_type", exchangeType);
        object.put("exchange_start_time", exchangeStartTime);
        object.put("exchange_end_time", exchangeEndTime);
        object.put("phone", phone);
        object.put("customerId", customerId);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/integral-center/exchange-detailed";
    }
}