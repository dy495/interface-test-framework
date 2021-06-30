package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.receptionmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 23.5. 接待车辆 （谢）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class ReceptionScene extends BaseScene {
    /**
     * 描述 车牌号
     * 是否必填 true
     * 版本 v1.0
     */
    private final String plateNumber;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("plate_number", plateNumber);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/reception-manage/reception";
    }
}