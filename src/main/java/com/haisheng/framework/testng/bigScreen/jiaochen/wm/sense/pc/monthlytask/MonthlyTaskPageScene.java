package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.monthlytask;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 15.4. 月度任务分页列表 v5.0 （张）
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class MonthlyTaskPageScene extends BaseScene {
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
     * 描述 门店id
     * 是否必填 false
     * 版本 v5.0
     */
    private final Long shopId;

    /**
     * 描述 销售id
     * 是否必填 false
     * 版本 v5.0
     */
    private final String saleId;

    /**
     * 描述 月份
     * 是否必填 false
     * 版本 v5.0
     */
    private final String month;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("shop_id", shopId);
        object.put("sale_id", saleId);
        object.put("month", month);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/monthly-task/page";
    }
}