package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.consultmanagement;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.2. 设置响应规则（池）（2021-03-08）通用
 *
 * @author wangmin
 * @date 2021-03-31 12:36:16
 */
@Builder
public class ResponseRuleEditScene extends BaseScene {
    /**
     * 描述 提醒时间
     * 是否必填 true
     * 版本 v3.0
     */
    private final Integer remindTime;

    /**
     * 描述 超市应答时间
     * 是否必填 true
     * 版本 v3.0
     */
    private final Integer overTime;

    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final JSONObject weekDay;

    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final JSONObject workDay;

    /**
     * 描述 业务类型 RENEWAL_INSURANCE 续保咨询 SALES 销售顾问咨询 ONLINE_EXPERTS 在线专家 USED_CAR_ASSESS 二手车评估 AFTER_SALES 售后顾问咨询 USED_CAR 二手车咨询
     * 是否必填 true
     * 版本 v3.0
     */
    private final String businessType;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("remind_time", remindTime);
        object.put("over_time", overTime);
        object.put("week_day", weekDay);
        object.put("work_day", workDay);
        object.put("business_type", businessType);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/consult-management/response-rule-edit";
    }
}