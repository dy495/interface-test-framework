package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 42.1. 积分兑换列表
 *
 * @author wangmin
 * @date 2021-03-30 14:00:03
 */
@Builder
public class ExchangePageScene extends BaseScene {
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
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 积分兑换品
     * 是否必填 false
     * 版本 v2.0
     */
    private final String exchangeGoods;

    /**
     * 描述 积分兑换类型
     * 是否必填 false
     * 版本 v2.0
     */
    private final String exchangeType;

    /**
     * 描述 状态
     * 是否必填 false
     * 版本 v2.0
     */
    private final String status;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("id", id);
        object.put("exchange_goods", exchangeGoods);
        object.put("exchange_type", exchangeType);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-center/exchange-page";
    }
}