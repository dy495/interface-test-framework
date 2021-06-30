package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.taste;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.8. 搜索评论
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class SearchCommentScene extends BaseScene {
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
     * 描述 当前页
     * 是否必填 true
     * 版本 -
     */
    private final Integer page;

    /**
     * 描述 当前页的数量
     * 是否必填 true
     * 版本 -
     */
    private final Integer size;

    /**
     * 描述 口味ID
     * 是否必填 true
     * 版本 -
     */
    private final Integer tasteId;

    /**
     * 描述 口味名称
     * 是否必填 true
     * 版本 -
     */
    private final String tasteName;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("tasteId", tasteId);
        object.put("taste_name", tasteName);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/taste/search-comment";
    }
}