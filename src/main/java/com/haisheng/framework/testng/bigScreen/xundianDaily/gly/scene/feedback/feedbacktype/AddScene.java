package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.feedback.feedbacktype;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 18.5. 新增反馈类型
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class AddScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 反馈类型
     * 是否必填 true
     * 版本 -
     */
    private final String feedbackType;

    /**
     * 描述 反馈类型描述
     * 是否必填 true
     * 版本 -
     */
    private final String feedbackMessage;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("feedback_type", feedbackType);
        object.put("feedback_message", feedbackMessage);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/feedback/feedback-type/add";
    }
}