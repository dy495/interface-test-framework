package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.5. 批量下载视频
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class TraceBackBatchVideoScene extends BaseScene {
    /**
     * 描述 门店Id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long shopId;

    /**
     * 描述 起始时间
     * 是否必填 true
     * 版本 v1.0
     */
    private final String startDate;

    /**
     * 描述 结束时间
     * 是否必填 true
     * 版本 v1.0
     */
    private final String endDate;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shopId", shopId);
        object.put("startDate", startDate);
        object.put("endDate", endDate);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/cashier/trace-back/batch-video";
    }
}