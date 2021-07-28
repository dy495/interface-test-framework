package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.Import;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.6. 导入模板
 *
 * @author wangmin
 * @date 2021-07-28 16:58:57
 */
@Builder
public class ImportTemplateScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 模板类型 AFTER_CUSTOMER 售后工单 v1.0 POTENTIAL_CUSTOMER 潜客模板 v2.0
     * 是否必填 true
     * 版本 v1.0
     */
    private final String type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/import/template";
    }
}