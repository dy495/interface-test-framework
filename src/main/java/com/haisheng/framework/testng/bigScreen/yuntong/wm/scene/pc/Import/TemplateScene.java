package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.Import;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 9.2. 导入模板下载
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class TemplateScene extends BaseScene {
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
        return "/yt/pc/import/template";
    }
}