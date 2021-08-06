package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.saleschedule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 14.3. 销售顾问组内排名更新(刘) v5.0
 *
 * @author wangmin
 * @date 2021-08-06 16:38:24
 */
@Builder
public class AppSaleScheduleUpdateSaleOrderScene extends BaseScene {
    /**
     * 描述 销售id
     * 是否必填 true
     * 版本 v5.0
     */
    private final String saleId;

    /**
     * 描述 组id
     * 是否必填 true
     * 版本 v5.0
     */
    private final String groupId;

    /**
     * 描述 排名
     * 是否必填 true
     * 版本 v5.0
     */
    private final String order;

    /**
     * 描述 销售状态
     * 是否必填 false
     * 版本 v5.0
     */
    private final String saleStatus;

    /**
     * 描述 PRE 销售 AFTER 售后
     * 是否必填 true
     * 版本 5.0
     */
    private final String type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("sale_id", saleId);
        object.put("group_id", groupId);
        object.put("order", order);
        object.put("sale_status", saleStatus);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/sale-schedule/update-sale-order";
    }
}