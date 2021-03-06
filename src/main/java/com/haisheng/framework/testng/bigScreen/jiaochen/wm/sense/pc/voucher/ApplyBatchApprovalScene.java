package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 8.4. 卡券批量审批 （张小龙） v3.0
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class ApplyBatchApprovalScene extends BaseScene {
    /**
     * 描述 批量审批记录id
     * 是否必填 true
     * 版本 v3.0
     */
    private final List<Long> ids;

    /**
     * 描述 状态 1 通过，2 拒绝
     * 是否必填 true
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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("ids", ids);
        object.put("status", status);
        object.put("reason", reason);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/voucher/apply/batch-approval";
    }
}