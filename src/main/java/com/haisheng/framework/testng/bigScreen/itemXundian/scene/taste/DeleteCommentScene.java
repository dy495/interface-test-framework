package com.haisheng.framework.testng.bigScreen.itemXundian.scene.taste;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.10. 删除评论
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class DeleteCommentScene extends BaseScene {
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
    private final Integer commentId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("commentId", commentId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/taste/delete-comment";
    }
}