package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.role;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 39.6. 角色详情 （杨航）
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class RoleDetailScene extends BaseScene {
    /**
     * 描述 角色id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/role/detail";
    }
}