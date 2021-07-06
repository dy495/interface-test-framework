package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.sensitivewords;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.4. 敏感行为审核（谢）
 *
 * @author wangmin
 * @date 2021-05-31 16:28:11
 */
@Builder
public class AppSensitiveBehaviorApprovalScene extends BaseScene {
    /**
     * 描述 id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;

    /**
     * 描述 审核状态 详见《审核状态》
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer approvalStatus;

    /**
     * 描述 审核备注
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer remark;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("approval_status", approvalStatus);
        object.put("remark", remark);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/sensitive-words/sensitive-behavior/approval";
    }
}