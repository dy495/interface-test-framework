package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.downloadcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 23.2. 导出记录分页
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class DownloadPageScene extends BaseScene {
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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("task_name", taskName);
        object.put("task_type", taskType);
        object.put("shop_name", shopName);
        object.put("applicant", applicant);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/download-center/download-page";
    }
}