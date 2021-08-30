package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.afterspecialaudio;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.3. 特殊音频审核（华成裕）
 *
 * @author wangmin
 * @date 2021-08-30 14:39:23
 */
@Builder
public class AfterSpecialAudioApprovalScene extends BaseScene {
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
        return "/intelligent-control/pc/after-special-audio/approval";
    }
}