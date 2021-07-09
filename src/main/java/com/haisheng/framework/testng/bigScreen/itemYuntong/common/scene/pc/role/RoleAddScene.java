package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.role;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 34.3. 新增角色 （杨航）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class RoleAddScene extends BaseScene {
    /**
     * 描述 角色名称
     * 是否必填 true
     * 版本 v1.0
     */
    private final String name;

    /**
     * 描述 权限集合
     * 是否必填 true
     * 版本 v1.0
     */
    private final List<Integer> authList;

    /**
     * 描述 角色描述
     * 是否必填 true
     * 版本 v2.0
     */
    private final String description;

    private final Integer parentRoleId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("authList", authList);
        object.put("description", description);
        object.put("parent_role_id", parentRoleId);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/role/add";
    }
}