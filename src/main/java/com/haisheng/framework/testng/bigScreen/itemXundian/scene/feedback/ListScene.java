package com.haisheng.framework.testng.bigScreen.itemXundian.scene.feedback;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 18.1. 反馈列表查询
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class ListScene extends BaseScene {
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
     * 描述 用户昵称
     * 是否必填 true
     * 版本 -
     */
    private final String userName;

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
        object.put("page", page);
        object.put("size", size);
        object.put("user_name", userName);
        object.put("feedback_type_id", feedbackTypeId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/feedback/list";
    }
}