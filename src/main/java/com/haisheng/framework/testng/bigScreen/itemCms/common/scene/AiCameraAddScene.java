package com.haisheng.framework.testng.bigScreen.itemCms.common.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 添加网络摄像头
 *
 * @author wangmin
 * @date 2021/08/03
 */
@Builder
public class AiCameraAddScene extends BaseScene {
    private final String subjectId;
    private final String name;
    private final String serialNumber;
    @Builder.Default
    private final String manufacturer = "HIKVISION";
    private final String model;
    private final String tag;
    private final String deviceUpgradePackageId;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("subject_id", subjectId);
        object.put("name", name);
        object.put("serial_number", serialNumber);
        object.put("manufacturer", manufacturer);
        object.put("model", model);
        object.put("tag", tag);
        object.put("device_upgrade_package_id", deviceUpgradePackageId);
        return object;
    }

    @Override
    public String getPath() {
        return "/admin/device/aiCamera/add";
    }
}
