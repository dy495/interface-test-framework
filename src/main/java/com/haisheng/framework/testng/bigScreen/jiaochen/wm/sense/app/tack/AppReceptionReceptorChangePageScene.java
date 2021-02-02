package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.app.tack;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 变更接待
 *
 * @author wangmin
 * @date 2021/1/29 15:11
 */
@Builder
public class AppReceptionReceptorChangePageScene extends BaseScene {
    private final Integer id;
    private final Integer shopId;
    private final String receptorId;

    @Override
    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("shop_id", shopId);
        jsonObject.put("receptor_id", receptorId);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/task/reception/receptor/change";
    }
}
