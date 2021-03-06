package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-活动管理列表
 */
@Builder
public class ActivityManagePageScene extends BaseScene {
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;
    private final String title;
    private final Integer approvalStatus;
    private final String creatorAccount;
    private final Integer status;
    private final String creatorName;
    private final String subjectType;
    private final Integer subjectId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("title", title);
        object.put("approval_status", approvalStatus);
        object.put("status", status);
        object.put("creator_account", creatorAccount);
        object.put("creator_name", creatorName);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/activity/manage/page";
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }
}
