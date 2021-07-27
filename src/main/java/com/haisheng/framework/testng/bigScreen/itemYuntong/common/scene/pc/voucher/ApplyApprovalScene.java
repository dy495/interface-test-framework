package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.voucher;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 11.3. 卡券审批 （张小龙）
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class ApplyApprovalScene extends BaseScene {
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
        return "/car-platform/pc/voucher/apply/approval";
    }
}