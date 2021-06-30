package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.1. 收银风控列表
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class PageScene extends BaseScene {
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
     * 描述 是否导出
     * 是否必填 false
     * 版本 v1.0
     */
    private final Boolean isExport;

    /**
     * 描述 门店名称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String shopName;

    /**
     * 描述 店长
     * 是否必填 false
     * 版本 v1.0
     */
    private final String managerName;

    /**
     * 描述 店长手机号
     * 是否必填 false
     * 版本 v1.0
     */
    private final String managerPhone;

    /**
     * 描述 排序类型
     * 是否必填 false
     * 版本 v1.0
     */
    private final String sortEventType;

    /**
     * 描述 类型排序，必须和sort_type同时配置才会生效。0： 降序 1：升序 。默认降序
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer sortEventTypeOrder;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("is_export", isExport);
        object.put("shop_name", shopName);
        object.put("manager_name", managerName);
        object.put("manager_phone", managerPhone);
        object.put("sort_event_type", sortEventType);
        object.put("sort_event_type_order", sortEventTypeOrder);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/cashier/page";
    }
}