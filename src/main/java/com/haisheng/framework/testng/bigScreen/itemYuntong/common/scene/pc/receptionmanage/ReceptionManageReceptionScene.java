package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.receptionmanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 28.5. 接待车辆 （谢）
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class ReceptionManageReceptionScene extends BaseScene {
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
        return "/car-platform/pc/reception-manage/reception";
    }
}