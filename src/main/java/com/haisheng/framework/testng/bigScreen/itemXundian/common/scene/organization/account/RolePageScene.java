package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.organization.account;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 32.2. 角色对应的账号分页
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class RolePageScene extends BaseScene {
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
     * 描述 角色id
     * 是否必填 true
     * 版本 -
     */
    private final Long roleId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("role_id", roleId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/organization/account/role/page";
    }
}