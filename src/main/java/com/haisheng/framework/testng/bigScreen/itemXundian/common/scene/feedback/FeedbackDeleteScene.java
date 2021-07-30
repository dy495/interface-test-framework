package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.feedback;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 36.4. 删除反馈
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class FeedbackDeleteScene extends BaseScene {
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
     * 描述 反馈ID
     * 是否必填 true
     * 版本 -
     */
    private final Integer feedbackId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("feedbackId", feedbackId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/feedback/delete";
    }
}