package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.brand;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.17. 品牌车系车型列表（谢）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class CarStyleCarModelAllScene extends BaseScene {
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
     * 描述 车系id
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long styleId;

    /**
     * 描述 品牌名称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String name;

    /**
     * 描述 品牌名称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String year;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("brand_id", brandId);
        object.put("style_id", styleId);
        object.put("name", name);
        object.put("year", year);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/brand/car-style/car-model/all";
    }
}