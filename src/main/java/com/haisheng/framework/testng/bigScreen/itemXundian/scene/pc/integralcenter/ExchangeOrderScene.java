package com.haisheng.framework.testng.bigScreen.itemXundian.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 积分订单
 */
@Builder
public class ExchangeOrderScene extends BaseScene {
    private final String orderStatus;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;
    private final String orderId;
    private final String member;
    private final String goodsName;
    private final String startTime;
    private final String endTime;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("order_status", orderStatus);
        object.put("page", page);
        object.put("size", size);
        object.put("order_id", orderId);
        object.put("member", member);
        object.put("goods_name", goodsName);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-center/exchange-order";
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }

}
