package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.role;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 32.6. 角色详情 （杨航）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
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
        return "/account-platform/auth/role/detail";
    }
}