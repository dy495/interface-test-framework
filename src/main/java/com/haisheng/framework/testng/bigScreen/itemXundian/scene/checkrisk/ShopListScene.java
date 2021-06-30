package com.haisheng.framework.testng.bigScreen.itemXundian.scene.checkrisk;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 42.2. 巡店风控事件统计-门店列表 (list{{"中关村1号店"，4116}，{"中关村2号店"，4117}})
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class ShopListScene extends BaseScene {
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
        return "/patrol/check-risk/shop-list";
    }
}