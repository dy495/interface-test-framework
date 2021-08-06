package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.delivery;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.1. 申请交车 v5.0
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class DeliveryApplyScene extends BaseScene {
    /**
     * 描述 接待id
     * 是否必填 false
     * 版本 v5.0
     */
    private final Long receptionId;

    /**
     * 描述 订单id
     * 是否必填 false
     * 版本 v5.0
     */
    private final Long orderId;

    /**
     * 描述 交车海报图片
     * 是否必填 true
     * 版本 v5.0
     */
    private final String deliverPosterPic;

    /**
     * 描述 客户姓名
     * 是否必填 false
     * 版本 v5.0
     */
    private final String customerName;

    /**
     * 描述 底盘号
     * 是否必填 false
     * 版本 v5.0
     */
    private final String vehicleChassisCode;

    /**
     * 描述 车型
     * 是否必填 false
     * 版本 v5.0
     */
    private final Long carModelId;

    /**
     * 描述 称呼
     * 是否必填 false
     * 版本 v5.0
     */
    private final String call;

    /**
     * 描述 祝福语
     * 是否必填 false
     * 版本 v5.0
     */
    private final String blessing;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("reception_id", receptionId);
        object.put("order_id", orderId);
        object.put("deliver_poster_pic", deliverPosterPic);
        object.put("customer_name", customerName);
        object.put("vehicle_chassis_code", vehicleChassisCode);
        object.put("car_model_id", carModelId);
        object.put("call", call);
        object.put("blessing", blessing);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/delivery/apply";
    }
}