package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.banner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 32.1. 修改banner详情
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class BannerEditScene extends BaseScene {
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