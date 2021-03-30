package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.granted.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.2. 小程序 - 积分规则 (张小龙) v2.0car_platform_path: /jiaochen/applet/granted/integral-mall/integral-rule
 *
 * @author wangmin
 * @date 2021-03-30 15:23:58
 */
@Builder
public class AppletIntegralRuleScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/integral-mall/integral-rule";
    }
}