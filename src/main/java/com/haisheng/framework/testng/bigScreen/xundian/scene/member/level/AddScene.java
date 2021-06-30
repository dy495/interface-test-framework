package com.haisheng.framework.testng.bigScreen.xundian.scene.member.level;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 11.6. 会员等级添加 ljq
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class AddScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 等级名称
     * 是否必填 true
     * 版本 -
     */
    private final String levelName;

    /**
     * 描述 图片oss path
     * 是否必填 true
     * 版本 -
     */
    private final String levelIconPath;

    /**
     * 描述 等级成长经验值
     * 是否必填 true
     * 版本 -
     */
    private final Integer levelExperience;

    /**
     * 描述 会员等级条件描述
     * 是否必填 true
     * 版本 -
     */
    private final String levelConditionDesc;

    /**
     * 描述 会员等级权益描述
     * 是否必填 true
     * 版本 -
     */
    private final String levelBenefitsDesc;

    /**
     * 描述 等级排序
     * 是否必填 true
     * 版本 -
     */
    private final Integer levelSort;

    /**
     * 描述 经验获取
     * 是否必填 true
     * 版本 -
     */
    private final Double experienceExchange;

    /**
     * 描述 积分获取
     * 是否必填 true
     * 版本 -
     */
    private final Double integralExchange;

    /**
     * 描述 是否隐藏
     * 是否必填 true
     * 版本 -
     */
    private final Boolean isHide;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("level_name", levelName);
        object.put("level_icon_path", levelIconPath);
        object.put("level_experience", levelExperience);
        object.put("level_condition_desc", levelConditionDesc);
        object.put("level_benefits_desc", levelBenefitsDesc);
        object.put("level_sort", levelSort);
        object.put("experience_exchange", experienceExchange);
        object.put("integral_exchange", integralExchange);
        object.put("is_hide", isHide);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/member/level/add";
    }
}