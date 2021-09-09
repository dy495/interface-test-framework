package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.carmodel;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.8. 获取车系车型树（谢志东）v3.0（2021-03-27）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:23
 */
@Builder
public class AppCarModelTreeScene extends BaseScene {
    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long shopId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/car-model/tree";
    }
}