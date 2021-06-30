package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.banner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 15.2. banner列表（id=广告位id）
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class ListScene extends BaseScene {
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
     * 描述 广告位类型
     * 是否必填 false
     * 版本 -
     */
    private final String adType;

    /**
     * 描述 id
     * 是否必填 false
     * 版本 -
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("ad_type", adType);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/pc/banner/list";
    }
}