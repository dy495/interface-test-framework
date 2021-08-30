package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.testdrivecar;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 27.4. 注销车辆 (刘) v5.0
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class TestDriveCarDelCarScene extends BaseScene {
    /**
     * 描述 唯一id
     * 是否必填 true
     * 版本 5.0
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/test-drive-car/del-car";
    }
}