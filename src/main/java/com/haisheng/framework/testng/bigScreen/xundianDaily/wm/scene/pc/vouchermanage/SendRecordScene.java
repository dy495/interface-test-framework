package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.15. 领取记录 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-03-30 14:00:03
 */
@Builder
public class SendRecordScene extends BaseScene {
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
     * 描述 id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 卡券id
     * 是否必填 false
     * 版本 -
     */
    private final Long voucherId;

    /**
     * 描述 领取人
     * 是否必填 false
     * 版本 v2.0
     */
    private final String receiver;

    /**
     * 描述 领取人手机号
     * 是否必填 false
     * 版本 v2.0
     */
    private final String receivePhone;

    /**
     * 描述 优惠券使用状态 IS_USED已核销 NO_USE未核销 EXPIRE已过期 INVALIDED已失效
     * 是否必填 false
     * 版本 v2.0
     */
    private final String useStatus;

    /**
     * 描述 领取开始时间
     * 是否必填 false
     * 版本 v2.0
     */
    private final String startTime;

    /**
     * 描述 领取结束时间
     * 是否必填 false
     * 版本 v2.0
     */
    private final String endTime;

    /**
     * 描述 有效期开始时间
     * 是否必填 false
     * 版本 v2.0
     */
    private final String useStartTime;

    /**
     * 描述 有效期结束时间
     * 是否必填 false
     * 版本 v2.0
     */
    private final String useEndTime;

    /**
     * 描述 客户标签
     * 是否必填 false
     * 版本 v2.0
     */
    private final String customerLabel;

    /**
     * 描述 发出渠道
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer sendChannel;

    /**
     * 描述 主体类型
     * 是否必填 false
     * 版本 -
     */
    private final String subjectType;

    /**
     * 描述 主体id
     * 是否必填 false
     * 版本 -
     */
    private final Long subjectId;

    /**
     * 描述 开始核销时间
     * 是否必填 false
     * 版本 -
     */
    private final String beginVerifyTime;

    /**
     * 描述 结束核销时间
     * 是否必填 false
     * 版本 -
     */
    private final String endVerifyTime;

    /**
     * 描述 核销账号
     * 是否必填 false
     * 版本 -
     */
    private final String verifyAccount;

    /**
     * 描述 开始核销时间
     * 是否必填 false
     * 版本 -
     */
    private final String beginInvalidTime;

    /**
     * 描述 结束核销时间
     * 是否必填 false
     * 版本 -
     */
    private final String endInvalidTime;

    /**
     * 描述 核销账号
     * 是否必填 false
     * 版本 -
     */
    private final String invalidAccount;

    /**
     * 描述 卡券名称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String voucherName;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("id", id);
        object.put("voucher_id", voucherId);
        object.put("receiver", receiver);
        object.put("receive_phone", receivePhone);
        object.put("use_status", useStatus);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("use_start_time", useStartTime);
        object.put("use_end_time", useEndTime);
        object.put("customer_label", customerLabel);
        object.put("send_channel", sendChannel);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        object.put("begin_verify_time", beginVerifyTime);
        object.put("end_verify_time", endVerifyTime);
        object.put("verify_account", verifyAccount);
        object.put("begin_invalid_time", beginInvalidTime);
        object.put("end_invalid_time", endInvalidTime);
        object.put("invalid_account", invalidAccount);
        object.put("voucher_name", voucherName);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/voucher-manage/send-record";
    }
}