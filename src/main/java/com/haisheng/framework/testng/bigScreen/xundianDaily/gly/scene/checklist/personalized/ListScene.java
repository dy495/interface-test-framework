package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.checklist.personalized;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 39.1. 个性化配置清单-列表
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
     * 描述 门店id
     * 是否必填 false
     * 版本 -
     */
    private final Long shopId;

    /**
     * 描述 巡店方式
     * 是否必填 false
     * 版本 -
     */
    private final String checkType;

    /**
     * 描述 定检巡店任务id
     * 是否必填 false
     * 版本 -
     */
    private final Long taskId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("shop_id", shopId);
        object.put("check_type", checkType);
        object.put("task_id", taskId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/check-list/personalized/list";
    }
}