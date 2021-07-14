package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.equipmentmanagement.auth;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 30.6. 获取权限下门店列表对应设备列表的树状结构
 *
 * @author wangmin
 * @date 2021-07-14 14:30:21
 */
@Builder
public class AllDeviceListScene extends BaseScene {
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
     * 描述 筛选摄像头状态 null:全部 0:不可用，1:可用
     * 是否必填 false
     * 版本 -
     */
    private final Integer available;

    /**
     * 描述 门店名称
     * 是否必填 false
     * 版本 -
     */
    private final String shopName;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("available", available);
        object.put("shop_name", shopName);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/equipment-management/auth/all-device/list";
    }
}