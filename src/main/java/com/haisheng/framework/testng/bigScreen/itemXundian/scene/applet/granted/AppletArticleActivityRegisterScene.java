package com.haisheng.framework.testng.bigScreen.itemXundian.scene.applet.granted;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 16.1. 小程序-文章详情-活动报名 （谢）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletArticleActivityRegisterScene extends BaseScene {
    /**
     * 描述 活动id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;

    /**
     * 描述 名字
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONArray registerItems;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("register_items", registerItems);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/article/activity/register";
    }
}