package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 38.4. 所属品类列表
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class IntegralMallBelongsCategoryScene extends BaseScene {
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
     * 描述 品类级别
     * 是否必填 false
     * 版本 v2.0
     */
    private final String categoryLevel;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("category_level", categoryLevel);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-mall/belongs-category";
    }
}