package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.delivery;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.3. 交车海报 v5.0
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppDeliveryPosterScene extends BaseScene {
    /**
     * 描述 唯一id
     * 是否必填 false
     * 版本 -
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/delivery/poster";
    }
}