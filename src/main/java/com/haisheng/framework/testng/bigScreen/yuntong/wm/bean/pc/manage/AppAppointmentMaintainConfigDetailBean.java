package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 5.2. 预约配置详情（谢）v3.0（2021-03-22）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class AppAppointmentMaintainConfigDetailBean implements Serializable {
    /**
     * 描述 提醒时间
     * 版本 v1.0
     */
    @JSONField(name = "remind_time")
    private Integer remindTime;

    /**
     * 描述 超时应答时间
     * 版本 v1.0
     */
    @JSONField(name = "replay_time_limit")
    private Integer replayTimeLimit;

    /**
     * 描述 正常应答间隔
     * 版本 v1.0
     */
    @JSONField(name = "appointment_interval")
    private Integer appointmentInterval;

}