package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.icon;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.2. 修改icon
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class IconChangeScene extends BaseScene {
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
    private final String projectIcon;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String projectSmallIcon;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("project_icon", projectIcon);
        object.put("project_small_icon", projectSmallIcon);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/icon/change";
    }
}