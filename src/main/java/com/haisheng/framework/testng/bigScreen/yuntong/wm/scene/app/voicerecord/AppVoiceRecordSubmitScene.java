package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.voicerecord;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.1. 接待语音记录提交（谢）
 *
 * @author wangmin
 * @date 2021-05-07 19:22:48
 */
@Builder
public class AppVoiceRecordSubmitScene extends BaseScene {
    /**
     * 描述 接待id
     * 是否必填 true
     * 版本 v1.0
     */
    private final String receptionId;

    /**
     * 描述 语音压缩文件base64
     * 是否必填 true
     * 版本 v1.0
     */
    private final String base64;

    private final String recordName;

    private final String startTime;

    private final String endTime;

    /**
     * 描述 语音记录
     * 是否必填 true
     * 版本 v1.0
     */
    private final JSONArray receptionNodes;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("reception_id", receptionId);
        object.put("base64", base64);
        object.put("record_name", recordName);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("reception_nodes", receptionNodes);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/app/voice-record/submit";
    }
}