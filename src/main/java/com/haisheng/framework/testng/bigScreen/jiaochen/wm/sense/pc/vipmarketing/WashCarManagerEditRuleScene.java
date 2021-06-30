package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/vip-marketing/wash-car-manager/edit-rule的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:17
 */
@Builder
public class WashCarManagerEditRuleScene extends BaseScene {
    /**
     * 描述 规则
     * 是否必填 true
     * 版本 1.2
     */
    private final String ruleDetail;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("rule_detail", ruleDetail);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/vip-marketing/wash-car-manager/edit-rule";
    }
}