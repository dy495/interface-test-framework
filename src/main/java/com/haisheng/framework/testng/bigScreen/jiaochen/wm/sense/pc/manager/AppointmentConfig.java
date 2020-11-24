package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

import java.util.List;

@Builder
public class AppointmentConfig extends BaseScene {
    private final String type;
    private final Long remindTime;
    private final Long replayTimeLimit;
    private final Long appointmentInterval;
    private final Boolean onTimeReward;
    private final Boolean isSendVoucher;
    private final List<Long> vouchers;
    private final String voucherStart;
    private final String voucherEnd;
    private final Integer voucherEffectiveDays;
    private final Long points;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("remind_time", remindTime);
        object.put("replay_time_limit", replayTimeLimit);
        object.put("appointment_interval", appointmentInterval);
        object.put("on_time_reward", onTimeReward);
        object.put("is_send_voucher", isSendVoucher);
        object.put("vouchers", vouchers);
        object.put("voucher_start", voucherStart);
        object.put("voucher_end", voucherEnd);
        object.put("voucher_effective_days", voucherEffectiveDays);
        object.put("points", points);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/manage/appointment/config";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
