package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.Import;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/import/template的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class TemplateScene extends BaseScene {
    /**
     * 描述 模板类型 AFTER_CUSTOMER 售后工单 v1.0 POTENTIAL_CUSTOMER 潜客模板 v2.0
     * 是否必填 true
     * 版本 v1.0
     */
    private final String type;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/import/template";
    }
}