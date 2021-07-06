package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.applet;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.6. 小程序名称
 *
 * @author wangmin
 * @date 2021-03-30 15:23:58
 */
@Builder
public class AppletNameScene extends BaseScene {
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
        return "/patrol-applet/name";
    }
}