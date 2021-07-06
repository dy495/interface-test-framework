package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.customermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.24. 流失客户列表-导出 (杨) v3.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class LossCustomerPageExportScene extends BaseScene {
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
     * 描述 客户名称
     * 是否必填 false
     * 版本 v3.0
     */
    private final String customerName;

    /**
     * 描述 联系方式
     * 是否必填 false
     * 版本 v3.0
     */
    private final String customerPhone;

    /**
     * 描述 创建开始时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String createStartTime;

    /**
     * 描述 创建结束时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String createEndTime;

    /**
     * 描述 订单日期开始时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String orderStartTime;

    /**
     * 描述 订单日期结束时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String orderEndTime;

    /**
     * 描述 底盘号
     * 是否必填 false
     * 版本 v3.0
     */
    private final String vehicleChassisCode;

    /**
     * 描述 门店id
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long shopId;

    /**
     * 描述 是否注册 true 是 false
     * 是否必填 false
     * 版本 v3, 0
     */
    private final Boolean registrationStatus;

    /**
     * 描述 品牌
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long brandId;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Boolean isLoss;

    /**
     * 描述 导出类型 ALL：导出全部，CURRENT_PAGE：导出当前页，SPECIFIED_DATA：导出特定数据
     * 是否必填 true
     * 版本 v2.0
     */
    private final String exportType;

    /**
     * 描述 导出数据id列表，特定数据时必填
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray ids;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("create_start_time", createStartTime);
        object.put("create_end_time", createEndTime);
        object.put("order_start_time", orderStartTime);
        object.put("order_end_time", orderEndTime);
        object.put("vehicle_chassis_code", vehicleChassisCode);
        object.put("shop_id", shopId);
        object.put("registration_status", registrationStatus);
        object.put("brand_id", brandId);
        object.put("isLoss", isLoss);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/customer-manage/loss-customer/page/export";
    }
}