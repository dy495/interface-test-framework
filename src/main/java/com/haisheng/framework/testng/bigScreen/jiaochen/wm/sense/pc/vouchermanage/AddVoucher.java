package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

@Builder
public class AddVoucher extends BaseScene {
    private final Long id;
    private final Integer addNumber;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("add_number", addNumber);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/voucher-manage/add-voucher";
    }
}
