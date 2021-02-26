package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-人群标签组
 */
@Builder
public class LabelGroupSence extends BaseScene {
    private final Integer code;
    private final String message;
    private final String  requestId;
    private final String source;
    private final Integer total;
    private final Long  id;
    private final String desc;

    @Override
    public JSONObject getRequest() {
        JSONObject object = new JSONObject();
        object.put("code", code);
        object.put("message", message);
        object.put("request_id", requestId);
        object.put("source", source);
        object.put("total", total);
        object.put("id",id );
        object.put("desc", desc);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/activity/manage/label-group";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
