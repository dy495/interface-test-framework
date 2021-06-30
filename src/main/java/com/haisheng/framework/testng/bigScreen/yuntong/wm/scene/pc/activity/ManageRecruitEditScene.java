package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 29.8. 编辑招募活动 （谢）（2021-01-13）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class ManageRecruitEditScene extends BaseScene {
    /**
     * 描述 活动类型 见字典表《活动类型》
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer type;

    /**
     * 描述 活动参与限制类型，0：全部可参加，1：部分可参加，2：部分不可参加
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer participationLimitType;

    /**
     * 描述 选中标签，限制类型为 1 或 2 时不可为空 使用人群标签组接口获取
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray chooseLabels;

    /**
     * 描述 活动标题
     * 是否必填 true
     * 版本 v2.0
     */
    private final String title;

    /**
     * 描述 活动规则（v3.0）兼容活动介绍
     * 是否必填 true
     * 版本 v2.0
     */
    private final String rule;

    /**
     * 描述 活动开始日期
     * 是否必填 true
     * 版本 v2.0
     */
    private final String startDate;

    /**
     * 描述 活动结束日期
     * 是否必填 true
     * 版本 v2.0
     */
    private final String endDate;

    /**
     * 描述 所属主体类型
     * 是否必填 true
     * 版本 v2.0
     */
    private final String subjectType;

    /**
     * 描述 所属主体id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long subjectId;

    /**
     * 描述 活动标签
     * 是否必填 true
     * 版本 v2.0
     */
    private final String label;

    /**
     * 描述 活动图片OSS地址
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray picList;

    /**
     * 描述 是否自定义分享信息
     * 是否必填 false
     * 版本 v3.0
     */
    private final Boolean isCustomShareInfo;

    /**
     * 描述 自定义分享图片
     * 是否必填 false
     * 版本 v3.0
     */
    private final String sharePicPath;

    /**
     * 描述 自定义分享标题
     * 是否必填 false
     * 版本 v3.0
     */
    private final String shareTitle;

    /**
     * 描述 活动报名开始日期
     * 是否必填 true
     * 版本 v2.0
     */
    private final String applyStart;

    /**
     * 描述 活动报名结束日期
     * 是否必填 true
     * 版本 v2.0
     */
    private final String applyEnd;

    /**
     * 描述 是否限制名额
     * 是否必填 true
     * 版本 v2.0
     */
    private final Boolean isLimitQuota;

    /**
     * 描述 限制名额数 限制名额时不能为空
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer quota;

    /**
     * 描述 活动地址
     * 是否必填 false
     * 版本 v2.0
     */
    private final String address;

    /**
     * 描述 所需报名信息项list 使用"创建招募活动报名信息项列表"接口获取
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray registerInformationList;

    /**
     * 描述 是否报名成功奖励
     * 是否必填 true
     * 版本 v2.0
     */
    private final Boolean successReward;

    /**
     * 描述 奖励卡券列表 报名成功奖励为是时，必填
     * 是否必填 true
     * 版本 v2.0
     */
    private final JSONArray rewardVouchers;

    /**
     * 描述 奖励有效期 报名成功奖励为是时，必填
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONObject voucherValid;

    /**
     * 描述 奖励领取方式 0：自动发放，1：主动领取
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer rewardReceiveType;

    /**
     * 描述 报名后是否需要审批
     * 是否必填 true
     * 版本 v2.0
     */
    private final Boolean isNeedApproval;

    /**
     * 描述 活动id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("participation_limit_type", participationLimitType);
        object.put("choose_labels", chooseLabels);
        object.put("title", title);
        object.put("rule", rule);
        object.put("start_date", startDate);
        object.put("end_date", endDate);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        object.put("label", label);
        object.put("pic_list", picList);
        object.put("is_custom_share_info", isCustomShareInfo);
        object.put("share_pic_path", sharePicPath);
        object.put("share_title", shareTitle);
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
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/activity/manage/recruit/edit";
    }
}