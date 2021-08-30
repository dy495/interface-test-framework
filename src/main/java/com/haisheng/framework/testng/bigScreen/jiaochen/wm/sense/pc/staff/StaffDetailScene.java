package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 42.8. 员工详情 （杨）（2021-03-23） v3.0
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class StaffDetailScene extends BaseScene {
    /**
     * 描述 账号id
     * 是否必填 true
     * 版本 v2.0
     */
    private final String id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/staff/detail";
    }
}