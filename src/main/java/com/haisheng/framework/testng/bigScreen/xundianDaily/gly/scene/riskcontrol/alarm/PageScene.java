package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.riskcontrol.alarm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 53.1. 风控告警事件列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class PageScene extends BaseScene {
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
     * 描述 风控告警规则
     * 是否必填 false
     * 版本 -
     */
    private final String name;

    /**
     * 描述 风控告警类型
     * 是否必填 false
     * 版本 -
     */
    private final String type;

    /**
     * 描述 门店名称
     * 是否必填 false
     * 版本 -
     */
    private final String shopName;

    /**
     * 描述 接受角色
     * 是否必填 false
     * 版本 -
     */
    private final String acceptRole;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("name", name);
        object.put("type", type);
        object.put("shop_name", shopName);
        object.put("accept_role", acceptRole);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/risk-control/alarm/page";
    }
}