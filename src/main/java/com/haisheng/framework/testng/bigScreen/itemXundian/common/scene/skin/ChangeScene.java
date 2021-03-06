package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.skin;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 34.1. 修改皮肤信息
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class ChangeScene extends BaseScene {
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
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final JSONObject skin;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("skin", skin);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/skin/change";
    }
}