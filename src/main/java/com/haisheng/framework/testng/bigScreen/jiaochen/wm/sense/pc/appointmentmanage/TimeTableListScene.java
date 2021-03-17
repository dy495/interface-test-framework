package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.appointmentmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 预约管理 -> 预约看板
 */
@Builder
public class TimeTableListScene extends BaseScene {
    private final String appointmentMonth;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("appointment_month", appointmentMonth);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/appointment-manage/time-table/list";
    }

}
