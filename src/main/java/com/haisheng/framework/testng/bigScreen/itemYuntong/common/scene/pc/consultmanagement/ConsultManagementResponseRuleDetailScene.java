package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.consultmanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.3. 响应规则详情（池）（2021-03-15）通用
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class ConsultManagementResponseRuleDetailScene extends BaseScene {
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
        return "/car-platform/pc/consult-management/response-rule-detail";
    }
}