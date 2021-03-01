package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-招募活动报名列表
 */
@Builder
public class ManageRecruitRegisterInformationScene extends BaseScene {
    private final Integer code;
    private final String message;
    private final String  requestId;
    private final String source;
    private final Integer type;
    private final String  name;
    private final String valueTips;
    private final Boolean isShow;
    private final String is_required;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("code", code);
        object.put("message", message);
        object.put("request_id", requestId);
        object.put("source", source);
        object.put("type", type);
        object.put("name", name);
        object.put("value_tips", valueTips);
        object.put("is_show", isShow);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/activity/manage/recruit/register-information/list";
    }


}
