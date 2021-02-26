package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 作废用户卡券
 */
@Builder
public class InvalidCustomerVoucherScene extends BaseScene {
    private final Long id;
    private final String invalidReason;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("invalid_reason", invalidReason);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/voucher-manage/invalid-customer-voucher";
    }
}
