package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.brand;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/brand/car-style/car-model/detail的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class CarStyleCarModelDetailScene extends BaseScene {
    /**
     * 描述 车型id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/brand/car-style/car-model/detail";
    }
}