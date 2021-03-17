package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/manage/maintain/car-model/page的接口
 *
 * @author wangmin
 * @date 2021-03-15 10:12:39
 */
@Builder
public class MaintainCarModelPageScene extends BaseScene {
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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("brand_name", brandName);
        object.put("manufacturer", manufacturer);
        object.put("car_style", carStyle);
        object.put("car_model", carModel);
        object.put("year", year);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/manage/maintain/car-model/page";
    }
}