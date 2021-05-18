package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.vipmarketing;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.4. 编辑洗车规则说明 (池) v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("rule_detail", ruleDetail);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/vip-marketing/wash-car-manager/edit-rule";
    }
}