package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 创建积分兑换
 */
@Builder
public class CreateExchangeGoodsScene extends BaseScene {
    private final String exchangeGoodsType;
    private final Long goodsId;
    private final String exchangePrice;
    private final String exchangeNum;
    private final Boolean isLimit;
    private final String exchangePeopleNum;
    private final JSONArray specificationList;
    private final String exchangeStartTime;
    private final String exchangeEndTime;
    private final Integer expireType;
    private final String useDays;

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
        return "/car-platform/pc/integral-center/create-exchange-goods";
    }
}
