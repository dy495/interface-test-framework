package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.userange;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 28.1. 
 *
 * @author wangmin
 * @date 2021-07-15 11:24:11
 */
@Builder
public class SubjectListScene extends BaseScene {
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


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/use-range/subject-list";
    }
}