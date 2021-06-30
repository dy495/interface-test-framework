package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.5. 小程序门店列表（谢）(2021-01-04)
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletShopListScene extends BaseScene {
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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("coordinate", coordinate);
        object.put("washingStatus", washingStatus);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/shop-list";
    }
}