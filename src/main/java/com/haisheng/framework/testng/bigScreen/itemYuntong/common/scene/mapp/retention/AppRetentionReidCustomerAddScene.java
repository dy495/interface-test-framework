package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.retention;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.5. 前台人脸-手动留资（华）v5.0（2021-07-09）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:24
 */
@Builder
public class AppRetentionReidCustomerAddScene extends BaseScene {
    /**
     * 描述 姓名
     * 是否必填 true
     * 版本 v5.0
     */
    private final String name;

    /**
     * 描述 手机号
     * 是否必填 false
     * 版本 v5.0
     */
    private final String phone;

    /**
     * 描述 车牌记录ID
     * 是否必填 false
     * 版本 v5.0
     */
    private final Long plateNumberRecordId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("phone", phone);
        object.put("plate_number_record_id", plateNumberRecordId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/retention/reid-customer-add";
    }
}