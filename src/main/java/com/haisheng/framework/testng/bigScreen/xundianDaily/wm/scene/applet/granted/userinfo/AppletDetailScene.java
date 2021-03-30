package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.granted.userinfo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.1. 获取个人信息详情（谢）（2021-01-04）car_platform_path: /jiaochen/applet/granted/user-info/detail
 *
 * @author wangmin
 * @date 2021-03-30 15:23:58
 */
@Builder
public class AppletDetailScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/user-info/detail";
    }
}