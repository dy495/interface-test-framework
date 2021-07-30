package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.riskcontrol.alarmrule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 52.6. 风控告警规则开关
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class AlarmRuleSwitchScene extends BaseScene {
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
     * 描述 id
     * 是否必填 true
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 状态 1启动 0不启动
     * 是否必填 true
     * 版本 -
     */
    private final Integer status;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/risk-control/alarm-rule/switch";
    }
}