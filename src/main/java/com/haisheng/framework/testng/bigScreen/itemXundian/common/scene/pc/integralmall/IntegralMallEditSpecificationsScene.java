package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 38.20. 修改商品规格
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class IntegralMallEditSpecificationsScene extends BaseScene {
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
     * 描述 规格id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 规格名称
     * 是否必填 true
     * 版本 v2.0
     */
    private final String specificationsName;

    /**
     * 描述 所属品类
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long belongsCategory;

    /**
     * 描述 品类列表
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray specificationsList;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("specifications_name", specificationsName);
        object.put("belongs_category", belongsCategory);
        object.put("specifications_list", specificationsList);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-mall/edit-specifications";
    }
}