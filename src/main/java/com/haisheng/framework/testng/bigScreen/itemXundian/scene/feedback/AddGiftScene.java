package com.haisheng.framework.testng.bigScreen.itemXundian.scene.feedback;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 18.2. 新增礼品
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class AddGiftScene extends BaseScene {
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