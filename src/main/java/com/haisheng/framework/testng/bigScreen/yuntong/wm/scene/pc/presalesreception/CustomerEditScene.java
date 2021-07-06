package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 21.12. 编辑客户（谢）v3.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class CustomerEditScene extends BaseScene {
    /**
     * 描述 接待id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long id;

    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long shopId;

    /**
     * 描述 性别
     * 是否必填 false
     * 版本 v3.0
     */
    private final Integer gender;

    /**
     * 描述 客户名称
     * 是否必填 false
     * 版本 v3.0
     */
    private final String customerName;

    /**
     * 描述 客户手机号
     * 是否必填 false
     * 版本 v3.0
     */
    private final String customerPhone;

    /**
     * 描述 意向车型
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long carModel;

    /**
     * 描述 预计购车时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String estimatedBuyTime;

    /**
     * 描述 客户车牌号1
     * 是否必填 false
     * 版本 v4.0
     */
    private final String plateNumber1;

    /**
     * 描述 客户车牌号2
     * 是否必填 false
     * 版本 v4.0
     */
    private final String plateNumber2;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shop_id", shopId);
        object.put("gender", gender);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("car_model", carModel);
        object.put("estimated_buy_time", estimatedBuyTime);
        object.put("plate_number1", plateNumber1);
        object.put("plate_number2", plateNumber2);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/pre-sales-reception/customer/edit";
    }
}