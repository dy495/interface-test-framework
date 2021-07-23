package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.saveInfo;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class FromCodeGetUserInfo extends BaseScene {
    /**
     * 二维码唯一标识
     * Required : false
     **/
    private final int[] id;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject obj = new JSONObject();

        obj.put("id",id);
        return obj;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/retention/query-user-info";
    }
}
