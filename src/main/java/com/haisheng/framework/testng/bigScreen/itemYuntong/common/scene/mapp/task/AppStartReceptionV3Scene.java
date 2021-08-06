package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 11.3. 开始接待车主车辆v3版本 （谢）v3.0 替换"开始接待车主车辆"接口
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppStartReceptionV3Scene extends BaseScene {
    /**
     * 描述 接待顾客id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long customerId;

    /**
     * 描述 接待车牌号
     * 是否必填 true
     * 版本 v1.0
     */
    private final String plateNumber;

    /**
     * 描述 售后接待类型 通过枚举接口获取，key为 AFTER_SALES_RECEPTION_TYPE
     * 是否必填 true
     * 版本 v3.0
     */
    private final String afterSalesType;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        object.put("plate_number", plateNumber);
        object.put("after_sales_type", afterSalesType);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/task/reception/start-reception/v3";
    }
}