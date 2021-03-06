package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.receptionmanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 20.11. 卡券列表（张小龙）（2020-12-17）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class ReceptionManagerVoucherListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 卡券id
     * 版本 v1.0
     */
    @JSONField(name = "voucher_id")
    private Long voucherId;

    /**
     * 描述 卡券名
     * 版本 v1.0
     */
    @JSONField(name = "voucher_name")
    private String voucherName;

}