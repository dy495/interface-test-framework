package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/package-manage/search-customer的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class SearchCustomerScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String customerPhone;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("customer_phone", customerPhone);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/package-manage/search-customer";
    }
}