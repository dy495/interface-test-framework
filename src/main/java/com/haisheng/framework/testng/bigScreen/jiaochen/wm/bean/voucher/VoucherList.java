package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.voucher;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 可见卡券列表
 *
 * @author wangmin
 * @date 2021/1/25 16:29
 */
@Data
public class VoucherList implements Serializable {
    @JSONField(name = "voucher_id")
    private Long voucherId;
    @JSONField(name = "voucher_name")
    private String voucherName;
}
