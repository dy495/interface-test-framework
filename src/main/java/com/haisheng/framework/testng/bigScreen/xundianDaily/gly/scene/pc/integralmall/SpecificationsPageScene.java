package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 55.16. 商品规格列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class SpecificationsPageScene extends BaseScene {
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
     * 描述 当前页
     * 是否必填 true
     * 版本 -
     */
    private final Integer page;

    /**
     * 描述 当前页的数量
     * 是否必填 true
     * 版本 -
     */
    private final Integer size;

    /**
     * 描述 规格名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String specificationsName;

    /**
     * 描述 一级品类
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long firstCategory;

    /**
     * 描述 状态
     * 是否必填 false
     * 版本 v2.0
     */
    private final Boolean specificationsStatus;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("specifications_name", specificationsName);
        object.put("first_category", firstCategory);
        object.put("specifications_status", specificationsStatus);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-mall/specifications-page";
    }
}