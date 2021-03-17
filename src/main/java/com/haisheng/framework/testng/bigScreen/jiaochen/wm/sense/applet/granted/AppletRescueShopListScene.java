package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/applet/granted/rescue/shop/list的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:53:03
 */
@Builder
public class AppletRescueShopListScene extends BaseScene {
    /**
     * 描述 客户当前位置经纬度 [纬度,经度]
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray coordinate;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String washingStatus;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("coordinate", coordinate);
        object.put("washingStatus", washingStatus);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/rescue/shop/list";
    }
}