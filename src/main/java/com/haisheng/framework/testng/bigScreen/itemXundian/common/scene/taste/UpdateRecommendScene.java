package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.taste;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.6. 编辑口味是否加入排行
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class UpdateRecommendScene extends BaseScene {
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
    private final Integer id;

    /**
     * 描述 是否推荐
     * 是否必填 true
     * 版本 -
     */
    private final Boolean recommend;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("recommend", recommend);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/taste/update-recommend";
    }
}