package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.activity;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-活动管理列表-更多--gly
 */
@Builder
public class AppletArticleList extends BaseScene {
    private final Integer lastValue;
    private  final Integer size;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("last_value",lastValue);
        object.put("size",size);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/article/page";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
