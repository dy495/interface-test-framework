package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/vip-marketing/wash-car-manager/edit-equity的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:17
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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("equity_detail", equityDetail);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/vip-marketing/wash-car-manager/edit-equity";
    }
}