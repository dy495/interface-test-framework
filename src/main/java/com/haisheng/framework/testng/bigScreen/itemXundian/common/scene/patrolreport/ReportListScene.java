package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.patrolreport;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 46.1. 巡店报告中心列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class ReportListScene extends BaseScene {
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
     * 描述 名称
     * 是否必填 false
     * 版本 -
     */
    private final String patrolPerson;

    /**
     * 描述 门店名称
     * 是否必填 false
     * 版本 -
     */
    private final String shopName;

    /**
     * 描述 报告状态
     * 是否必填 false
     * 版本 -
     */
    private final String reportStatus;

    /**
     * 描述 处理状态
     * 是否必填 false
     * 版本 -
     */
    private final String dealStatus;

    /**
     * 描述 类型排序.0： 降序 1：升序 。默认降序
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
        object.put("patrol_person", patrolPerson);
        object.put("shop_name", shopName);
        object.put("report_status", reportStatus);
        object.put("deal_status", dealStatus);
        object.put("sort_event_type_order", sortEventTypeOrder);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/patrol-report/report-list";
    }
}