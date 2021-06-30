package com.haisheng.framework.testng.bigScreen.itemXundian.scene.shop.device;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 36.11. 合并拉流心跳接口
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class MergeHeartbeatScene extends BaseScene {
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
    private final Long shopId;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray deviceIdList;

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
        object.put("shop_id", shopId);
        object.put("device_id_list", deviceIdList);
        object.put("date", date);
        object.put("time", time);
        object.put("isMobile", isMobile);
        object.put("remoteIp", remoteIp);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/shop/device/merge-heartbeat";
    }
}