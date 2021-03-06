package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 变更接待
 */
@Builder
public class ReceptorChangeScene extends BaseScene {
    private final String receptorId;
    private final Long id;
    private final Long shopId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("receptor_id", receptorId);
        jsonObject.put("id", id);
        jsonObject.put("shop_id", shopId);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/reception-manage/receptor/change";
    }
}
