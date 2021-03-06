package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.26. 流失客户维修记录-导出 (杨) v3.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class LossCustomerRepairPageExportScene extends BaseScene {
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
     * 描述 车辆id
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long carId;

    /**
     * 描述 门店id
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long shopId;

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
        object.put("car_id", carId);
        object.put("shop_id", shopId);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/customer-manage/loss-customer/repair-page/export";
    }
}