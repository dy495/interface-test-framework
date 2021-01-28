package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wangmin
 * @date 2021/1/26 14:52
 */
@Data
public class VoucherInvalidPage implements Serializable {
    /**
     * 作废记录id
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 领取时间
     */
    @JSONField(name = "send_time")
    private String sendTime;

    /**
     * 有效期
     */
    @JSONField(name = "invalid_time")
    private String invalidTime;

    /**
     * 领取人
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 领取人手机号
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 作废人
     */
    @JSONField(name = "invalid_name")
    private String invalidName;

    /**
     * 作废人手机号
     */
    @JSONField(name = "invalid_phone")
    private String invalidPhone;

    /**
     * 作废说明
     */
    @JSONField(name = "invalid_description")
    private String invalidDescription;

    /**
     * 卡券码
     */
    @JSONField(name = "voucher_code")
    private String voucherCode;
}
