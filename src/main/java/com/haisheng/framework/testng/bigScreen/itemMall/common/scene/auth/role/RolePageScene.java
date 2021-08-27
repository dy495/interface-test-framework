package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.auth.role;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.1. 角色分页 （杨航）
 *
 * @author wangmin
 * @date 2021-08-26 16:28:32
 */
@Builder
public class RolePageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 是否导出
     * 是否必填 false
     * 版本 v1.0
     */
    private final Boolean isExport;

    /**
     * 描述 角色名称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String name;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("is_export", isExport);
        object.put("name", name);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/role/page";
    }
}