package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.5. 角色详情 （杨航）
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class DetailScene extends BaseScene {
    /**
     * 描述 角色id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer id;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/role/detail";
    }
}