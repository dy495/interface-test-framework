package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 56.23. 积分规则设置
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class IntegralRuleSetScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
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