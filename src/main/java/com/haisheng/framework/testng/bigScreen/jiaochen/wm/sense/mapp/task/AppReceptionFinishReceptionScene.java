package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 完成接待
 *
 * @author wangmin
 * @date 2021/1/29 15:11
 */
@Builder
public class AppReceptionFinishReceptionScene extends BaseScene {
    private final Long id;
    private final Long shopId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("shop_id", shopId);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/task/reception/finish-reception";
    }
}
