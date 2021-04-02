package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.manage;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 3.2. 预约配置详情（谢）v3.0（2021-03-22）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class AppointmentMaintainConfigDetailBean implements Serializable {
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