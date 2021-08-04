package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 15.12. 编辑客户（谢）v3.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-03-31 12:01:33
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
     * 描述 性别
     * 是否必填 false
     * 版本 v3.0
     */
    private final String gender;

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
     * 描述 底盘号
     * 是否必填 false
     * 版本 v3.0
     */
    private final String vin;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("gender", gender);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("car_model", carModel);
        object.put("estimated_buy_time", estimatedBuyTime);
        object.put("vin", vin);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/pre-sales-reception/customer/edit";
    }
}