package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.carmodel;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.2. 车型
 *
 * @author wangmin
 * @date 2021-05-07 19:22:48
 */
@Builder
public class AppCarModelTreeScene extends BaseScene {

    private final String shopId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/car-model/tree";
    }
}