package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 12.2. 救援申请 （谢） （2020-12-17）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletRescueApplyScene extends BaseScene {
    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long shopId;

    /**
     * 描述 客户当前位置经纬度 [纬度,经度]
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray coordinate;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("coordinate", coordinate);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/rescue/apply";
    }
}