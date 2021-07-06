package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.brand;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.1. 品牌列表
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletBrandListScene extends BaseScene {
    /**
     * 描述 门店id
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long shopId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/brand/list";
    }
}