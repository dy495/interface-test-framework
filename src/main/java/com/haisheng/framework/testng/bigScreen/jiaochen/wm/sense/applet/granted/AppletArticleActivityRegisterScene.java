package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/applet/granted/article/activity/register的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:53:04
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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("register_items", registerItems);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/article/activity/register";
    }
}