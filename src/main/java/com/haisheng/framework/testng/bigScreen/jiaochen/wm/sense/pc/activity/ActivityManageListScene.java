package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-活动管理列表
 */
@Builder
public class ActivityManageListScene extends BaseScene {
    private final Integer page;
    private final Integer size;
    private final String title;
    private final Integer  status;
    private final String creatorAccount;
    private final String creatorName;
    private final String subjectType;
    private final Integer subjectId;

    @Override
    public JSONObject getJSONObject() {
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

    @Override
    public String getIpPort() {
        return null;
    }
}
