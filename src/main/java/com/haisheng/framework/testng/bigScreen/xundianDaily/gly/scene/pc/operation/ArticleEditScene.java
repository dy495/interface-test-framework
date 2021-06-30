package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.operation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 14.4. 内容运营 : 编辑文章
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class ArticleEditScene extends BaseScene {
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
     * 描述 文章ID
     * 是否必填 true
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 标题
     * 是否必填 true
     * 版本 v1.0
     */
    private final String title;

    /**
     * 描述 富文本内容
     * 是否必填 true
     * 版本 v1.0
     */
    private final String content;

    /**
     * 描述 作者头像url
     * 是否必填 false
     * 版本 v1.0
     */
    private final String authorAvatar;

    /**
     * 描述 作者昵称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String authorNickname;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("title", title);
        object.put("content", content);
        object.put("author_avatar", authorAvatar);
        object.put("author_nickname", authorNickname);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/operation/article/edit";
    }
}