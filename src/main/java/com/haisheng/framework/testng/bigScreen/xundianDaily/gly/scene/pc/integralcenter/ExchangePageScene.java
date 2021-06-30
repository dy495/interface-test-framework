package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 56.1. 积分兑换列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class ExchangePageScene extends BaseScene {
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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
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