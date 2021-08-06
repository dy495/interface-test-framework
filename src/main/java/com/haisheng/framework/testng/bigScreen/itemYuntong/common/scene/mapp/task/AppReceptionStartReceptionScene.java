package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 11.2. 开始接待车主车辆（谢）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppReceptionStartReceptionScene extends BaseScene {
    /**
     * 描述 接待顾客id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long customerId;

    /**
     * 描述 接待车牌号
     * 是否必填 true
     * 版本 v1.0
     */
    private final String plateNumber;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        object.put("plate_number", plateNumber);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/task/reception/start-reception";
    }
}