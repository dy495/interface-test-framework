package com.haisheng.framework.testng.bigScreen.xundian.scene.riskcontrol.alarmrule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 52.4. 编辑风控告警规则
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class EditScene extends BaseScene {
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
     * 描述 风控告警规则
     * 是否必填 true
     * 版本 -
     */
    private final String name;

    /**
     * 描述 风控告警类型
     * 是否必填 true
     * 版本 -
     */
    private final String type;

    /**
     * 描述 风控规则
     * 是否必填 true
     * 版本 -
     */
    private final JSONArray ruleIdList;

    /**
     * 描述 接受者id
     * 是否必填 true
     * 版本 -
     */
    private final JSONArray acceptRoleIdList;

    /**
     * 描述 开始时间
     * 是否必填 true
     * 版本 -
     */
    private final String startTime;

    /**
     * 描述 结束时间
     * 是否必填 true
     * 版本 -
     */
    private final String endTime;

    /**
     * 描述 沉默时间
     * 是否必填 true
     * 版本 -
     */
    private final String silentTime;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("name", name);
        object.put("type", type);
        object.put("rule_id_list", ruleIdList);
        object.put("accept_role_id_list", acceptRoleIdList);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("silent_time", silentTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/risk-control/alarm-rule/edit";
    }
}