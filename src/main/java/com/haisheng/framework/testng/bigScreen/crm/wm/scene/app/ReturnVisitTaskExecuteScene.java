package com.haisheng.framework.testng.bigScreen.crm.wm.scene.app;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;
import org.springframework.util.StringUtils;

/**
 * 回访接口
 */
@Builder
public class ReturnVisitTaskExecuteScene extends BaseScene {
    private final JSONArray returnVisitPic;
    private final String comment;
    private final String failureCause;
    private final String failureCauseRemark;
    private final String otherStoreCarType;
    @Builder.Default
    private final boolean ifSystemRecommend = false;
    private final String nextReturnVisitDate;
    private final String preBuyCarTime;
    private final String returnVisitResult;
    private final String taskId;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("comment", comment);
        if (!StringUtils.isEmpty(failureCause)) {
            object.put("failure_cause", failureCause);
        }
        if (!StringUtils.isEmpty(failureCauseRemark)) {
            object.put("failure_cause_remark", failureCauseRemark);
        }
        if (!StringUtils.isEmpty(otherStoreCarType)) {
            object.put("other_store_car_type", otherStoreCarType);
        }
        object.put("if_system_recommend", ifSystemRecommend);
        object.put("next_return_visit_date", nextReturnVisitDate);
        object.put("pre_buy_car_time", preBuyCarTime);
        object.put("return_visit_pic_list", returnVisitPic);
        object.put("return_visit_result", returnVisitResult);
        object.put("task_id", taskId);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/app/return-visit-task/execute";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}