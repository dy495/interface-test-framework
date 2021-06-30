package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.banner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 15.1. 修改banner详情
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class EditScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray list;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String adName;

    /**
     * 描述 广告位类型
     * 是否必填 false
     * 版本 -
     */
    private final String adType;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("list", list);
        object.put("ad_name", adName);
        object.put("ad_type", adType);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/pc/banner/edit";
    }
}