package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.saleschedule;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 14.2. 销售顾问修改状态 (刘) v5.0
 *
 * @author wangmin
 * @date 2021-08-06 16:38:24
 */
@Builder
public class AppSaleScheduleUpdateSaleStatusScene extends BaseScene {
    /**
     * 描述 销售id（必传）
     * 是否必填 true
     * 版本 v5.0
     */
    private final String saleId;

    /**
     * 描述 销售原始状态 公共enum传值SALES_STATUS
     * 是否必填 true
     * 版本 v5.0
     */
    private final Integer sourceSaleStatus;

    /**
     * 描述 销售变更状态 公共enum传值SALES_STATUS
     * 是否必填 true
     * 版本 v5.0
     */
    private final Integer targetSaleStatus;

    /**
     * 描述 休假开始时间
     * 是否必填 false
     * 版本 v5.0
     */
    private final String vacationStartTime;

    /**
     * 描述 休假结束时间
     * 是否必填 false
     * 版本 v5.0
     */
    private final String vacationEndTime;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("sale_id", saleId);
        object.put("source_sale_status", sourceSaleStatus);
        object.put("target_sale_status", targetSaleStatus);
        object.put("vacation_start_time", vacationStartTime);
        object.put("vacation_end_time", vacationEndTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/sale-schedule/update-sale-status";
    }
}