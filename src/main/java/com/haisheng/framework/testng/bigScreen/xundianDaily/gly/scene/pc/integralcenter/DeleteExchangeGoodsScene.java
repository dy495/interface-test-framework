package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 56.9. 积分兑换删除
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class DeleteExchangeGoodsScene extends BaseScene {
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
     * 描述 唯一id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long goodsId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("goodsId", goodsId);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-center/delete-exchange-goods";
    }
}