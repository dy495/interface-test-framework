package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.9. 售前客户购车记录导出 (池)v3.0
 *
 * @author wangmin
 * @date 2021-03-31 12:32:56
 */
@Builder
public class PreSaleCustomerBuyCarPageExportScene extends BaseScene {
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
     * 描述 门店Id
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long shopId;

    /**
     * 描述 客户Id
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long customerId;

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
    private final String phone;

    /**
     * 描述 车系Id
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long carStyleId;

    /**
     * 描述 销售名称
     * 是否必填 false
     * 版本 v3.0
     */
    private final String preSaleName;

    /**
     * 描述 销售账号
     * 是否必填 false
     * 版本 v3.0
     */
    private final String preSaleAccount;

    /**
     * 描述 底盘号
     * 是否必填 false
     * 版本 v3.0
     */
    private final String vehicleChassisCode;

    /**
     * 描述 购车日期
     * 是否必填 false
     * 版本 v3.0
     */
    private final String buyCarTimeStart;

    /**
     * 描述 购车日期
     * 是否必填 false
     * 版本 v3.0
     */
    private final String buyCarTimeEnd;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("shop_id", shopId);
        object.put("customer_id", customerId);
        object.put("customer_name", customerName);
        object.put("phone", phone);
        object.put("car_style_id", carStyleId);
        object.put("pre_sale_name", preSaleName);
        object.put("pre_sale_account", preSaleAccount);
        object.put("vehicle_chassis_code", vehicleChassisCode);
        object.put("buy_car_time_start", buyCarTimeStart);
        object.put("buy_car_time_end", buyCarTimeEnd);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/customer-manage/pre-sale-customer/buy-car/page/export";
    }
}