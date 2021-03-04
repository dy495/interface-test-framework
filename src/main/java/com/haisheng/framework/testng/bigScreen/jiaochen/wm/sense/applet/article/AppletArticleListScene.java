package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.article;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class AppletArticleListScene extends BaseScene {
    private final String size;
    private final String lastValue;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("last_value", lastValue);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/article/list";
    }
}
