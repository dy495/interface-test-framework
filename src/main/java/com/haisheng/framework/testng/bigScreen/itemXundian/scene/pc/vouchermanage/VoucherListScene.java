package com.haisheng.framework.testng.bigScreen.itemXundian.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 卡券列表
 */
@Builder
public class VoucherListScene extends BaseScene {
    private final String transferPhone;

    @Override
    public JSONObject getRequestBody() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("transfer_phone", transferPhone);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/shop/pc/voucher-manage/voucher-list";
    }
}
