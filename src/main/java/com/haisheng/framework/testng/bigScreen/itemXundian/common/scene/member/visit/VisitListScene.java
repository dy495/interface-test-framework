package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.member.visit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 41.7. 会员到访列表
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class VisitListScene extends BaseScene {
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
     * 描述 人物id
     * 是否必填 false
     * 版本 -
     */
    private final String userId;

    /**
     * 描述 会员名称
     * 是否必填 false
     * 版本 -
     */
    private final String name;

    /**
     * 描述 首次到店
     * 是否必填 false
     * 版本 -
     */
    private final String shopName;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("user_id", userId);
        object.put("name", name);
        object.put("shop_name", shopName);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/member/visit/list";
    }
}