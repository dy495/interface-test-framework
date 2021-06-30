package com.haisheng.framework.testng.bigScreen.xundian.scene.riskcontrol.transaction;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 54.1. 批量下载视频
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class TraceBackBatchVideoScene extends BaseScene {
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
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final Long shopId;

    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final String startDate;

    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final String endDate;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("shopId", shopId);
        object.put("startDate", startDate);
        object.put("endDate", endDate);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/risk-control/transaction/trace-back/batch-video";
    }
}