package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.brand;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 26.18. 品牌车系车型详情（谢）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/brand/car-style/car-model/detail";
    }
}