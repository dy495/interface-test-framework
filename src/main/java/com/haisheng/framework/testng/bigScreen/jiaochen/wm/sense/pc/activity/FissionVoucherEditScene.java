package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;
import java.lang.reflect.Array;
import java.util.List;

/**
 * 活动管理-编辑裂变活动
 */
@Builder
public class FissionVoucherEditScene extends BaseScene {
    private final Integer type;
    private final Integer participationLimitType;
    private final List<Integer> chooseLabels;
    private final Integer receiveLimitType;
    private final String receiveLimitTimes;
    private final String title;
    private final String  rule;
    private final String startDate;
    private final String endDate;
    private final String subjectType;
    private final Long subjectId;
    private final String label;
    private final List<String> picList;
    private final String shareNum;
    private final JSONObject shareVoucher;
    private final JSONObject invitedVoucher;
    private final Long id;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
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
        object.put("share_num", shareNum);
        object.put("share_voucher", shareVoucher);
        object.put("invited_voucher", invitedVoucher);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/activity/manage/fission-voucher/edit";
    }
}
