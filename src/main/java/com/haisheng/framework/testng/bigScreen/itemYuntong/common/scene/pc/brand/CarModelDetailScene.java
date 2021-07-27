package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.brand;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 36.18. 品牌车系车型详情（谢）
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class CarModelDetailScene extends BaseScene {
    /**
     * 描述 车型id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/brand/car-style/car-model/detail";
    }
}