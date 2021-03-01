package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-活动管理列表
 *
 * @author wangmin
 * @date 2021/1/22 16:32
 */
@Builder
public class ManagerPageScene extends BaseScene {
    @Builder.Default
    private final Integer page = 1;
    @Builder.Default
    private final Integer size = 10;
    private final String title;
    private final Integer status;
    private final String creatorAccount;
    private final String creatorName;
    private final String subjectType;
    private final Integer subjectId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("title", title);
        object.put("status", status);
        object.put("creator_account", creatorAccount);
        object.put("creator_name", creatorName);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/activity/manage/page";
    }
}
