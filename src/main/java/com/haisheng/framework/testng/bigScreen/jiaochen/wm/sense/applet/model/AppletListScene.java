package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.3. 车型列表
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletListScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final Long brandId;

    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final Long styleId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("brandId", brandId);
        object.put("styleId", styleId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/model/list";
    }
}