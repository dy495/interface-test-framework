package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.shop.device;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 22.12. 合并拉流心跳接口
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class DeviceMergeHeartbeatV2Scene extends BaseScene {
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
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray deviceMergeList;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String date;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String time;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Boolean isMobile;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String remoteIp;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("device_merge_list", deviceMergeList);
        object.put("date", date);
        object.put("time", time);
        object.put("isMobile", isMobile);
        object.put("remoteIp", remoteIp);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/shop/device/merge-heartbeat-v2";
    }
}