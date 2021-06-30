package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.4. 新增/修改评价配置详情
 *
 * @author wangmin
 * @date 2021-06-01 19:09:09
 */
@Builder
public class EvaluateV4ConfigSubmitScene extends BaseScene {
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