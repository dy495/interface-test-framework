package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.member.level;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 43.1. 会员等级列表 ljq
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class LevelListScene extends BaseScene {
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
     * 描述 等级名称
     * 是否必填 false
     * 版本 -
     */
    private final String levelName;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("level_name", levelName);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/member/level/list";
    }
}