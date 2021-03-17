package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/manage/maintain/car-model/edit的接口
 *
 * @author wangmin
 * @date 2021-03-15 10:12:39
 */
@Builder
public class MaintainCarModelEditScene extends BaseScene {
    /**
     * 描述 车型id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;

    /**
     * 描述 保养价格
     * 是否必填 false
     * 版本 v1.0
     */
    private final Double price;

    /**
     * 描述 保养预约状态 ENABLE：开启，DISABLE：关闭
     * 是否必填 false
     * 版本 v1.0
     */
    private final String status;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("price", price);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/manage/maintain/car-model/edit";
    }
}