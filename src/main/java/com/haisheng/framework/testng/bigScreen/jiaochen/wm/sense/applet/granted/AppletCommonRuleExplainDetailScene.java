package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/applet/granted/common/rule-explain-detail的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:53:03
 */
@Builder
public class AppletCommonRuleExplainDetailScene extends BaseScene {
    /**
     * 描述 业务类型
     * 是否必填 true
     * 版本 v2.0
     */
    private final String businessType;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("business_type", businessType);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/common/rule-explain-detail";
    }
}