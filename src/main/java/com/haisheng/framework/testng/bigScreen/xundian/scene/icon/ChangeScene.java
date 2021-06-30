package com.haisheng.framework.testng.bigScreen.xundian.scene.icon;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 34.2. 修改icon
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