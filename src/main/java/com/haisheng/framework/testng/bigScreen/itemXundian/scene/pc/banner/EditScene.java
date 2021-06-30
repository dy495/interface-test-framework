package com.haisheng.framework.testng.bigScreen.itemXundian.scene.pc.banner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.1. 修改banner详情
 *
 * @author wangmin
 * @date 2021-03-30 14:00:02
 */
@Builder
public class EditScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray list;

    private final String adName;
    private final String adType;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("list", list);
        object.put("ad_name",adName);
        object.put("ad_type",adType);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/pc/banner/edit";
    }
}