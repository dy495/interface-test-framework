package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.devicemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 19.1. 日志文件上传
 *
 * @author wangmin
 * @date 2021-08-06 16:38:24
 */
@Builder
public class AppDeviceManageLogUploadScene extends BaseScene {
    /**
     * 描述 设备的唯一标识
     * 是否必填 true
     * 版本 v1.0
     */
    private final String deviceUuid;

    /**
     * 描述 设备的国际移动设备身份码
     * 是否必填 false
     * 版本 v1.0
     */
    private final String deviceImei;

    /**
     * 描述 设备的国际移动用户识别码
     * 是否必填 false
     * 版本 v1.0
     */
    private final String deviceImsi;

    /**
     * 描述 设备的型号
     * 是否必填 false
     * 版本 v1.0
     */
    private final String deviceModel;

    /**
     * 描述 设备的生产厂商
     * 是否必填 false
     * 版本 v1.0
     */
    private final String deviceVendor;

    /**
     * 描述 操作系统版本
     * 是否必填 false
     * 版本 v1.0
     */
    private final String deviceVersion;

    /**
     * 描述 其他信息，使用JSON格式存储
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONObject otherInfo;

    /**
     * 描述 日志压缩文件base64
     * 是否必填 true
     * 版本 v1.0
     */
    private final String base64;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("device_uuid", deviceUuid);
        object.put("device_imei", deviceImei);
        object.put("device_imsi", deviceImsi);
        object.put("device_model", deviceModel);
        object.put("device_vendor", deviceVendor);
        object.put("device_version", deviceVersion);
        object.put("other_info", otherInfo);
        object.put("base64", base64);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/device-manage/log-upload";
    }
}