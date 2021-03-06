package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 5.24. 客户名下卡卷列表
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class VoucherListBean implements Serializable {

    @JSONField(name = "voucher_name")
    private String voucherName;

    @JSONField(name = "id")
    private Long id;
}