package com.haisheng.framework.testng.bigScreen.itemXundian.scene.downloadcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 23.3. 导出报表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class ReportExportScene extends BaseScene {
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
     * 描述 报表id
     * 是否必填 true
     * 版本 -
     */
    private final Long id;

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
     * 描述 门店idList
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray shopIdList;

    /**
     * 描述 自定义时间-起始时间
     * 是否必填 false
     * 版本 -
     */
    private final String startTime;

    /**
     * 描述 自定义时间-终止时间
     * 是否必填 false
     * 版本 -
     */
    private final String endTime;

    /**
     * 描述 报表数据维度
     * 是否必填 false
     * 版本 -
     */
    private final String dataDimension;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("report_type", reportType);
        object.put("report_time_dimension", reportTimeDimension);
        object.put("shop_id_List", shopIdList);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("data_dimension", dataDimension);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/download-center/report-export";
    }
}