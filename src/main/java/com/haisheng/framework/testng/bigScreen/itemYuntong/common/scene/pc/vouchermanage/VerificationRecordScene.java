package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.25. 核销记录分页 （张小龙）v3.0 modify
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class VerificationRecordScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 发卡时间范围查询开始日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String startTime;

    /**
     * 描述 发卡时间范围查询结束日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String endTime;

    /**
     * 描述 核销人姓名
     * 是否必填 false
     * 版本 v3.0
     */
    private final String verifySaleName;

    /**
     * 描述 核销人手机号
     * 是否必填 false
     * 版本 v3.0
     */
    private final String verifySalePhone;

    /**
     * 描述 核销开始日期
     * 是否必填 false
     * 版本 v3.0
     */
    private final String startVerifyTime;

    /**
     * 描述 核销结束日期
     * 是否必填 false
     * 版本 v3.0
     */
    private final String endVerifyTime;

    /**
     * 描述 核销方式
     * 是否必填 false
     * 版本 v3.0
     */
    private final String verifyType;

    /**
     * 描述 核销主体
     * 是否必填 false
     * 版本 v3.0
     */
    private final String verifySubject;

    /**
     * 描述 发券人手机号
     * 是否必填 false
     * 版本 -
     */
    private final String salePhone;

    /**
     * 描述 客户名称
     * 是否必填 false
     * 版本 v2.2
     */
    private final String customerName;

    /**
     * 描述 客户手机号
     * 是否必填 false
     * 版本 v2.2
     */
    private final String customerPhone;

    /**
     * 描述 核销查询开始日期
     * 是否必填 false
     * 版本 v2.2
     */
    private final String verifyStartTime;

    /**
     * 描述 核销查询结束日期
     * 是否必填 false
     * 版本 v2.2
     */
    private final String verifyEndTime;

    /**
     * 描述 核销码
     * 是否必填 false
     * 版本 v2.2
     */
    private final String verifyCode;

    /**
     * 描述 核销渠道
     * 是否必填 false
     * 版本 v2.2
     */
    private final Integer verifyChannel;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("verify_sale_name", verifySaleName);
        object.put("verify_sale_phone", verifySalePhone);
        object.put("start_verify_time", startVerifyTime);
        object.put("end_verify_time", endVerifyTime);
        object.put("verify_type", verifyType);
        object.put("verify_subject", verifySubject);
        object.put("sale_phone", salePhone);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("verify_start_time", verifyStartTime);
        object.put("verify_end_time", verifyEndTime);
        object.put("verify_code", verifyCode);
        object.put("verify_channel", verifyChannel);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/voucher-manage/verification-record";
    }
}