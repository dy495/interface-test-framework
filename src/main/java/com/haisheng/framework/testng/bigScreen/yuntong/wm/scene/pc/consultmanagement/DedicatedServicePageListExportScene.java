package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.consultmanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.7. 专属服务咨询列表导出 （池）(2021-03-12)
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class DedicatedServicePageListExportScene extends BaseScene {
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
     * 描述 车系id
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long carStyleId;

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
        object.put("shop_id", shopId);
        object.put("follow_date_start", followDateStart);
        object.put("follow_date_end", followDateEnd);
        object.put("follow_login_name", followLoginName);
        object.put("follow_sales_name", followSalesName);
        object.put("consult_date_start", consultDateStart);
        object.put("consult_date_end", consultDateEnd);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("is_over_time", isOverTime);
        object.put("car_style_id", carStyleId);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/consult-management/dedicated-service-page-list-export";
    }
}