package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.consultmanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.3. 响应规则详情（池）（2021-03-15）通用的接口
 *
 * @author wangmin
 * @date 2021-03-24 14:32:26
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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("business_type", businessType);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/consult-management/response-rule-detail";
    }
}