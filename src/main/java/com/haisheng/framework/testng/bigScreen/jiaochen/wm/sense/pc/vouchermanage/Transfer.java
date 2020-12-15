package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 卡券转移
 */
@Builder
public class Transfer extends BaseScene {
    private final String transferPhone;
    private final String receivePhone;
    private final List<Long> voucherIds;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("transfer_phone", transferPhone);
        object.put("receive_phone", receivePhone);
        object.put("voucher_ids", voucherIds);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/voucher-manage/transfer";
    }
}
