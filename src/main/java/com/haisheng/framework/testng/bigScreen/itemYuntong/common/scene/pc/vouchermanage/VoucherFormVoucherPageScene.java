package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.1. 卡券表单 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class VoucherFormVoucherPageScene extends BaseScene {
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
     * 描述 状态
     * 是否必填 false
     * 版本 v2.0
     */
    private final String voucherStatus;

    /**
     * 描述 优惠券类型
     * 是否必填 false
     * 版本 v2.0
     */
    private final String voucherType;

    /**
     * 描述 卡券名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String voucherName;

    /**
     * 描述 归属
     * 是否必填 false
     * 版本 v2.0
     */
    private final String subjectName;

    /**
     * 描述 创建者姓名
     * 是否必填 false
     * 版本 v2.0
     */
    private final String creatorName;

    /**
     * 描述 创建者账号
     * 是否必填 false
     * 版本 v2.0
     */
    private final String creatorAccount;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("voucher_status", voucherStatus);
        object.put("voucher_type", voucherType);
        object.put("voucher_name", voucherName);
        object.put("subject_name", subjectName);
        object.put("creator_name", creatorName);
        object.put("creator_account", creatorAccount);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/voucher-manage/voucher-form/voucher-page";
    }
}