package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/package-manage/invalid-customer-package的接口
 *
 * @author wangmin
 * @date 2021-03-15 10:12:39
 */
@Builder
public class InvalidCustomerPackageScene extends BaseScene {
    /**
     * 描述 唯一id
     * 是否必填 false
     * 版本 v2.2
     */
    private final Long id;

    /**
     * 描述 作废原因
     * 是否必填 false
     * 版本 v2.2
     */
    private final String reason;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("reason", reason);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/package-manage/invalid-customer-package";
    }
}