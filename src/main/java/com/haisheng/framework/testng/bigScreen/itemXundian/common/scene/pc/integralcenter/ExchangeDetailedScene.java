package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 积分明细
 */
@Builder
public class ExchangeDetailedScene extends BaseScene {
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;
    private final String phone;
    private final String exchangeCustomerName;
    private final String exchangeType;
    private final String exchangeStartTime;
    private final String exchangeEndTime;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("phone", phone);
        object.put("exchange_customer_name", exchangeCustomerName);
        object.put("exchange_type", exchangeType);
        object.put("exchange_start_time", exchangeStartTime);
        object.put("exchange_end_time", exchangeEndTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-center/exchange-detailed";
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
