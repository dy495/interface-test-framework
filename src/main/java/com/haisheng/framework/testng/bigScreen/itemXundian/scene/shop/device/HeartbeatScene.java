package com.haisheng.framework.testng.bigScreen.itemXundian.scene.shop.device;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 36.10. 拉流心跳接口
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class HeartbeatScene extends BaseScene {
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
     * 描述 设备id
     * 是否必填 true
     * 版本 -
     */
    private final String deviceId;

    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 -
     */
    private final Long shopId;

    /**
     * 描述 日期
     * 是否必填 true
     * 版本 -
     */
    private final String date;

    /**
     * 描述 重播开始时间
     * 是否必填 true
     * 版本 -
     */
    private final String time;

    /**
     * 描述 录播查看时长，不指定的话，底层支撑服务默认为10分钟 单位为秒，最短时长不得少于
     * 是否必填 false
     * 版本 -
     */
    private final Long duration;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("device_id", deviceId);
        object.put("shop_id", shopId);
        object.put("date", date);
        object.put("time", time);
        object.put("duration", duration);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/shop/device/heartbeat";
    }
}