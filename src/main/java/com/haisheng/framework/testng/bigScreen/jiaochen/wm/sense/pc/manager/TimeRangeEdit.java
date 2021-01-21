package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 预约时间段编辑
 */
@Builder
public class TimeRangeEdit extends BaseScene {

    private final String type;
    private final String dataType;
    private final String morningReplyStart;
    private final String morningReplyEnd;
    private final String morningStartTime;
    private final String morningEndTime;
    private final Integer morningNum;
    private final Double morningDiscount;
    private final String afternoonReplyStart;
    private final String afternoonReplyEnd;
    private final String afternoonStartTime;
    private final String afternoonEndTime;
    private final Integer afternoonNum;
    private final Double afternoonDiscount;

    @Override
    public JSONObject getJSONObject() {
        JSONObject morning = new JSONObject();
        morning.put("reply_start", morningReplyStart);
        morning.put("reply_end", morningReplyEnd);
        JSONArray morningList = new JSONArray();
        JSONObject morningObject = new JSONObject();
        morningObject.put("start_time", morningStartTime);
        morningObject.put("end_time", morningEndTime);
        morningObject.put("num", morningNum);
        morningObject.put("discount", morningDiscount);
        morningList.add(morningObject);
        morning.put("list", morningList);
        JSONObject afternoon = new JSONObject();
        morning.put("reply_start", afternoonReplyStart);
        morning.put("reply_end", afternoonReplyEnd);
        JSONArray afternoonList = new JSONArray();
        JSONObject afternoonObject = new JSONObject();
        afternoonObject.put("start_time", afternoonStartTime);
        afternoonObject.put("end_time", afternoonEndTime);
        afternoonObject.put("num", afternoonNum);
        afternoonObject.put("discount", afternoonDiscount);
        afternoonList.add(afternoonObject);
        afternoon.put("list", afternoonList);
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("date_type", dataType);
        object.put("morning", morning);
        object.put("afternoon", afternoon);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/manage/appointment/time-range/edit";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
