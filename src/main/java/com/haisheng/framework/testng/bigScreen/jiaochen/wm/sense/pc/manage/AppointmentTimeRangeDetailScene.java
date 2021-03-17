package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/manage/appointment/time-range/detail的接口
 *
 * @author wangmin
 * @date 2021-03-15 10:12:39
 */
@Builder
public class AppointmentTimeRangeDetailScene extends BaseScene {
    /**
     * 描述 预约类型 REPAIR：维修，MAINTAIN：保养
     * 是否必填 true
     * 版本 v2.0
     */
    private final String type;

    /**
     * 描述 日期类型 WEEKDAY：工作提，WEEKEND：周末
     * 是否必填 true
     * 版本 v1.0
     */
    private final String dateType;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("date_type", dateType);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/manage/appointment/time-range/detail";
    }
}