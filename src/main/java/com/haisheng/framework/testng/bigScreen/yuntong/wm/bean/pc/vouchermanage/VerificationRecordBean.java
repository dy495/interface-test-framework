package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 7.25. 核销记录分页 （张小龙）v3.0 modify
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class VerificationRecordBean implements Serializable {
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
    private Integer  pageSize;

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
     * 描述 唯一id
     * 版本 v3.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 核销主体
     * 版本 v3.0
     */
    @JSONField(name = "subject_name")
    private String subjectName;

    /**
     * 描述 核销时间
     * 版本 v3.0
     */
    @JSONField(name = "verification_time")
    private String verificationTime;

    /**
     * 描述 领券时间
     * 版本 v3.0
     */
    @JSONField(name = "send_time")
    private String sendTime;

    /**
     * 描述 客户名称
     * 版本 v3.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 客户手机号
     * 版本 v3.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 核销人员名称
     * 版本 v3.0
     */
    @JSONField(name = "verification_sale_name")
    private String verificationSaleName;

    /**
     * 描述 核销账户
     * 版本 v3.0
     */
    @JSONField(name = "verification_account")
    private String verificationAccount;

    /**
     * 描述 核销方式
     * 版本 v3.0
     */
    @JSONField(name = "verification_channel")
    private String verificationChannel;

    /**
     * 描述 核销方式名称
     * 版本 v3.0
     */
    @JSONField(name = "verification_channel_name")
    private String verificationChannelName;

    /**
     * 描述 卡券码
     * 版本 v3.0
     */
    @JSONField(name = "voucher_code")
    private String voucherCode;

    /**
     * 描述 卡券名称
     * 版本 -
     */
    @JSONField(name = "voucher_name")
    private String voucherName;

    /**
     * 描述 核销人员
     * 版本 -
     */
    @JSONField(name = "verification_person")
    private String verificationPerson;

    /**
     * 描述 核销码
     * 版本 -
     */
    @JSONField(name = "verification_code")
    private String verificationCode;

    /**
     * 描述 微信open_id
     * 版本 -
     */
    @JSONField(name = "open_id")
    private String openId;

}