package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 确认预约
 *
 * @author wangmin
 * @date 2021/1/29 15:11
 */
@Builder
public class AppReceptionReceptorListScene extends BaseScene {
    private final Integer shopId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("shop_id", shopId);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/task/reception/receptor/list";
    }
}
