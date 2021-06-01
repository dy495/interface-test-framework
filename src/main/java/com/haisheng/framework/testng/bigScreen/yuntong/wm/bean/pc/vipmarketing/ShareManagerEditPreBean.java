package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 4.20. 分享管理(修改前获取根据任务id查询) (池) v2.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class ShareManagerEditPreBean implements Serializable {
    /**
     * 描述 业务类型
     * 版本 -
     */
    @JSONField(name = "business_type")
    private String businessType;

    /**
     * 描述 任务id
     * 版本 -
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 说明
     * 版本 v2.0
     */
    @JSONField(name = "task_explain")
    private String taskExplain;

    /**
     * 描述 奖励用户规则 EVERY_TIME("新用户单次"); ONCE("老用户每次"),
     * 版本 v2.0
     */
    @JSONField(name = "award_customer_rule")
    private String awardCustomerRule;

    /**
     * 描述 图片url
     * 版本 v2.0
     */
    @JSONField(name = "picture_path")
    private String picturePath;

    /**
     * 描述 奖励积分
     * 版本 v2.0
     */
    @JSONField(name = "award_score")
    private Integer awardScore;

    /**
     * 描述 奖励卡卷
     * 版本 v2.0
     */
    @JSONField(name = "award_card_volume_id")
    private Long awardCardVolumeId;

    /**
     * 描述 有效天数
     * 版本 v2.0
     */
    @JSONField(name = "day_number")
    private String dayNumber;

    /**
     * 描述 生效开始日期
     * 版本 v2.0
     */
    @JSONField(name = "take_effect_start_time")
    private String takeEffectStartTime;

    /**
     * 描述 生效结束日期
     * 版本 v2.0
     */
    @JSONField(name = "take_effect_end_time")
    private String takeEffectEndTime;

    /**
     * 描述 DAY 天数生效 TIME 时间段生效
     * 版本 v2.0
     */
    @JSONField(name = "take_effect_type")
    private String takeEffectType;

    /**
     * 描述 任务有效天数
     * 版本 v3.0
     */
    @JSONField(name = "day")
    private Integer day;

    /**
     * 描述 是否可编辑天数
     * 版本 -
     */
    @JSONField(name = "day_edit_flag")
    private Boolean dayEditFlag;

}