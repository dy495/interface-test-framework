package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.organization.rolemanagement;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 32.11. 权限包列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class QueryPermissionMapScene extends BaseScene {
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
     * 描述 上级角色id
     * 是否必填 false
     * 版本 -
     */
    private final Integer superiorRoleId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("superior_role_id", superiorRoleId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/organization/role-management/query-permission-map";
    }
}