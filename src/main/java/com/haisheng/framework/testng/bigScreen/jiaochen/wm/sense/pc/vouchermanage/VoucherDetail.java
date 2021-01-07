package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

@Builder
public class VoucherDetail extends BaseScene {
    private final Integer id;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/voucher-manage/voucher-detail";
    }

}
