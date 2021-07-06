package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.1. 提交预约配置（谢）v3.0（2021-03-22）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class AppAppointmentConfigScene extends BaseScene {
    /**
     * 描述 预约类型 取值见字典表《预约类型》 v3.0增加试驾
     * 是否必填 true
     * 版本 v2.0
     */
    private final String type;

    /**
     * 描述 提醒时间
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer remindTime;

    /**
     * 描述 超时应答时间
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer replayTimeLimit;

    /**
     * 描述 正常应答间隔
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer appointmentInterval;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("remind_time", remindTime);
        object.put("replay_time_limit", replayTimeLimit);
        object.put("appointment_interval", appointmentInterval);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/manage/appointment/config";
    }
}