package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/manage/evaluate/config/submit的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class EvaluateConfigSubmitScene extends BaseScene {
    /**
     * 描述 评价类型 1：预约评价，2：新车评价
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer type;

    /**
     * 描述 默认好评周期 使用通用枚举接口获取，key为 DEFAULT_FAVOURABLE_CYCLE
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer defaultFavourableCycle;

    /**
     * 描述 是否提供评价奖励
     * 是否必填 true
     * 版本 v2.0
     */
    private final Boolean evaluateReward;

    /**
     * 描述 是否发送卡券
     * 是否必填 false
     * 版本 v2.0
     */
    private final Boolean isSendVoucher;

    /**
     * 描述 卡券列表 选择发送卡券时必填
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray vouchers;

    /**
     * 描述 卡券有效期配置 发送卡券时必填
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONObject voucherValid;

    /**
     * 描述 是否奖励积分
     * 是否必填 false
     * 版本 v2.0
     */
    private final Boolean isSendPoints;

    /**
     * 描述 奖励积分 奖励积分时需大于0
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer points;

    /**
     * 描述 评价分数列表
     * 是否必填 true
     * 版本 v2.0
     */
    private final JSONArray scores;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("default_favourable_cycle", defaultFavourableCycle);
        object.put("evaluate_reward", evaluateReward);
        object.put("is_send_voucher", isSendVoucher);
        object.put("vouchers", vouchers);
        object.put("voucher_valid", voucherValid);
        object.put("is_send_points", isSendPoints);
        object.put("points", points);
        object.put("scores", scores);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/manage/evaluate/config/submit";
    }
}