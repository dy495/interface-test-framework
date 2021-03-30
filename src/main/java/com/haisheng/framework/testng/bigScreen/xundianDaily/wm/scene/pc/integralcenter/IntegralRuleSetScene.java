package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 42.23. 积分规则设置
 *
 * @author wangmin
 * @date 2021-03-30 14:00:03
 */
@Builder
public class IntegralRuleSetScene extends BaseScene {
    /**
     * 描述 规则id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 积分有效年份
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer year;

    /**
     * 描述 积分规则描述
     * 是否必填 false
     * 版本 v2.0
     */
    private final String description;

    /**
     * 描述 规则类型
     * 是否必填 false
     * 版本 v2.0
     */
    private final String ruleType;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("year", year);
        object.put("description", description);
        object.put("rule_type", ruleType);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-center/integral-rule-set";
    }
}