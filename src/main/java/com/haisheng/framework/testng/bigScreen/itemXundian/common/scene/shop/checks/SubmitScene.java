package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.shop.checks;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 36.15. 提交巡检结果
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class SubmitScene extends BaseScene {
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
     * 描述 门店id
     * 是否必填 true
     * 版本 -
     */
    private final Long shopId;

    /**
     * 描述 id
     * 是否必填 true
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 提交说明
     * 是否必填 false
     * 版本 -
     */
    private final String comment;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("shop_id", shopId);
        object.put("id", id);
        object.put("comment", comment);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/shop/checks/submit";
    }
}