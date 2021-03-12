package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.insurancemanagement;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/insurance-management/renew-insurance-page-list的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class RenewInsurancePageListScene extends BaseScene {
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
     * 描述 所属门店
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long shopId;

    /**
     * 描述 品牌
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long brandId;

    /**
     * 描述 跟进人员
     * 是否必填 false
     * 版本 v3.0
     */
    private final String followSalesName;

    /**
     * 描述 跟进账号
     * 是否必填 false
     * 版本 v3.0
     */
    private final String followLoginName;

    /**
     * 描述 任务开始时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String taskDateStart;

    /**
     * 描述 任务结束时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String taskDateEnd;

    /**
     * 描述 联系人
     * 是否必填 false
     * 版本 v3.0
     */
    private final String customerName;

    /**
     * 描述 联系电话
     * 是否必填 false
     * 版本 v3.0
     */
    private final String customerPhone;

    /**
     * 描述 是否超时
     * 是否必填 false
     * 版本 v3.0
     */
    private final Boolean isOverTime;

    /**
     * 描述 跟进开始时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String followDateStart;

    /**
     * 描述 跟进结束时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String followDateEnd;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("shop_id", shopId);
        object.put("brand_id", brandId);
        object.put("follow_sales_name", followSalesName);
        object.put("follow_login_name", followLoginName);
        object.put("task_date_start", taskDateStart);
        object.put("task_date_end", taskDateEnd);
        object.put("brand_id", brandId);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("is_over_time", isOverTime);
        object.put("follow_date_start", followDateStart);
        object.put("follow_date_end", followDateEnd);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/insurance-management/renew-insurance-page-list";
    }
}