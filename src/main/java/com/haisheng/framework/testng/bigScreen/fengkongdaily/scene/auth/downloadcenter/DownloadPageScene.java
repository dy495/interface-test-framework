package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.downloadcenter;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.1. 下载中心-列表
 *
 * @author wangmin
 * @date 2021-04-01 14:22:35
 */
@Builder
public class DownloadPageScene extends BaseScene {
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
     * 描述 任务名称
     * 是否必填 false
     * 版本 -
     */
    private final String taskName;

    /**
     * 描述 任务类型
     * 是否必填 false
     * 版本 -
     */
    private final String taskType;

    /**
     * 描述 相关门店
     * 是否必填 false
     * 版本 -
     */
    private final String shopName;

    /**
     * 描述 申请人
     * 是否必填 false
     * 版本 -
     */
    private final String applicant;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("is_export", isExport);
        object.put("task_name", taskName);
        object.put("task_type", taskType);
        object.put("shop_name", shopName);
        object.put("applicant", applicant);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/download-center/download-page";
    }
}