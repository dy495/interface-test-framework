package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.appointmentmanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/appointment-manage/time-table/list的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class TimeTableListScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final String appointmentMonth;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("appointmentMonth", appointmentMonth);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/appointment-manage/time-table/list";
    }
}