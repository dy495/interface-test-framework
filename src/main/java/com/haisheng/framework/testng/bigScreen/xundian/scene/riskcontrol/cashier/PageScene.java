package com.haisheng.framework.testng.bigScreen.xundian.scene.riskcontrol.cashier;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 50.1. 收银风控列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class PageScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 当前页
     * 是否必填 true
     * 版本 -
     */
    private final Integer page;

    /**
     * 描述 当前页的数量
     * 是否必填 true
     * 版本 -
     */
    private final Integer size;

    /**
     * 描述 门店名称
     * 是否必填 false
     * 版本 -
     */
    private final String shopName;

    /**
     * 描述 店长
     * 是否必填 false
     * 版本 -
     */
    private final String managerName;

    /**
     * 描述 店长手机号
     * 是否必填 false
     * 版本 -
     */
    private final String managerPhone;

    /**
     * 描述 拍戏类型
     * 是否必填 false
     * 版本 -
     */
    private final String sortEventType;

    /**
     * 描述 类型排序，必须和sort_type同时配置才会生效。0： 降序 1：升序 。默认降序
     * 是否必填 false
     * 版本 -
     */
    private final Integer sortEventTypeOrder;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("shop_name", shopName);
        object.put("manager_name", managerName);
        object.put("manager_phone", managerPhone);
        object.put("sort_event_type", sortEventType);
        object.put("sort_event_type_order", sortEventTypeOrder);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/risk-control/cashier/page";
    }
}