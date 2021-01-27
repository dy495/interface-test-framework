package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;
/**
 * 活动管理-编辑招募活动
 */
@Builder
public class ManageRecruitEditScene extends BaseScene {
    private final Integer type;
    private final Integer participationLimitType;
    private final List<Integer> chooseLabels;
    private final Integer receiveLimitType;
    private final Integer receiveLimitTimes;
    private final String title;
    private final String  rule;
    private final String startDate;
    private final String endDate;
    private final String subjectType;
    private final Integer subjectId;
    private final String label;
    private final JSONArray picList;
    private final Integer applyStart;
    private final Integer applyEnd;
    private final Boolean isLimitQuota;
    private final Integer quota;
    private final JSONArray registerInformationList;
    private final Integer address;
    private final Boolean successReward;
    private final JSONArray rewardVouchers;
    private final JSONObject voucherValid;
    private final Integer rewardReceiveType;
    private final Boolean isNeedApproval;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("participation_limit_type", participationLimitType);
        object.put("choose_labels", chooseLabels);
        object.put("receive_limit_type", receiveLimitType);
        object.put("receive_limit_times", receiveLimitTimes);
        object.put("title", title);
        object.put("rule", rule);
        object.put("start_date", startDate);
        object.put("end_date", endDate);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        object.put("label", label);
        object.put("pic_list", picList);
        object.put("apply_start", applyStart);
        object.put("apply_end", applyEnd);
        object.put("is_limit_quota", isLimitQuota);
        object.put("quota", quota);
        object.put("address", address);
        object.put("register_information_list", registerInformationList);
        object.put("success_reward", successReward);
        object.put("reward_vouchers", rewardVouchers);
        object.put("voucher_valid", voucherValid);
        object.put("reward_receive_type", rewardReceiveType);
        object.put("is_need_approval", isNeedApproval);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/activity/manage/recruit/edit";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
