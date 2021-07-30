package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 58.26. 核销记录分页 （张小龙）（2020-12-23）
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class VoucherManageVerificationRecordScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 当前页
     * 是否必填 true
     * 版本 -
     */
    private final Integer page;

    /**
     * 描述 当前页的数量
     * 是否必填 true
     * 版本 -
     */
    private final Integer size;

    /**
     * 描述 卡券id
     * 是否必填 false
     * 版本 -
     */
    private final Long voucherId;

    /**
     * 描述 卡券名称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String voucherName;

    /**
     * 描述 发券人
     * 是否必填 false
     * 版本 v1.0
     */
    private final String sender;

    /**
     * 描述 发券人手机号
     * 是否必填 false
     * 版本 -
     */
    private final String salePhone;

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
     * 描述 操作人名称
     * 是否必填 false
     * 版本 -
     */
    private final String verifySaleName;

    /**
     * 描述 操作人凉席方式
     * 是否必填 false
     * 版本 -
     */
    private final String verifySalePhone;

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
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("voucher_id", voucherId);
        object.put("voucher_name", voucherName);
        object.put("sender", sender);
        object.put("sale_phone", salePhone);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("verify_start_time", verifyStartTime);
        object.put("verify_end_time", verifyEndTime);
        object.put("verify_sale_name", verifySaleName);
        object.put("verify_sale_phone", verifySalePhone);
        object.put("verify_code", verifyCode);
        object.put("verify_channel", verifyChannel);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/voucher-manage/verification-record";
    }
}