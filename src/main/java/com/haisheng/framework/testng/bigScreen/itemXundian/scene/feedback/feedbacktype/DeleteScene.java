package com.haisheng.framework.testng.bigScreen.itemXundian.scene.feedback.feedbacktype;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 18.6. 删除反馈类型
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class DeleteScene extends BaseScene {
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
     * 描述 反馈类型ID
     * 是否必填 true
     * 版本 -
     */
    private final Integer feedbackTypeId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("feedbackTypeId", feedbackTypeId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/feedback/feedback-type/delete";
    }
}