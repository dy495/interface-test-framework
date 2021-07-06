package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.organization.account;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 32.1. 账号分页
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class PageScene extends BaseScene {
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
     * 描述 账号名称
     * 是否必填 false
     * 版本 -
     */
    private final String name;

    /**
     * 描述 邮箱
     * 是否必填 false
     * 版本 -
     */
    private final String email;

    /**
     * 描述 手机号
     * 是否必填 false
     * 版本 -
     */
    private final String phone;

    /**
     * 描述 角色名称
     * 是否必填 false
     * 版本 -
     */
    private final String roleName;

    /**
     * 描述 门店名称
     * 是否必填 false
     * 版本 -
     */
    private final String shopName;

    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final Integer page;

    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final Integer size;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("name", name);
        object.put("email", email);
        object.put("phone", phone);
        object.put("role_name", roleName);
        object.put("shop_name", shopName);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/organization/account/page";
    }
}