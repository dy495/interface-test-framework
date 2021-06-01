package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 7.20. 卡券表头展示信息查询 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class VoucherInfoBean implements Serializable {
    /**
     * 描述 优惠券id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 优惠券名称
     * 版本 v2.0
     */
    @JSONField(name = "voucher_name")
    private String voucherName;

    /**
     * 描述 共领取
     * 版本 v2.0
     */
    @JSONField(name = "total_send")
    private Integer totalSend;

    /**
     * 描述 共核销
     * 版本 v2.0
     */
    @JSONField(name = "total_verify")
    private Integer totalVerify;

    /**
     * 描述 共作废
     * 版本 v2.0
     */
    @JSONField(name = "total_invalid")
    private Integer totalInvalid;

}