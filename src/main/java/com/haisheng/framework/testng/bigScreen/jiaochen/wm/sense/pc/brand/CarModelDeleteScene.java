package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.brand;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 34.21. 删除品牌车型 （谢）
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class CarModelDeleteScene extends BaseScene {
    /**
     * 描述 品牌id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long brandId;

    /**
     * 描述 车系id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long styleId;

    /**
     * 描述 车型名称
     * 是否必填 true
     * 版本 v1.0
     */
    private final String name;

    /**
     * 描述 车型年款
     * 是否必填 true
     * 版本 v1.0
     */
    private final String year;

    /**
     * 描述 车型预约状态
     * 是否必填 true
     * 版本 v1.0
     */
    private final String status;

    /**
     * 描述 车型id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("brand_id", brandId);
        object.put("style_id", styleId);
        object.put("name", name);
        object.put("year", year);
        object.put("status", status);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/brand/car-style/car-model/delete";
    }
}