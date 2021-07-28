package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.1. 门店分页列表
 *
 * @author wangmin
 * @date 2021-07-28 16:58:57
 */
@Builder
public class ShopPageScene extends BaseScene {
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
     * 描述 开始时间
     * 是否必填 false
     * 版本 -
     */
    private final String startTime;

    /**
     * 描述 结束时间
     * 是否必填 false
     * 版本 -
     */
    private final String endTime;

    /**
     * 描述 楼层id
     * 是否必填 false
     * 版本 -
     */
    private final Long floorId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("page", page);
        object.put("size", size);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("floor_id", floorId);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/pc/shop/page";
    }
}