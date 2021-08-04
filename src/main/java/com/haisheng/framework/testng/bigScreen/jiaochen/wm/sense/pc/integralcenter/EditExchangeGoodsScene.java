package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 修改积分兑换
 */
@Builder
public class EditExchangeGoodsScene extends BaseScene {
    private final String exchangeGoodsType;
    private final Long goodsId;
    private final Long exchangePrice;
    private final Boolean isLimit;
    private final Integer exchangePeopleNum;
    private final String exchangeStartTime;
    private final String exchangeEndTime;
    private final Long id;
    private final Long exchangeNum;
    private final Integer expireType;
    private final Integer useDays;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("exchange_goods_type", exchangeGoodsType);
        object.put("goods_id", goodsId);
        object.put("exchange_price", exchangePrice);
        object.put("is_limit", isLimit);
        object.put("exchange_people_num", exchangePeopleNum);
        object.put("exchange_start_time", exchangeStartTime);
        object.put("exchange_end_time", exchangeEndTime);
        object.put("id", id);
        object.put("exchange_num", exchangeNum);
        object.put("expire_type", expireType);
        object.put("use_days", useDays);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/integral-center/edit-exchange-goods";
    }

}
