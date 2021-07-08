package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.5. 编辑洗车权益说明 (池) v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class WashCarManagerEditEquityScene extends BaseScene {
    /**
     * 描述 洗车权益说明
     * 是否必填 true
     * 版本 v2.0
     */
    private final String equityDetail;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("equity_detail", equityDetail);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/vip-marketing/wash-car-manager/edit-equity";
    }
}