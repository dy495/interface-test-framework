package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.followup;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.4. app 跟进列表回复 v3 (池)(2020-03-09)
 *
 * @author wangmin
 * @date 2021-03-31 13:03:23
 */
@Builder
public class AppReplyV3Scene extends BaseScene {
    /**
     * 描述 跟进id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long followId;

    /**
     * 描述 回复内容
     * 是否必填 true
     * 版本 v3.0
     */
    private final String content;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("follow_id", followId);
        object.put("content", content);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/follow-up/reply-v3";
    }
}