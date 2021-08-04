package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.personalcenter;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.2. app 我的核销记录分页（张小龙）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:23
 */
@Builder
public class AppWriteOffRecordsPageScene extends BaseScene {
    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 上次请求最后值
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONObject lastValue;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("last_value", lastValue);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/personal-center/write-off-records/page";
    }
}