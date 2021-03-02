package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 积分商品内容
 *
 * @author wangmin
 * @date 2021/3/1 19:06
 */
@Data
public class ExchangeGoodsDetail implements Serializable {
    @JSONField(name = "exchange_price")
    private Integer exchangePrice;

    @JSONField(name = "is_limit")
    private Boolean isLimit;

    @JSONField(name = "exchange_end_time")
    private String exchangeEndTime;

    @JSONField(name = "exchange_start_time")
    private String exchangeStartTime;

    @JSONField(name = "commodity_price")
    private String commodityPrice;

    @JSONField(name = "goods_id")
    private Long goodsId;

    @JSONField(name = "exchange_num")
    private Integer exchangeNum;

    @JSONField(name = "commodity_pic")
    private String commodityPic;

    @JSONField(name = "commodity_stock")
    private Integer commodityStock;

    @JSONField(name = "id")
    private Long id;

    @JSONField(name = "exchange_people_num")
    private Integer exchangePeopleNum;

    @JSONField(name = "exchange_goods_type")
    private String exchangeGoodsType;

    @JSONField(name = "commodity_name")
    private String commodityName;
}
