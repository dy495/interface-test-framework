package com.haisheng.framework.testng.bigScreen.itemXundian.scene.organization.account;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 32.6. 账号删除
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class DeleteScene extends BaseScene {
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
     * 描述 账号id
     * 是否必填 true
     * 版本 -
     */
    private final String account;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("account", account);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/organization/account/delete";
    }
}