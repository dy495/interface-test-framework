package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.receptionmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 23.6. 开始接待车主车辆 （谢）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class StartReceptionScene extends BaseScene {
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
        return "/yt/pc/reception-manage/start-reception";
    }
}