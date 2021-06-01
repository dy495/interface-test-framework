package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 18.4. 评价配置详情（谢）v3.0（2021-03-12）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class EvaluateConfigDetailBean implements Serializable {
    /**
     * 描述 默认好评周期 使用通用枚举接口获取，key为 DEFAULT_FAVOURABLE_CYCLE
     * 版本 v2.0
     */
    @JSONField(name = "default_favourable_cycle")
    private Integer defaultFavourableCycle;

    /**
     * 描述 是否提供评价奖励
     * 版本 v2.0
     */
    @JSONField(name = "evaluate_reward")
    private Boolean evaluateReward;

    /**
     * 描述 是否发送卡券
     * 版本 v2.0
     */
    @JSONField(name = "is_send_voucher")
    private Boolean isSendVoucher;

    /**
     * 描述 卡券列表 选择发送卡券时必填
     * 版本 v2.0
     */
    @JSONField(name = "vouchers")
    private JSONArray vouchers;

    /**
     * 描述 卡券有效期配置 发送卡券时必填
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
     * 描述 是否奖励积分
     * 版本 v2.0
     */
    @JSONField(name = "is_send_points")
    private Boolean isSendPoints;

    /**
     * 描述 奖励积分 奖励积分时需大于0
     * 版本 v2.0
     */
    @JSONField(name = "points")
    private Integer points;

    /**
     * 描述 评价分数列表
     * 版本 v2.0
     */
    @JSONField(name = "scores")
    private JSONArray scores;

    /**
     * 描述 评价分数
     * 版本 v2.0
     */
    @JSONField(name = "score")
    private Integer score;

    /**
     * 描述 评价提示语
     * 版本 v2.0
     */
    @JSONField(name = "describe")
    private String describe;

    /**
     * 描述 标签列表
     * 版本 v2.0
     */
    @JSONField(name = "labels")
    private JSONArray labels;

}