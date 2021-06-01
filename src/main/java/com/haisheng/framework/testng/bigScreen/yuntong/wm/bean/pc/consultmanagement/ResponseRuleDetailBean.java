package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.consultmanagement;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 10.3. 响应规则详情（池）（2021-03-15）通用
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class ResponseRuleDetailBean implements Serializable {
    /**
     * 描述 提醒时间
     * 版本 v3.0
     */
    @JSONField(name = "remind_time")
    private Integer remindTime;

    /**
     * 描述 超时应答时间
     * 版本 v3.0
     */
    @JSONField(name = "over_time")
    private Integer overTime;

    /**
     * 描述 工作日
     * 版本 v3.0
     */
    @JSONField(name = "work_day")
    private JSONObject workDay;

    /**
     * 描述 上午开始时间
     * 版本 v3.0
     */
    @JSONField(name = "forenoon_date_start")
    private String forenoonDateStart;

    /**
     * 描述 上午结束时间
     * 版本 v3.0
     */
    @JSONField(name = "forenoon_date_end")
    private String forenoonDateEnd;

    /**
     * 描述 下午开始时间
     * 版本 v3.0
     */
    @JSONField(name = "afternoon_date_start")
    private String afternoonDateStart;

    /**
     * 描述 下午结束时间
     * 版本 v3.0
     */
    @JSONField(name = "afternoon_date_end")
    private String afternoonDateEnd;

    /**
     * 描述 休息日
     * 版本 v3.0
     */
    @JSONField(name = "week_day")
    private JSONObject weekDay;

}