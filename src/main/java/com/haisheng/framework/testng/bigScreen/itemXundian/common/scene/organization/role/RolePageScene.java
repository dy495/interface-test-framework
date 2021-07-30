package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.organization.role;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.12. 角色分页
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
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
     * 描述 角色名称
     * 是否必填 false
     * 版本 -
     */
    private final String roleName;

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
        object.put("role_name", roleName);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/organization/role/page";
    }
}