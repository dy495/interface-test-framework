package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/vip-marketing/wash-car-manager/rule-detail的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:17
 */
@Builder
public class WashCarManagerRuleDetailScene extends BaseScene {

    @Override
    public JSONObject getRequestBody(){
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/vip-marketing/wash-car-manager/rule-detail";
    }
}