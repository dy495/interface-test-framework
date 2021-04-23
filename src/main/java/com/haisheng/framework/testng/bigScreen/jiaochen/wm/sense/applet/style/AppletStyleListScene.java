package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.style;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.2. 车系列表
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletStyleListScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final Long brandId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("brandId", brandId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/style/list";
    }
}