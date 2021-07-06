package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.role;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 34.3. 新增角色 （杨航）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("authList", authList);
        object.put("description", description);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/role/add";
    }
}