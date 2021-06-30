package com.haisheng.framework.testng.bigScreen.itemXundian.scene.shop;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 19.7. 更新门店状态
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class UpdateStatusScene extends BaseScene {
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
    private final Integer id;

    /**
     * 描述 门店状态
     * 是否必填 true
     * 版本 -
     */
    private final Boolean isShow;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("is_show", isShow);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/shop/update-status";
    }
}