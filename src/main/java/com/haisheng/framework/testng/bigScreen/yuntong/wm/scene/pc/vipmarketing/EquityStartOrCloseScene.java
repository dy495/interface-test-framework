package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.3. 权益开启或关闭 (池) v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class EquityStartOrCloseScene extends BaseScene {
    /**
     * 描述 权益id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long equityId;

    /**
     * 描述 权益状态 ENABLE "开启" DISABLE "关闭"
     * 是否必填 true
     * 版本 v2.0
     */
    private final String equityStatus;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("equity_id", equityId);
        object.put("equity_status", equityStatus);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/vip-marketing/equity/start-or-close";
    }
}