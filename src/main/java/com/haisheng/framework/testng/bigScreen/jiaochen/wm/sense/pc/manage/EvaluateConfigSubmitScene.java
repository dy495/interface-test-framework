package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.5. 新增/修改评价配置详情（谢）v3.0（2021-03-12）
 *
 * @author wangmin
 * @date 2021-03-31 12:29:35
 */
@Builder
public class EvaluateConfigSubmitScene extends BaseScene {
    /**
     * 描述 评价类型 枚举见字典表《评价类型》
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
    private final String vouchersId;

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

    //?????? 怎么能这么写 其他调用的不就定死了
    @Override
    public JSONObject getRequestBody() {
        String request = "{\n" +
                "    \"type\":" + type + ",\n" +
                "    \"default_favourable_cycle\":" + defaultFavourableCycle + ",\n" +
                "    \"evaluate_reward\":" + evaluateReward + ",\n" +
                "    \"is_send_points\":" + isSendPoints + ",\n" +
                "    \"is_send_voucher\":" + isSendVoucher + ",\n" +
                "    \"evaluate_reward\":" + evaluateReward + ",\n" +
                "    \"points\":" + points + ",\n" +
                "    \"vouchers\":[\n" + vouchersId + "\n" +
                "    ],\n" +
                "    \"voucher_valid\":{\n" +
                "        \"expire_type\":2,\n" +
                "        \"voucher_effective_days\":1\n" +
                "    },\n" +
                "    \"scores\":[\n" +
                "        {\n" +
                "            \"score\":1,\n" +
                "            \"describe\":\"荣耀黄金\",\n" +
                "            \"labels\":[\n" +
                "                \"菜\",\n" +
                "                \"跨开开\",\n" +
                "                \"零零零零\",\n" +
                "                \"卡卡卡卡\"\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"score\":2,\n" +
                "            \"describe\":\"华贵铂金\",\n" +
                "            \"labels\":[\n" +
                "                \"菜\",\n" +
                "                \"科马\",\n" +
                "                \"来了\",\n" +
                "                \"你奶娘\"\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"score\":3,\n" +
                "            \"describe\":\"璀璨钻石\",\n" +
                "            \"labels\":[\n" +
                "                \"强\"\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"score\":4,\n" +
                "            \"describe\":\"超凡大师\",\n" +
                "            \"labels\":[\n" +
                "                \"半神\",\n" +
                "                \"uuu\",\n" +
                "                \"卡卡卡卡\",\n" +
                "                \"经济经济\"\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"score\":5,\n" +
                "            \"describe\":\"最强王者\",\n" +
                "            \"labels\":[\n" +
                "                \"大神\",\n" +
                "                \"u哈哈哈哈\",\n" +
                "                \"卡卡卡卡\",\n" +
                "                \"怕怕怕怕\"\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        return JSONObject.parseObject(request);
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/manage/evaluate/config/submit";
    }
}