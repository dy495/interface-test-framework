package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 积分订单页
 *
 * @author wangmin
 * @date 2021/3/1 19:06
 */
@Data
public class ExchangeStockPage implements Serializable {

    @JSONField(name = "sale_id")
    private String saleId;

    @JSONField(name = "sale_phone")
    private String salePhone;

    @JSONField(name = "change_reason")
    private String changeReason;

    @JSONField(name = "id")
    private Long id;

    @JSONField(name = "sale_name")
    private String saleName;

    @JSONField(name = "exchange_type")
    private String exchangeType;

    @JSONField(name = "operate_time")
    private String operateTime;

    @JSONField(name = "stock_detail")
    private Integer stockDetail;
}
