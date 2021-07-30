package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.feedback;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 36.2. 新增礼品
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class FeedbackAddGiftScene extends BaseScene {
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
     * 描述 反馈礼品
     * 是否必填 true
     * 版本 -
     */
    private final String feedbackGift;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("feedbackId", feedbackId);
        object.put("feedback_gift", feedbackGift);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/feedback/add-gift";
    }
}