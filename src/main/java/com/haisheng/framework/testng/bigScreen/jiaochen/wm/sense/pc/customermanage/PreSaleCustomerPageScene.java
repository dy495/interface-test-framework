package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.1. 销售客户列表 (池) v1.0 (2021-3-15更新)
 *
 * @author wangmin
 * @date 2021-03-31 12:32:56
 */
@Builder
public class PreSaleCustomerPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    @Builder.Default
    private Integer page = 1;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    @Builder.Default
    private Integer size = 10;

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
        return "/jiaochen/pc/customer-manage/pre-sale-customer/page";
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }
}
