package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.equipmentmanagement.device;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 29.1. 设备列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class DevicePageScene extends BaseScene {
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
    @Builder.Default
    private Integer page = 1;

    /**
     * 描述 当前页的数量
     * 是否必填 true
     * 版本 -
     */
    @Builder.Default
    private Integer size = 10;

    /**
     * 描述 设备名称
     * 是否必填 false
     * 版本 -
     */
    private final String deviceName;

    /**
     * 描述 所属门店
     * 是否必填 false
     * 版本 -
     */
    private final String shopName;

    /**
     * 描述 设备id
     * 是否必填 false
     * 版本 -
     */
    private final String deviceId;

    /**
     * 描述 状态
     * 是否必填 false
     * 版本 -
     */
    private final String deviceStatus;

    /**
     * 描述 设备类型CAMERA -("摄像头") SERVER -("服务器")
     * 是否必填 true
     * 版本 -
     */
    private final String type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("device_name", deviceName);
        object.put("shop_name", shopName);
        object.put("device_id", deviceId);
        object.put("device_status", deviceStatus);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/equipment-management/device/page";
    }


    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }
}