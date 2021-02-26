package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 修改权益列表内容
 *
 * @author wangmin
 * @date 2021/2/1 17:02
 */
@Builder
public class EquityEditScene extends BaseScene {

    /**
     * 奖励数
     */
    private final Integer awardCount;

    /**
     * 权益说明
     */
    private final String description;

    /**
     * 权益id
     */
    private final Integer equityId;

    @Override
    public JSONObject getRequest() {
        JSONObject object = new JSONObject();
        object.put("award_count", awardCount);
        object.put("description", description);
        object.put("equity_id", equityId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/vip-marketing/equity/edit";
    }
}
