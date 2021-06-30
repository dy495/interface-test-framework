package com.haisheng.framework.testng.bigScreen.itemXundian.scene.organization.role;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 32.15. 角色编辑
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class EditScene extends BaseScene {
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
     * 描述 角色id
     * 是否必填 true
     * 版本 -
     */
    private final Integer roleId;

    /**
     * 描述 上级角色Id
     * 是否必填 true
     * 版本 -
     */
    private final Integer superiorRoleId;

    /**
     * 描述 角色名称
     * 是否必填 true
     * 版本 -
     */
    private final String roleName;

    /**
     * 描述 描述
     * 是否必填 true
     * 版本 -
     */
    private final String description;

    /**
     * 描述 权限包集合
     * 是否必填 true
     * 版本 -
     */
    private final JSONArray moduleIds;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("roleId", roleId);
        object.put("superiorRoleId", superiorRoleId);
        object.put("roleName", roleName);
        object.put("description", description);
        object.put("moduleIds", moduleIds);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/organization/role/edit";
    }
}