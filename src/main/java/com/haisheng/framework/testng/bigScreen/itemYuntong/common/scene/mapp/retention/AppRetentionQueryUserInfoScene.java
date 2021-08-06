package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.retention;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.3. 获取用户留资信息 V5
 *
 * @author wangmin
 * @date 2021-08-06 16:38:24
 */
@Builder
public class AppRetentionQueryUserInfoScene extends BaseScene {
    /**
     * 描述 二维码唯一标识
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONArray id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/retention/query-user-info";
    }
}