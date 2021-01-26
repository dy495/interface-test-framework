package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 卡券列表
 */
@Builder
public class VoucherListScene extends BaseScene {

    @Override
    public JSONObject getJSONObject() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/reception-manage/voucher-list";
    }
}
