package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 评价列表
 */
@Builder
public class EvaluatePage extends BaseScene {
    private final Integer page;
    private final Integer size;
    private final String plateNumber;
    private final String serviceStaff;
    private final String evaluateType;
    private final Long shopId;
    private final String customerName;
    private final Integer score;
    private final String evaluateTime;
    private final Boolean isRemark;
    private final String phone;
    private final String status;
    private final String createTime;
    private final Boolean isLeaveMsg;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("plate_number", plateNumber);
        object.put("service_staff", serviceStaff);
        object.put("evaluate_type", evaluateType);
        object.put("shop_id", shopId);
        object.put("customer_name", customerName);
        object.put("score", score);
        object.put("evaluate_time", evaluateTime);
        object.put("is_remark", isRemark);
        object.put("phone", phone);
        object.put("status", status);
        object.put("create_time", createTime);
        object.put("is_leave_msg", isLeaveMsg);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/manage/evaluate/page";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
