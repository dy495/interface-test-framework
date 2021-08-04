package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shopstylemodel;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 18.1. 保养、维修车型配置分页，替换"原保养配置分页"（谢）V3.0（2021-03-22）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class ManageModelPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    @Builder.Default
    private Integer page = 1;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    @Builder.Default
    private Integer size = 10;

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
    public JSONObject getRequestBody() {
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

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }
}