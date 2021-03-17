package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.brand;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/brand/car-style/all的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class CarStyleAllScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 品牌id
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long brandId;

    /**
     * 描述 品牌名称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String name;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("brand_id", brandId);
        object.put("name", name);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/brand/car-style/all";
    }
}