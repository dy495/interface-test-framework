package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-活动导出
 */
@Builder
public class ManageExportScene extends BaseScene {
    private final Integer page;
    private final Integer size;
    private final String title;
    private final Integer  status;
    private final String creatorAccount;
    private final String creatorName;
    private final String subjectType;
    private final Integer subjectId;
    private  final String exportType;
    private final JSONArray ids;

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
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/activity/manage/export";
    }
}
