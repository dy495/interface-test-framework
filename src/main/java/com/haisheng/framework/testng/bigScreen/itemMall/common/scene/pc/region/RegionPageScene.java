package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.region;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.1. 场景区域管理分页列表
 *
 * @author wangmin
 * @date 2021-09-01 17:31:17
 */
@Builder
public class RegionPageScene extends BaseScene {
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
    private final String regionName;

    /**
     * 描述 购物中心id
     * 是否必填 true
     * 版本 -
     */
    private final Long mallId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("page", page);
        object.put("size", size);
        object.put("region_name", regionName);
        object.put("mall_id", mallId);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/pc/region/page";
    }
}