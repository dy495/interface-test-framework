package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shopstylemodel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 25.1. 保养、维修车型配置分页，替换"原保养配置分页"（谢）V3.0（2021-03-22）
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class ModelPageScene extends BaseScene {
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
     * 描述 预约类型 MAINTAIN：保养，REPAIR：维修
     * 是否必填 true
     * 版本 v3.0
     */
    private final String type;

    /**
     * 描述 品牌
     * 是否必填 false
     * 版本 v1.0
     */
    private final String brandName;

    /**
     * 描述 生产商
     * 是否必填 false
     * 版本 v1.0
     */
    private final String manufacturer;

    /**
     * 描述 车系
     * 是否必填 false
     * 版本 v2.0
     */
    private final String carStyle;

    /**
     * 描述 车型
     * 是否必填 false
     * 版本 v1.0
     */
    private final String carModel;

    /**
     * 描述 年款
     * 是否必填 false
     * 版本 v1.0
     */
    private final String year;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("type", type);
        object.put("brand_name", brandName);
        object.put("manufacturer", manufacturer);
        object.put("car_style", carStyle);
        object.put("car_model", carModel);
        object.put("year", year);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/shop-style-model/manage/model/page";
    }
}