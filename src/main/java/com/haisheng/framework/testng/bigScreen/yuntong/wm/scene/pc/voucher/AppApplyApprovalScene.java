package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.voucher;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.3. 卡券审批 （张小龙）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class AppApplyApprovalScene extends BaseScene {
    /**
     * 描述 卡券审批申请记录id
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long id;

    /**
     * 描述 状态 1 通过，2 拒绝
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer status;

    /**
     * 描述 审批未通过原因
     * 是否必填 false
     * 版本 v3.0
     */
    private final String reason;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("status", status);
        object.put("reason", reason);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/voucher/apply/approval";
    }
}