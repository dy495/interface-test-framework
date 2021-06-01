package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.integralcenter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 37.10. 积分兑换库存 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class ExchangeGoodsStockBean implements Serializable {
    /**
     * 描述 积分兑换品id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 商品图
     * 版本 v2.0
     */
    @JSONField(name = "goods_pic")
    private String goodsPic;

    /**
     * 描述 商品名称
     * 版本 v2.0
     */
    @JSONField(name = "goods_name")
    private String goodsName;

    /**
     * 描述 商品价格
     * 版本 v2.0
     */
    @JSONField(name = "goods_price")
    private String goodsPrice;

    /**
     * 描述 商品库存
     * 版本 v2.0
     */
    @JSONField(name = "goods_stock")
    private Long goodsStock;

    /**
     * 描述 累计兑换
     * 版本 v2.0
     */
    @JSONField(name = "exchange_number")
    private Long exchangeNumber;

}