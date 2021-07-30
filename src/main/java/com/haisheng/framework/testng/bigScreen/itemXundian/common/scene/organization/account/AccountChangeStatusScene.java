package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.organization.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.7. 账号激活/禁用
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class AccountChangeStatusScene extends BaseScene {
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

    /**
     * 描述 账号状态
     * 是否必填 true
     * 版本 -
     */
    private final Integer status;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("account", account);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/organization/account/change-status";
    }
}