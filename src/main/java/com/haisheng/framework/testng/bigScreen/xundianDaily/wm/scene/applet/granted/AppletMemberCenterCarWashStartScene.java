package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 14.5. 开始洗车 (池) v2.0
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("car_wash_shop_id", carWashShopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/member-center/car-wash/start";
    }
}