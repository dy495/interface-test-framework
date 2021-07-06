package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.consultmanagement;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.3. 响应规则详情（池）（2021-03-15）通用
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class ResponseRuleDetailScene extends BaseScene {
    /**
     * 描述 咨询业务类型
     * 是否必填 true
     * 版本 v3.0
     */
    private final String businessType;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("business_type", businessType);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/consult-management/response-rule-detail";
    }
}