package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * @author wangmin
 * @date 2021/1/26 15:54
 */
@Builder
public class VoucherInfoScene extends BaseScene {
    private final Long id;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/pc/voucher-manage/voucher-info";
    }
}
