package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.shop.own;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 37.2. 门店列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class ListScene extends BaseScene {
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
     * 描述 区域编码
     * 是否必填 true
     * 版本 -
     */
    private final String districtCode;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("district_code", districtCode);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/shop/own/list";
    }
}