package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.schedulecheck;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 40.7. 编辑定检任务
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class EditScene extends BaseScene {
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
     * 描述 id
     * 是否必填 true
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 任务名称
     * 是否必填 false
     * 版本 -
     */
    private final String name;

    /**
     * 描述 任务周期
     * 是否必填 false
     * 版本 -
     */
    private final String cycle;

    /**
     * 描述 任务推送周期
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray dates;

    /**
     * 描述 推送时间
     * 是否必填 false
     * 版本 -
     */
    private final String sendTime;

    /**
     * 描述 有效开始时间
     * 是否必填 false
     * 版本 -
     */
    private final String validStart;

    /**
     * 描述 有效结束时间
     * 是否必填 false
     * 版本 -
     */
    private final String validEnd;

    /**
     * 描述 巡检员id
     * 是否必填 false
     * 版本 -
     */
    private final String inspectorId;

    /**
     * 描述 门店id列表
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray shopList;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("name", name);
        object.put("cycle", cycle);
        object.put("dates", dates);
        object.put("send_time", sendTime);
        object.put("valid_start", validStart);
        object.put("valid_end", validEnd);
        object.put("inspector_id", inspectorId);
        object.put("shop_list", shopList);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/schedule-check/edit";
    }
}