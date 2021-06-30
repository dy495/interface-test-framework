package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.riskcontrol.rule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 48.6. 添加黑白名单-操作日志列表(黑白名单共用)
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class BlackWhiteListOperatePageScene extends BaseScene {
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
     * 描述 人物id
     * 是否必填 true
     * 版本 门店 v4.1
     */
    private final String customerId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("customer_id", customerId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/risk-control/rule/black-white-list/operate/page";
    }
}