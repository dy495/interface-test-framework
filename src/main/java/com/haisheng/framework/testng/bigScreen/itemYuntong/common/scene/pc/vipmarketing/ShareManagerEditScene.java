package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.19. 分享管理(修改) (池) v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class ShareManagerEditScene extends BaseScene {
    /**
     * 描述 任务id
     * 是否必填 false
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 说明
     * 是否必填 true
     * 版本 v2.0
     */
    private final String taskExplain;

    /**
     * 描述 奖励用户规则 EVERY_TIME("新用户单次"); ONCE("老用户每次"),
     * 是否必填 true
     * 版本 v2.0
     */
    private final String awardCustomerRule;

    /**
     * 描述 图片url
     * 是否必填 true
     * 版本 v2.0
     */
    private final String picturePath;

    /**
     * 描述 奖励积分
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer awardScore;

    /**
     * 描述 奖励卡卷
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long awardCardVolumeId;

    /**
     * 描述 有效天数
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer dayNumber;

    /**
     * 描述 生效开始日期
     * 是否必填 false
     * 版本 v2.0
     */
    private final String takeEffectStartTime;

    /**
     * 描述 生效结束日期
     * 是否必填 false
     * 版本 v2.0
     */
    private final String takeEffectEndTime;

    /**
     * 描述 DAY 天数生效 TIME 时间段生效
     * 是否必填 false
     * 版本 v2.0
     */
    private final String takeEffectType;

    /**
     * 描述 任务有效天数
     * 是否必填 false
     * 版本 -
     */
    private final Integer day;


    @Override
    protected JSONObject getRequestBody() {
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
        object.put("take_effect_type", takeEffectType);
        object.put("day", day);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/vip-marketing/share-manager/edit";
    }
}