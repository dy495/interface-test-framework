package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.taste;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.7. 新增评论
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class AddCommentScene extends BaseScene {
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
     * 描述 口味ID
     * 是否必填 true
     * 版本 -
     */
    private final Integer tasteId;

    /**
     * 描述 评论人图片
     * 是否必填 true
     * 版本 -
     */
    private final String commentUserImagePath;

    /**
     * 描述 评论人名称
     * 是否必填 true
     * 版本 -
     */
    private final String commentUserName;

    /**
     * 描述 评论信息
     * 是否必填 true
     * 版本 -
     */
    private final String commentMessage;

    /**
     * 描述 评论等级
     * 是否必填 true
     * 版本 -
     */
    private final Integer commentStars;

    /**
     * 描述 是否可见
     * 是否必填 true
     * 版本 -
     */
    private final Boolean visible;

    /**
     * 描述 评论图片
     * 是否必填 true
     * 版本 -
     */
    private final JSONArray commentImagesPath;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("tasteId", tasteId);
        object.put("commentUserImagePath", commentUserImagePath);
        object.put("commentUserName", commentUserName);
        object.put("commentMessage", commentMessage);
        object.put("commentStars", commentStars);
        object.put("visible", visible);
        object.put("commentImagesPath", commentImagesPath);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/taste/add-comment";
    }
}