package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.role;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 32.7. 编辑角色 （杨航）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class EditScene extends BaseScene {
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

    /**
     * 描述 角色id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer id;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("authList", authList);
        object.put("description", description);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/role/edit";
    }
}