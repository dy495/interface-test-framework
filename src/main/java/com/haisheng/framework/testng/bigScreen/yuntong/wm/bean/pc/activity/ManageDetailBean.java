package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 28.12. 活动详情 （谢）v3.0（2021-04-02）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class ManageDetailBean implements Serializable {
    /**
     * 描述 活动id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 活动类型 通过活动目标树获取 0：列表优惠券，1：活动招募
     * 版本 v2.0
     */
    @JSONField(name = "type")
    private Integer type;

    /**
     * 描述 活动标题
     * 版本 v2.0
     */
    @JSONField(name = "title")
    private String title;

    /**
     * 描述 活动参与限制类型，0：全部可参加，1：部分可参加，2：部分不可参加
     * 版本 v2.0
     */
    @JSONField(name = "participation_limit_type")
    private Integer participationLimitType;

    /**
     * 描述 选中标签，限制类型为 1 或 2 时不可为空
     * 版本 v2.0
     */
    @JSONField(name = "choose_labels")
    private JSONArray chooseLabels;

    /**
     * 描述 选中标签名
     * 版本 v2.0
     */
    @JSONField(name = "choose_label_names")
    private JSONArray chooseLabelNames;

    /**
     * 描述 活动开始日期
     * 版本 v2.0
     */
    @JSONField(name = "start_date")
    private String startDate;

    /**
     * 描述 活动结束日期
     * 版本 v2.0
     */
    @JSONField(name = "end_date")
    private String endDate;

    /**
     * 描述 所属主体类型
     * 版本 v2.0
     */
    @JSONField(name = "subject_type")
    private String subjectType;

    /**
     * 描述 所属主体id
     * 版本 v2.0
     */
    @JSONField(name = "subject_id")
    private Long subjectId;

    /**
     * 描述 活动标签
     * 版本 v2.0
     */
    @JSONField(name = "label")
    private String label;

    /**
     * 描述 活动图片OSS地址
     * 版本 -
     */
    @JSONField(name = "pic_list")
    private JSONArray picList;

    /**
     * 描述 图片展示路径
     * 版本 v1.0
     */
    @JSONField(name = "pic_url")
    private String picUrl;

    /**
     * 描述 图片oss路径
     * 版本 v1.0
     */
    @JSONField(name = "pic_path")
    private String picPath;

    /**
     * 描述 活动状态
     * 版本 v2.0
     */
    @JSONField(name = "status")
    private Integer status;

    /**
     * 描述 活动状态描述
     * 版本 v2.0
     */
    @JSONField(name = "status_name")
    private String statusName;

    /**
     * 描述 是否自定义分享信息
     * 版本 v3.0
     */
    @JSONField(name = "is_custom_share_info")
    private Boolean isCustomShareInfo;

    /**
     * 描述 自定义分享图片
     * 版本 v3.0
     */
    @JSONField(name = "share_pic")
    private JSONObject sharePic;

    /**
     * 描述 自定义分享标题
     * 版本 v3.0
     */
    @JSONField(name = "share_title")
    private String shareTitle;

    /**
     * 描述 活动规则
     * 版本 v3.0
     */
    @JSONField(name = "rule")
    private String rule;

    /**
     * 描述 活动类型为裂变优惠券时返回
     * 版本 v2.0
     */
    @JSONField(name = "fission_voucher_info")
    private JSONObject fissionVoucherInfo;

    /**
     * 描述 领取限制类型，0：不限制，1：获取期间总次数，2：每天领取限制
     * 版本 v2.0
     */
    @JSONField(name = "receive_limit_type")
    private Integer receiveLimitType;

    /**
     * 描述 领取限制次数，1 或 2 时不能为空
     * 版本 v2.0
     */
    @JSONField(name = "receive_limit_times")
    private Integer receiveLimitTimes;

    /**
     * 描述 分享人数
     * 版本 v2.0
     */
    @JSONField(name = "share_num")
    private Integer shareNum;

    /**
     * 描述 分享者奖励
     * 版本 v2.0
     */
    @JSONField(name = "share_voucher")
    private JSONObject shareVoucher;

    /**
     * 描述 奖励项名称
     * 版本 v2.0
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 描述 奖励项主体
     * 版本 v2.0
     */
    @JSONField(name = "subject_name")
    private String subjectName;

    /**
     * 描述 奖励项总数
     * 版本 v2.0
     */
    @JSONField(name = "num")
    private Integer num;

    /**
     * 描述 卡券总库存
     * 版本 v2.0
     */
    @JSONField(name = "left_stock")
    private Long leftStock;

    /**
     * 描述 奖励项发放数
     * 版本 v2.0
     */
    @JSONField(name = "send_num")
    private Integer sendNum;

    /**
     * 描述 奖励项剩余库存
     * 版本 v2.0
     */
    @JSONField(name = "left_num")
    private Integer leftNum;

    /**
     * 描述 单张面值
     * 版本 v2.0
     */
    @JSONField(name = "price")
    private String price;

    /**
     * 描述 总成本
     * 版本 v2.0
     */
    @JSONField(name = "total_cost")
    private String totalCost;

    /**
     * 描述 创建人
     * 版本 v2.0
     */
    @JSONField(name = "creator_name")
    private String creatorName;

    /**
     * 描述 奖励有效期
     * 版本 v2.0
     */
    @JSONField(name = "voucher_valid")
    private JSONObject voucherValid;

    /**
     * 描述 卡券有效期类型 选择发送卡券时必填，1：时间段，2：有效天数
     * 版本 v2.0
     */
    @JSONField(name = "expire_type")
    private Integer expireType;

    /**
     * 描述 卡券有效开始日期 卡券有效期类型为1（时间段）必填
     * 版本 v2.0
     */
    @JSONField(name = "voucher_start")
    private String voucherStart;

    /**
     * 描述 卡券有效结束日期 卡券有效期类型为1（时间段）必填
     * 版本 v2.0
     */
    @JSONField(name = "voucher_end")
    private String voucherEnd;

    /**
     * 描述 卡券有效天数 卡券有效期类型为2（有效天数）必填
     * 版本 v2.0
     */
    @JSONField(name = "voucher_effective_days")
    private Integer voucherEffectiveDays;

    /**
     * 描述 被邀请者奖励
     * 版本 v2.0
     */
    @JSONField(name = "invited_voucher")
    private JSONObject invitedVoucher;

    /**
     * 描述 活动类型为活动招募时返回
     * 版本 v2.0
     */
    @JSONField(name = "recruit_activity_info")
    private JSONObject recruitActivityInfo;

    /**
     * 描述 活动报名开始日期
     * 版本 v2.0
     */
    @JSONField(name = "apply_start")
    private String applyStart;

    /**
     * 描述 活动报名结束日期
     * 版本 v2.0
     */
    @JSONField(name = "apply_end")
    private String applyEnd;

    /**
     * 描述 是否限制名额
     * 版本 v2.0
     */
    @JSONField(name = "is_limit_quota")
    private Boolean isLimitQuota;

    /**
     * 描述 限制名额数 限制名额时不能为空
     * 版本 v2.0
     */
    @JSONField(name = "quota")
    private Integer quota;

    /**
     * 描述 活动地址
     * 版本 v2.0
     */
    @JSONField(name = "address")
    private String address;

    /**
     * 描述 所需报名信息项list
     * 版本 v2.0
     */
    @JSONField(name = "register_information_list")
    private JSONArray registerInformationList;

    /**
     * 描述 此项信息是否展示
     * 版本 v2.0
     */
    @JSONField(name = "is_show")
    private Boolean isShow;

    /**
     * 描述 此项信息是否必填
     * 版本 v2.0
     */
    @JSONField(name = "is_required")
    private Boolean isRequired;

    /**
     * 描述 提示语
     * 版本 -
     */
    @JSONField(name = "value_tips")
    private String valueTips;

    /**
     * 描述 自定义报名项内容,当type为自定义时必填
     * 版本 -
     */
    @JSONField(name = " custom_condition")
    private JSONObject  customCondition;

    /**
     * 描述 复选框选择项列表，当type为复选框时必填
     * 版本 v3.0
     */
    @JSONField(name = "boxes")
    private JSONArray boxes;

    /**
     * 描述 是否报名成功奖励
     * 版本 v2.0
     */
    @JSONField(name = "success_reward")
    private Boolean successReward;

    /**
     * 描述 奖励卡券列表 报名成功奖励为是时，必填
     * 版本 v2.0
     */
    @JSONField(name = "reward_vouchers")
    private JSONArray rewardVouchers;

    /**
     * 描述 奖励领取方式 0：自动发放，1：主动领取
     * 版本 v2.0
     */
    @JSONField(name = "reward_receive_type")
    private Integer rewardReceiveType;

    /**
     * 描述 报名后是否需要审批
     * 版本 v2.0
     */
    @JSONField(name = "is_need_approval")
    private Boolean isNeedApproval;

    /**
     * 描述 行动点类型 见字典表《行动点类型》 活动类型为内容营销时返回
     * 版本 v3.0
     */
    @JSONField(name = "content_marketing_info")
    private JSONObject contentMarketingInfo;

    /**
     * 描述 行动点类型 见字典表《行动点类型》
     * 版本 v3.0
     */
    @JSONField(name = "action_point")
    private Integer actionPoint;

    /**
     * 描述 行动点名称
     * 版本 v3.0
     */
    @JSONField(name = "action_point_name")
    private String actionPointName;

}