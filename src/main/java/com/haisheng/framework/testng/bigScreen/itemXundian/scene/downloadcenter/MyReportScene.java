package com.haisheng.framework.testng.bigScreen.itemXundian.scene.downloadcenter;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 23.1. 我的报表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class MyReportScene extends BaseScene {
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
     * 描述 报表名称
     * 是否必填 false
     * 版本 -
     */
    private final String reportName;

    /**
     * 描述 报表类型
     * 是否必填 false
     * 版本 -
     */
    private final String reportType;

    /**
     * 描述 报表时间维度
     * 是否必填 false
     * 版本 -
     */
    private final String reportTimeDimension;

    /**
     * 描述 相关门店
     * 是否必填 false
     * 版本 -
     */
    private final String shopName;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("report_name", reportName);
        object.put("report_type", reportType);
        object.put("report_time_dimension", reportTimeDimension);
        object.put("shop_name", shopName);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/download-center/my-report";
    }
}