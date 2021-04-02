package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 5.18. 作废记录 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class VoucherInvalidPageBean implements Serializable {
    /**
     * 描述 当前页
     * 版本 v1.0
     */
    @JSONField(name = "page")
    private Integer page;

    /**
     * 描述 当前页的数量
     * 版本 v1.0
     */
    @JSONField(name = "size")
    private Integer size;

    /**
     * 描述 每页的数量
     * 版本 v1.0
     */
    @JSONField(name = " page_size")
    private Integer pageSize;

    /**
     * 描述 总数
     * 版本 v1.0
     */
    @JSONField(name = "total")
    private Long total;

    /**
     * 描述 总页数
     * 版本 v1.0
     */
    @JSONField(name = "pages")
    private Integer pages;

    /**
     * 描述 详细数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 记录id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 领取时间
     * 版本 v2.0
     */
    @JSONField(name = "send_time")
    private String sendTime;

    /**
     * 描述 有效期
     * 版本 v2.0
     */
    @JSONField(name = "invalid_time")
    private String invalidTime;

    /**
     * 描述 领取人
     * 版本 v2.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 领取人手机号
     * 版本 v2.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 作废人
     * 版本 v2.0
     */
    @JSONField(name = "invalid_name")
    private String invalidName;

    /**
     * 描述 作废人手机号
     * 版本 v2.0
     */
    @JSONField(name = "invalid_phone")
    private String invalidPhone;

    /**
     * 描述 作废说明
     * 版本 v2.0
     */
    @JSONField(name = "invalid_description")
    private String invalidDescription;

    /**
     * 描述 卡券码
     * 版本 v2.0
     */
    @JSONField(name = "voucher_code")
    private String voucherCode;

    /**
     * 描述 卡券名称
     * 版本 v2.2
     */
    @JSONField(name = "voucher_name")
    private String voucherName;

}