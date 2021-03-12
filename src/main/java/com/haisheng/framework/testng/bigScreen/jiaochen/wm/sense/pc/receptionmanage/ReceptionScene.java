package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/reception-manage/reception的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("plate_number", plateNumber);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/reception-manage/reception";
    }
}