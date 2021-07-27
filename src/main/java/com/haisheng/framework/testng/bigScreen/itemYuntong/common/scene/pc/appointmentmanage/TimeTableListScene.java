package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.appointmentmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 36.1. 预约看板 （谢）v3.0 （2021-03-22）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class TimeTableListScene extends BaseScene {
    /**
     * 描述 查询月份
     * 是否必填 true
     * 版本 v1.0
     */
    private final String appointmentMonth;

    /**
     * 描述 预约类型 取值见字典表《预约类型》
     * 是否必填 true
     * 版本 v3.0
     */
    private final String type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("appointment_month", appointmentMonth);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/appointment-manage/time-table/list";
    }
}