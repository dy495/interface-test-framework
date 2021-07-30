package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.punchclockandsignrule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 24.1. 签到或打卡规则更新 （新增和更新都调用一个接口）
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class PunchClockAndSignRuleUpdateScene extends BaseScene {
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
     * 描述 打卡或签到类型 PUNCH_CLOCK 打卡 SIGN_IN 签到
     * 是否必填 false
     * 版本 -
     */
    private final String punchOrSignType;

    /**
     * 描述 打卡规则周期 WEEK 自然周 MONTH 自然月
     * 是否必填 false
     * 版本 -
     */
    private final String cycleType;

    /**
     * 描述 规则类型 TOTAL 累计签到 CONTINUE 连续签到
     * 是否必填 false
     * 版本 -
     */
    private final String type;

    /**
     * 描述 初始积分
     * 是否必填 false
     * 版本 -
     */
    private final Integer initIntegral;

    /**
     * 描述 初始优惠券id
     * 是否必填 false
     * 版本 -
     */
    private final Long initCouponId;

    /**
     * 描述 天数列表
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray ruleDaysList;

    /**
     * 描述 纪念日列表
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray anniversaryList;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("punch_or_sign_type", punchOrSignType);
        object.put("cycle_type", cycleType);
        object.put("type", type);
        object.put("init_integral", initIntegral);
        object.put("init_coupon_id", initCouponId);
        object.put("rule_days_list", ruleDaysList);
        object.put("anniversary_list", anniversaryList);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/punch-clock-and-sign-rule/update";
    }
}