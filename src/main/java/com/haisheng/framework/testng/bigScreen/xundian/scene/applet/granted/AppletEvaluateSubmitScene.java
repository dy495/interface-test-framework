package com.haisheng.framework.testng.bigScreen.xundian.scene.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 提交评价
 *
 * @author wangmin
 * @date 2021/1/29 19:01
 */
@Builder
public class AppletEvaluateSubmitScene extends BaseScene {
    private final Integer id;
    private final Integer shopId;
    private final Integer type;
    private final Integer score;
    private final String describe;
    private final List<String> labels;
    private final String suggestion;
    private final Boolean isAnonymous;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shop_id", shopId);
        object.put("type", type);
        object.put("score", score);
        object.put("describe", describe);
        object.put("labels", labels);
        object.put("suggestion", suggestion);
        object.put("isAnonymous", isAnonymous);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/evaluate/submit";
    }
}
