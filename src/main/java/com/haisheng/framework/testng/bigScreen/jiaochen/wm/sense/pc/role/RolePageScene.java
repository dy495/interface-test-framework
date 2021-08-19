package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.role;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 32.1. 角色分页 （杨航）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class RolePageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    @Builder.Default
    private Integer page = 1;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    @Builder.Default
    private Integer size = 10;

    /**
     * 描述 角色名称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String name;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("name", name);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/role/page";
    }


    @Override
    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }
}