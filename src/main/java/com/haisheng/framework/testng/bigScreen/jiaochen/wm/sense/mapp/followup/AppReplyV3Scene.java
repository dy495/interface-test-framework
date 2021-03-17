package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.followup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/m-app/follow-up/reply-v3的接口
 *
 * @author wangmin
 * @date 2021-03-12 18:09:47
 */
@Builder
public class AppReplyV3Scene extends BaseScene {
    /**
     * 描述 跟进id
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long followId;

    /**
     * 描述 内容
     * 是否必填 false
     * 版本 -
     */
    private final String content;


    @Override
    public JSONObject getRequestBody(){
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