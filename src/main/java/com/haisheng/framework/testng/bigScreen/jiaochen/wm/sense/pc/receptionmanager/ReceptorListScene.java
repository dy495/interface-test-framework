package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 接待人员列表
 */
@Builder
public class ReceptorListScene extends BaseScene {
    private final Integer shopId;

    @Override
    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("shop_id", shopId);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/reception-manage/receptor/list";
    }
}
