package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.2. 新增角色 （杨航）
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class AddScene extends BaseScene {
    /**
     * 描述 角色名称
     * 是否必填 true
     * 版本 v1.0
     */
    private final String name;

    /**
     * 描述 上级角色Id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer parentRoleId;

    /**
     * 描述 权限集合
     * 是否必填 true
     * 版本 v1.0
     */
    private final JSONArray authList;

    /**
     * 描述 角色描述
     * 是否必填 true
     * 版本 v2.0
     */
    private final String description;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("parent_role_id", parentRoleId);
        object.put("auth_list", authList);
        object.put("description", description);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/role/add";
    }
}