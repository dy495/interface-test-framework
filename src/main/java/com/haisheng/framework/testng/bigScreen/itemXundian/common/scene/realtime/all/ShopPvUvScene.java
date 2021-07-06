package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.realtime.all;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.8. 门店pc 小时级别实时客流pv & uv-多店
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class ShopPvUvScene extends BaseScene {
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


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/real-time/all/shop/pv-uv";
    }
}