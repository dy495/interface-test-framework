package com.haisheng.framework.testng.bigScreen.itemXundian.scene.pc.voucher;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

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
    private final JSONArray ids;

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
        return "/shop/pc/voucher/apply/batch-approval";
    }
}