package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.1. 获取评价配置 （谢）v3.0 （2021-03-12）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletEvaluateConfigScene extends BaseScene {
    /**
     * 描述 评价类型 详见字典表《评价类型》 2021-03-12
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer type;

    /**
     * 描述 评价所属门店
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long shopId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/evaluate/config";
    }
}