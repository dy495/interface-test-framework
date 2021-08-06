package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.ordercar;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.1. 申请订车 v5.0
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class OrderCarApplyScene extends BaseScene {
    /**
     * 描述 接待id
     * 是否必填 true
     * 版本 v5.0
     */
    private final Long receptionId;

    /**
     * 描述 身份证正面
     * 是否必填 true
     * 版本 v5.0
     */
    private final String frontCardPic;

    /**
     * 描述 身份证反面
     * 是否必填 true
     * 版本 v5.0
     */
    private final String reverseCardPic;

    /**
     * 描述 客户名称
     * 是否必填 true
     * 版本 v5.0
     */
    private final String customerName;

    /**
     * 描述 性别
     * 是否必填 true
     * 版本 v5.0
     */
    private final Integer gender;

    /**
     * 描述 身份证号
     * 是否必填 true
     * 版本 v5.0
     */
    private final String idCardNumber;

    /**
     * 描述 客户手机号
     * 是否必填 false
     * 版本 v5.0
     */
    private final String customerPhone;

    /**
     * 描述 地址
     * 是否必填 true
     * 版本 v5.0
     */
    private final String address;

    /**
     * 描述 车型id
     * 是否必填 false
     * 版本 v5.0
     */
    private final Long carModelId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("reception_id", receptionId);
        object.put("front_card_pic", frontCardPic);
        object.put("reverse_card_pic", reverseCardPic);
        object.put("customer_name", customerName);
        object.put("gender", gender);
        object.put("id_card_number", idCardNumber);
        object.put("customer_phone", customerPhone);
        object.put("address", address);
        object.put("car_model_id", carModelId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/order-car/apply";
    }
}