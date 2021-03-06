package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.querycycle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.1. 查询周期列表
 *
 * @author wangmin
 * @date 2021-08-06 17:47:04
 */
@Builder
public class QueryCycleListScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/query-cycle/list";
    }
}