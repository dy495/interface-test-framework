package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 积分兑换页
 *
 * @author wangmin
 * @date 2021/3/1 19:06
 */
@Data
public class ExchangePage implements Serializable {
    /**
     * 积分兑换id
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 装态
     */
    @JSONField(name = "status")
    private String status;


    /**
     * 状态名称
     */
    @JSONField(name = "status_name")
    private String statusName;


    /**
     * 商品名称
     */
    @JSONField(name = "goods_name")
    private String goodsName;

    /**
     * 兑换类型
     */
    @JSONField(name = "exchange_type")
    private String exchangeType;

    /**
     * 兑换类型
     */
    @JSONField(name = "exchange_type_name")
    private String exchangeTypeName;

    /**
     * 兑换价格
     */
    @JSONField(name = "exchange_price")
    private Integer exchangePrice;


    /**
     * 已兑换/剩余
     */
    @JSONField(name = "exchanged_and_surplus")
    private String exchangedAndSurplus;

    /**
     * 兑换数量
     */
    @JSONField(name = "exchange_number")
    private Integer exchangeNumber;

    /**
     * 开始时间
     */
    @JSONField(name = "begin_use_time")
    private String beginUseTime;

    /**
     * 结束时间
     */
    @JSONField(name = "end_use_time")
    private String endUseTime;

}
