package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.passengerflow;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.1. 会员(客流)list
 *
 * @author wangmin
 * @date 2021-08-24 14:07:10
 */
@Builder
public class PassengerFlowListScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

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
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String eventDay;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("page", page);
        object.put("size", size);
        object.put("eventDay", eventDay);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/passenger-flow/list";
    }
}