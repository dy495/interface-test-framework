package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.voucher;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 25.3. 卡券审批 （张小龙）
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class AppApplyApprovalScene extends BaseScene {
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


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/voucher/apply/approval";
    }
}