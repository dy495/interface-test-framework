package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/applet/granted/member-center/car-wash/shop-list的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:53:03
 */
@Builder
public class AppletMemberCenterCarWashShopListScene extends BaseScene {
    /**
     * 描述 客户当前位置经纬度
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray coordinate;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("coordinate", coordinate);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/member-center/car-wash/shop-list";
    }
}