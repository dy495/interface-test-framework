package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 积分明细
 *
 * @author wangmin
 * @date 2021/1/27 20:02
 */
@Data
public class ExchangeDetailed implements Serializable {

    /**
     * 详情
     */
    @JSONField(name = "change_reason")
    private String changeReason;

    /**
     * 客户
     */
    @JSONField(name = "exchange_customer_name")
    private String exchangeCustomerName;

    /**
     * 变更类型
     */
    @JSONField(name = "exchange_type")
    private String exchangeType;


    /**
     * 电话号
     */
    @JSONField(name = "phone")
    private String phone;

    /**
     * 明细
     */
    @JSONField(name = "stock_detail")
    private Long stockDetail;
}
