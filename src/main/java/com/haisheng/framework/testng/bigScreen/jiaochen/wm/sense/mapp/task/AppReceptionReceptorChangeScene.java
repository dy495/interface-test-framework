package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/m-app/task/reception/receptor/change的接口
 *
 * @author wangmin
 * @date 2021-03-12 18:09:47
 */
@Builder
public class AppReceptionReceptorChangeScene extends BaseScene {
    /**
     * 描述 接待id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long shopId;

    /**
     * 描述 售后接待员工id
     * 是否必填 true
     * 版本 v2.0
     */
    private final String receptorId;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shop_id", shopId);
        object.put("receptor_id", receptorId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/task/reception/receptor/change";
    }
}