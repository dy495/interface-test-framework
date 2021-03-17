package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/applet/granted/member-center/car-wash/start的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:53:03
 */
@Builder
public class AppletMemberCenterCarWashStartScene extends BaseScene {
    /**
     * 描述 洗车门店
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long carWashShopId;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("car_wash_shop_id", carWashShopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/member-center/car-wash/start";
    }
}