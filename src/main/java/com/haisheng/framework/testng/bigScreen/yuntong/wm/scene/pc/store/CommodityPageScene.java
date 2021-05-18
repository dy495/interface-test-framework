package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.store;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 20.1. 特惠商品 v2.0(池)
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class CommodityPageScene extends BaseScene {
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
     * 描述 商品名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String commodityName;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String startCreateDate;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String endCreateDate;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("commodity_name", commodityName);
        object.put("start_create_date", startCreateDate);
        object.put("end_create_date", endCreateDate);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/store/commodity/page";
    }
}