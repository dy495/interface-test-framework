package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 创建积分兑换
 */
@Builder
public class CreateExchangeGoodsScene extends BaseScene {
    private final String exchangeGoodsType;
    private final Long goodsId;
    private final String exchangePrice;
    private final Boolean isLimit;
    private final String exchangePeopleNum;
    private final JSONArray specificationList;
    private final String exchangeStartTime;
    private final String exchangeEndTime;
    private final String exchangeNum;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("exchange_goods_type", exchangeGoodsType);
        object.put("exchange_price", exchangePrice);
        object.put("goods_id", goodsId);
        object.put("is_limit", isLimit);
        object.put("exchange_people_num", exchangePeopleNum);
        object.put("specification_list", specificationList);
        object.put("exchange_start_time", exchangeStartTime);
        object.put("exchange_end_time", exchangeEndTime);
        object.put("exchange_num", exchangeNum);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/integral-center/create-exchange-goods";
    }
}
