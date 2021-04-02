package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 4.2. 新建风控告警规则
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class AddScene extends BaseScene {
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
    private final List<Long> ruleIdList;

    /**
     * 描述 接受者id
     * 是否必填 true
     * 版本 -
     */
    private final List<Long> acceptRoleIdList;

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
    private final Long silentTime;

    /**
     * 描述 是否接收实时告警
     * 是否必填 true
     * 版本 -
     */
    private final Boolean realTime;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("type", type);
        object.put("rule_id_list", ruleIdList);
        object.put("accept_role_id_list", acceptRoleIdList);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("silent_time", silentTime);
        object.put("real_time", realTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/alarm-rule/add";
    }
}