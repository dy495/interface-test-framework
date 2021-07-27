package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.Import;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 12.2. 导入模板下载
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class ImportTemplateScene extends BaseScene {
    /**
     * 描述 模板类型 AFTER_CUSTOMER 售后工单 v1.0 POTENTIAL 潜客模板 v2.0 BUY_CAR 购车模板v3.0
     * 是否必填 true
     * 版本 v1.0
     */
    private final String type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/import/template";
    }
}