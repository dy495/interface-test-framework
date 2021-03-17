package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.insurancemanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/insurance-management/response-rule-edit的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class ResponseRuleEditScene extends BaseScene {
    /**
     * 描述 提醒时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final Integer remindTime;

    /**
     * 描述 超市应答时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final Integer overTime;

    /**
     * 描述 上午开始时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String forenoonDateStart;

    /**
     * 描述 上午结束时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String forenoonDateEnd;

    /**
     * 描述 下午开始时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String afternoonDateStart;

    /**
     * 描述 下午结束时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String afternoonDateEnd;

    /**
     * 描述 业务类型 后期补入接口文档
     * 是否必填 false
     * 版本 v3.0
     */
    private final String businessType;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("remind_time", remindTime);
        object.put("over_time", overTime);
        object.put("forenoon_date_start", forenoonDateStart);
        object.put("forenoon_date_end", forenoonDateEnd);
        object.put("afternoon_date_start", afternoonDateStart);
        object.put("afternoon_date_end", afternoonDateEnd);
        object.put("business_type", businessType);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/insurance-management/response-rule-edit";
    }
}