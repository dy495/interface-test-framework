package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 55.26. 上架/下架
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class ChangeGoodsStatusScene extends BaseScene {
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
     * 描述 商品id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 状态
     * 是否必填 true
     * 版本 v2.0
     */
    private final String status;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-mall/change-goods-status";
    }
}