package com.haisheng.framework.testng.bigScreen.itemPorsche.common.scene.applet;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 小程序我的消息接口
 *
 * @author wangmin
 */
@Builder
public class AppletPorscheAMessageListScene extends BaseScene {
    private final Integer lastValue;
    private final Integer size;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("last_value", lastValue);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/WeChat-applet/porsche/a/message/list";
    }
}
