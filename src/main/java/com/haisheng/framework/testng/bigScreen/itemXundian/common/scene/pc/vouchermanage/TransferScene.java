package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 卡券转移
 */
@Builder
public class TransferScene extends BaseScene {
    private final String transferPhone;
    private final String receivePhone;
    private final List<Long> voucherIds;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("transfer_phone", transferPhone);
        object.put("receive_phone", receivePhone);
        object.put("voucher_ids", voucherIds);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/voucher-manage/transfer";
    }
}
