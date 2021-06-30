package com.haisheng.framework.testng.bigScreen.itemXundian.scene.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 小程序消息详情
 *
 * @author wangmin
 * @date 2021/1/28 19:20
 */
@Builder
public class AppletMessageDetailScene extends BaseScene {
    private final Long id;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;

    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/message/detail";
    }
}
