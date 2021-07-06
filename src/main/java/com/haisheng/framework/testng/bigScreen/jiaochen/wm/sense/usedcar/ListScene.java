package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.usedcar;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.1. 二手车商品列表 （华成裕）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class ListScene extends BaseScene {
    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 上次请求最后值
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONObject lastValue;

    /**
     * 描述 排序字段
     * 是否必填 false
     * 版本 v3.0
     */
    private final JSONObject orderCondition;

    /**
     * 描述 筛选过滤条件
     * 是否必填 false
     * 版本 -
     */
    private final JSONObject filterCondition;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("last_value", lastValue);
        object.put("order_condition", orderCondition);
        object.put("filter_condition", filterCondition);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/used-car/list";
    }
}