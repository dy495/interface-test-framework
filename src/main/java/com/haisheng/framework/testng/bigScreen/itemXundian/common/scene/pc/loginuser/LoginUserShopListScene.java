package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.loginuser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.5. 登陆用户门店
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class LoginUserShopListScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String map;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("map", map);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/login-user/shop-list";
    }
}