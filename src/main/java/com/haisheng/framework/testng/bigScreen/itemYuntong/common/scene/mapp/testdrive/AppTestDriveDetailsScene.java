package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.testdrive;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 15.5. 试乘试驾展示详情（刘）v5.0
 *
 * @author wangmin
 * @date 2021-08-06 16:38:24
 */
@Builder
public class AppTestDriveDetailsScene extends BaseScene {
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
        return "/car-platform/m-app/test-drive/details";
    }
}