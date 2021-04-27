package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 修改分享管理
 *
 * @author wangmin
 * @date 2021/2/1 17:02
 */
@Builder
public class ShareManagerEditScene extends BaseScene {

    /**
     * 任务id
     */
    private final Integer id;

    /**
     * 说明
     */
    private final String taskExplain;

    /**
     * 奖励用户规则
     */
    private final String awardCustomerRule;

    /**
     * 图片url
     */
    private final String picturePath;

    /**
     * 奖励积分
     */
    private final Integer awardScore;

    /**
     * 奖励卡卷
     */
    private final Integer awardCardVolumeId;

    /**
     * 有效天数
     */
    private final String dayNumber;

    /**
     * 生效开始日期
     */
    private final String takeEffectStartTime;

    /**
     * 生效结束日期
     */
    private final String takeEffectEndTime;

    /**
     * 业务类型
     */
    private final String businessType;

    /**
     * DAY 天数生效
     * TIME 时间段生效
     */
    private final String takeEffectType;

    /**
     * 任务有效期
     */
    private final String day;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("task_explain", taskExplain);
        object.put("award_customer_rule", awardCustomerRule);
        object.put("picture_path", picturePath);
        object.put("award_score", awardScore);
        object.put("award_card_volume_id", awardCardVolumeId);
        object.put("day_number", dayNumber);
        object.put("take_effect_start_time", takeEffectStartTime);
        object.put("take_effect_end_time", takeEffectEndTime);
        object.put("business_type", businessType);
        object.put("take_effect_type", takeEffectType);
        object.put("day", day);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/vip-marketing/share-manager/edit";
    }
}
