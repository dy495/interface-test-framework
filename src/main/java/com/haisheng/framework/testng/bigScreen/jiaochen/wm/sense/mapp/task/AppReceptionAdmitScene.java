package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.1. 查询接待车辆车主信息（谢）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:23
 */
@Builder
public class AppReceptionAdmitScene extends BaseScene {
    /**
     * 描述 车牌号
     * 是否必填 true
     * 版本 v1.0
     */
    private final String plateNumber;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("plate_number", plateNumber);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/task/reception/admit";
    }
}