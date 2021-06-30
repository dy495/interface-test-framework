package com.haisheng.framework.testng.bigScreen.itemXundian.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 创建积分兑换
 */
@Builder
public class CreateExchangeGoodsScene extends BaseScene {
    private final String exchangeGoodsType;
    private final Long goodsId;
    private final Long exchangePrice;
    private final Long exchangeNum;
    private final Boolean isLimit;
    private final Integer exchangePeopleNum;
    private final JSONArray specificationList;
    private final String exchangeStartTime;
    private final String exchangeEndTime;
    private final Integer expireType;
    private final Integer useDays;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("exchange_goods_type", exchangeGoodsType);
        object.put("exchange_price", exchangePrice);
        object.put("exchange_num", exchangeNum);
        object.put("goods_id", goodsId);
        object.put("is_limit", isLimit);
        object.put("exchange_people_num", exchangePeopleNum);
        object.put("specification_list", specificationList);
        object.put("exchange_start_time", exchangeStartTime);
        object.put("exchange_end_time", exchangeEndTime);
        object.put("expire_type", expireType);
        object.put("use_days", useDays);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-center/create-exchange-goods";
    }
}
