package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.consultmanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.5. 在线专家咨询列表导出 （池）(2021-03-08)
 *
 * @author wangmin
 * @date 2021-04-13 20:17:12
 */
@Builder
public class OnlineExpertsPageListExportScene extends BaseScene {
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

    /**
     * 描述 跟进账号
     * 是否必填 false
     * 版本 v3.0
     */
    private final String followLoginName;

    /**
     * 描述 跟进人员
     * 是否必填 false
     * 版本 v3.0
     */
    private final String followSalesName;

    /**
     * 描述 车型id
     * 是否必填 false
     * 版本 -
     */
    private final Long carModelId;

    /**
     * 描述 咨询时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String consultDateStart;

    /**
     * 描述 咨询时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String consultDateEnd;

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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("shop_id", shopId);
        object.put("follow_date_start", followDateStart);
        object.put("follow_date_end", followDateEnd);
        object.put("follow_login_name", followLoginName);
        object.put("follow_sales_name", followSalesName);
        object.put("car_model_id", carModelId);
        object.put("consult_date_start", consultDateStart);
        object.put("consult_date_end", consultDateEnd);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("is_over_time", isOverTime);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/consult-management/online-experts-page-list-export";
    }
}