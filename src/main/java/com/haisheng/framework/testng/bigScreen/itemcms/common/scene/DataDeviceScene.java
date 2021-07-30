package com.haisheng.framework.testng.bigScreen.itemcms.common.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class DataDeviceScene extends BaseScene {
    private final String name;
    private final String deviceType;
    private final String cloudSceneType;
    private final String tag;
    private final String url;
    private final String subUrl;
    private final String subjectId;
    private final String recordStreamUrl;
    private final String liveStreamUrl;
    private final String serialNumber;
    private final String manufacturer;
    private final Long deploymentGroupId;
    private final Long deploymentId;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("device_type", deviceType);
        object.put("cloud_scene_type", cloudSceneType);
        object.put("tag", tag);
        object.put("url", url);
        object.put("sub_url", subUrl);
        object.put("subject_id", subjectId);
        object.put("record_stream_url", recordStreamUrl);
        object.put("live_stream_url", liveStreamUrl);
        object.put("serial_number", serialNumber);
        object.put("manufacturer", manufacturer);
        object.put("deployment_group_id", deploymentGroupId);
        object.put("deployment_id", deploymentId);
        return object;
    }

    @Override
    public String getPath() {
        return "/admin/data/device/";
    }
}
