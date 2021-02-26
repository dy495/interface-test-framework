package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 权益开启或关闭
 *
 * @author wangmin
 * @date 2021/2/1 17:45
 */
@Builder
public class EquityStartOrCloseScene extends BaseScene {
    private final Integer equityId;
    private final String equityStatus;

    @Override
    public JSONObject getRequest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("equity_id", equityId);
        jsonObject.put("equity_status", equityStatus);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/vip-marketing/equity/start-or-close";
    }
}
