package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.6. app接待预约（谢）v3.0（2020-12-15）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppReceptionV3Scene extends BaseScene {
    /**
     * 描述 预约id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 预约类型 见字典表《预约类型》
     * 是否必填 true
     * 版本 v3.0
     */
    private final String type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/task/appointment/reception/v3";
    }
}