package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.message;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 18.3. 获取消息详情（张小龙）（2021-03-11）v3.0 modify
 *
 * @author wangmin
 * @date 2021-08-06 16:38:24
 */
@Builder
public class AppMessageDetailScene extends BaseScene {
    /**
     * 描述 消息id
     * 是否必填 true
     * 版本 v2.0
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
        return "/car-platform/m-app/message/detail";
    }
}