package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.5. app完成接待
 *
 * @author wangmin
 * @date 2021-03-31 13:03:23
 */
@Builder
public class AppFinishReceptionScene extends BaseScene {
    private final String shopId;
    private final String id;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/pre-sales-reception/finish-reception";
    }
}