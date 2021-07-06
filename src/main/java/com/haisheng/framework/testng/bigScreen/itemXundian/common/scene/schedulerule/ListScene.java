package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.schedulerule;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 43.3. 定检规则列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
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
     * 描述 规则名称
     * 是否必填 false
     * 版本 -
     */
    private final String name;

    /**
     * 描述 门店名称
     * 是否必填 false
     * 版本 -
     */
    private final String shopName;

    /**
     * 描述 定检规则状态
     * 是否必填 false
     * 版本 -
     */
    private final Integer status;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("name", name);
        object.put("shop_name", shopName);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/schedule-rule/list";
    }
}