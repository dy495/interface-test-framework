package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.insurancemanagement;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.3. 新建投保客户 （池）（2021-03-05）
 *
 * @author wangmin
 * @date 2021-03-31 12:32:56
 */
@Builder
public class CreateCustomerScene extends BaseScene {
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
     * 描述 归属门店
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long shopId;

    /**
     * 描述 客户名称
     * 是否必填 false
     * 版本 v3.0
     */
    private final String customerName;

    /**
     * 描述 联系方式1
     * 是否必填 false
     * 版本 v3.0
     */
    private final String phone1;

    /**
     * 描述 联系方式2
     * 是否必填 false
     * 版本 v3.0
     */
    private final String phone2;

    /**
     * 描述 车牌号码
     * 是否必填 false
     * 版本 v3.0
     */
    private final String plateNumber;

    /**
     * 描述 销售顾问
     * 是否必填 false
     * 版本 v3.0
     */
    private final String salesName;

    /**
     * 描述 创建开始时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String createDateStart;

    /**
     * 描述 创建结束时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String createDateEnd;

    /**
     * 描述 投保开始时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String insuranceDateStart;

    /**
     * 描述 投保结束时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String insuranceDateEnd;

    /**
     * 描述 车型
     * 是否必填 false
     * 版本 v3.0
     */
    private final String modelId;

    /**
     * 描述 投保次数
     * 是否必填 false
     * 版本 v3.0
     */
    private final String insuranceTimes;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("shop_id", shopId);
        object.put("customer_name", customerName);
        object.put("phone1", phone1);
        object.put("phone2", phone2);
        object.put("plate_number", plateNumber);
        object.put("sales_name", salesName);
        object.put("create_date_start", createDateStart);
        object.put("create_date_end", createDateEnd);
        object.put("insurance_date_start", insuranceDateStart);
        object.put("insurance_date_end", insuranceDateEnd);
        object.put("model_id", modelId);
        object.put("insurance_times", insuranceTimes);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/insurance-management/create-customer";
    }
}