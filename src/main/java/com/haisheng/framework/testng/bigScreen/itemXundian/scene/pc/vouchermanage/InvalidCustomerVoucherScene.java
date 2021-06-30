package com.haisheng.framework.testng.bigScreen.itemXundian.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
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
        return "/shop/pc/voucher-manage/invalid-customer-voucher";
    }
}
