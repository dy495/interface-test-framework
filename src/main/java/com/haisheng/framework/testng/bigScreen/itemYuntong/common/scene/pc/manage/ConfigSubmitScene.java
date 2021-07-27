package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.4. 新增/修改评价配置详情
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class ConfigSubmitScene extends BaseScene {
    /**
     * 描述 环节列表
     * 是否必填 true
     * 版本 v1.0
     */
    private final JSONArray links;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("links", links);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/manage/evaluate/v4/config/submit";
    }
}