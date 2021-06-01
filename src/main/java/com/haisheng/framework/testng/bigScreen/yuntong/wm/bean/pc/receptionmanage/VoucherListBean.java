package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.receptionmanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 22.11. 卡券列表（张小龙）（2020-12-17）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class VoucherListBean implements Serializable {
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