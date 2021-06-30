package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 24.1. 卡券表单List
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class VoucherFormVoucherListScene extends BaseScene {
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
        object.put("referer", referer);
        object.put("appId", appId);
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
        return "/shop/pc/voucher-manage/voucher-form/voucher-list";
    }
}