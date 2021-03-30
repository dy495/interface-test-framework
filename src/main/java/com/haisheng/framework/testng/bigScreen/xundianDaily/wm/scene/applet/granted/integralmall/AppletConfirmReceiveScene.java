package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.granted.integralmall;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.12. 小程序 - 确认收货 (张小龙) v2.0car_platform_path: /jiaochen/applet/granted/integral-mall/confirm-receive
 *
 * @author wangmin
 * @date 2021-03-30 15:23:58
 */
@Builder
public class AppletConfirmReceiveScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 订单id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/integral-mall/confirm-receive";
    }
}