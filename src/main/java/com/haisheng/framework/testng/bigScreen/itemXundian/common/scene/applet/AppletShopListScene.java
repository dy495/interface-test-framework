package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.applet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.5. 小程序门店列表
 *
 * @author wangmin
 * @date 2021-03-30 15:23:58
 */
@Builder
public class AppletShopListScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

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
        object.put("referer", referer);
        object.put("coordinate", coordinate);
        object.put("washingStatus", washingStatus);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/shop-list";
    }
}