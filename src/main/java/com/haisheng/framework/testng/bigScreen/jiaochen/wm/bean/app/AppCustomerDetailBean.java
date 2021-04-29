package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 预约列表
 *
 * @author wangmin
 * @date 2021/1/29 15:20
 */
@Data
public class AppCustomerDetailBean implements Serializable {
    @JSONField(name = "id")
    private Long id;
    @JSONField(name = "shop_id")
    private Long shopId;
    @JSONField(name = "customer_name")
    private String customerName;
    @JSONField(name = "customer_gender")
    private String customerGender;
    @JSONField(name = "customer_phone")
    private String customerPhone;
    @JSONField(name = "car_model_id")
    private Long carModelId ;
}
