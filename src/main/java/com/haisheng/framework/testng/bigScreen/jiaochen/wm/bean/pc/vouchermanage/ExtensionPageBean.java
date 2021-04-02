package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 5.2. 卡券推广 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class ExtensionPageBean implements Serializable {
    /**
     * 描述 卡券推广码
     * 版本 v2.0
     */
    @JSONField(name = "voucher_extension_url")
    private String voucherExtensionUrl;

}