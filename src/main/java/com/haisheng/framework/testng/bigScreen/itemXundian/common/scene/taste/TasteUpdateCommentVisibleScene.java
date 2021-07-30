package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.taste;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 53.12. 编辑评论是否可见
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class TasteUpdateCommentVisibleScene extends BaseScene {
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
     * 描述 评论ID
     * 是否必填 true
     * 版本 -
     */
    private final Integer id;

    /**
     * 描述 是否可见
     * 是否必填 true
     * 版本 -
     */
    private final Boolean visible;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("visible", visible);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/taste/update-comment-visible";
    }
}