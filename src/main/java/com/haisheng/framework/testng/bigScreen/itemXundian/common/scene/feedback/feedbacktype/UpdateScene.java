package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.feedback.feedbacktype;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 18.7. 编辑反馈类型
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class UpdateScene extends BaseScene {
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
    private final Integer feedbackTypeId;

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
        object.put("feedbackTypeId", feedbackTypeId);
        object.put("feedbackType", feedbackType);
        object.put("feedbackMessage", feedbackMessage);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/feedback/feedback-type/update";
    }
}