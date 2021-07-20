package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.5. app完成接待
 *
 * @author wangmin
 * @date 2021-03-31 13:03:23
 */
@Builder
public class voiceSubmit extends BaseScene {
    private final Long start_time;
    private final Long end_time;
    private final Long reception_id;
    private final String record_name;
    private final String base64;
    private final JSONArray reception_nodes;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("reception_id", reception_id);
        object.put("start_time", start_time);
        object.put("end_time", end_time);
        object.put("record_name", record_name);
        object.put("base64", base64);
        object.put("reception_nodes", reception_nodes);
        return object;
    }


    @Override
    public String getPath() {
        return "/intelligent-control/app/voice-record/submit";
    }
}