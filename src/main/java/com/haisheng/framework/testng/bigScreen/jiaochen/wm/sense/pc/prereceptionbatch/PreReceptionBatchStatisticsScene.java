package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.prereceptionbatch;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 20.1. 批次数据统计 （华）（2021-07-12）
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class PreReceptionBatchStatisticsScene extends BaseScene {
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
     * 描述 查询日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String day;

    /**
     * 描述 查询月份
     * 是否必填 false
     * 版本 v1.0
     */
    private final String month;

    /**
     * 描述 查询日期类型
     * 是否必填 false
     * 版本 v1.0
     */
    private final String dayType;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("day", day);
        object.put("month", month);
        object.put("day_type", dayType);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/pre-reception-batch/statistics";
    }
}