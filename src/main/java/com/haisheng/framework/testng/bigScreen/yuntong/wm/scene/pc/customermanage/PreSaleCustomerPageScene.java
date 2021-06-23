package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.1. 销售客户列表 (池) v1.0 (2021-05-06更新)
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class PreSaleCustomerPageScene extends BaseScene {
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
     * 版本 v1.0
     */
    private final String customerName;

    /**
     * 描述 客户手机
     * 是否必填 false
     * 版本 v1.0
     */
    private final String customerPhone;

    /**
     * 描述 服务顾问姓名
     * 是否必填 false
     * 版本 v1.0
     */
    private final String saleName;

    /**
     * 描述 客户类型
     * 是否必填 false
     * 版本 v1.0
     */
    private final String customerType;

    /**
     * 描述 性别
     * 是否必填 false
     * 版本 v1.0
     */
    private final String sex;

    /**
     * 描述 创建开始时间
     * 是否必填 false
     * 版本 v1.0
     */
    private final String startTime;

    /**
     * 描述 创建结束时间
     * 是否必填 false
     * 版本 v1.0
     */
    private final String endTime;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("sale_name", saleName);
        object.put("customer_type", customerType);
        object.put("sex", sex);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/customer-manage/pre-sale-customer/page";
    }
}