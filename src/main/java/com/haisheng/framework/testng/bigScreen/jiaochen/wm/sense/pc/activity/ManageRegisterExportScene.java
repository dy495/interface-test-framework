package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-活动报名导出
 */
@Builder
public class ManageRegisterExportScene extends BaseScene {
    private final Integer page;
    private final Integer size;
    private final Integer activityId;
    private final Integer  status;
    private  final String exportType;
    private final JSONArray ids;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("activity_id", activityId);
        object.put("status", status);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/activity/manage/register/export";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
