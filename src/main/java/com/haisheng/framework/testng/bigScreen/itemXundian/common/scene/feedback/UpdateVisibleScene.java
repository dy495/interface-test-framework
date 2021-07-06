package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.feedback;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 18.3. 是否展示反馈
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class UpdateVisibleScene extends BaseScene {
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

    /**
     * 描述 是否展示
     * 是否必填 true
     * 版本 -
     */
    private final Boolean visible;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("feedbackId", feedbackId);
        object.put("visible", visible);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/feedback/update-visible";
    }
}