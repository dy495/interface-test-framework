package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.2. 权益修改 (池) v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class EquityEditScene extends BaseScene {
    /**
     * 描述 权益id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long equityId;

    /**
     * 描述 奖励数
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer awardCount;

    /**
     * 描述 权益说明
     * 是否必填 true
     * 版本 v2.0
     */
    private final String description;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("equity_id", equityId);
        object.put("award_count", awardCount);
        object.put("description", description);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/vip-marketing/equity/edit";
    }
}