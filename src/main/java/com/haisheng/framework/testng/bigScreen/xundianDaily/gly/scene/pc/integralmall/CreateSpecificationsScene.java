package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 55.17. 创建商品规格
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class CreateSpecificationsScene extends BaseScene {
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
        return "/shop/pc/integral-mall/create-specifications";
    }
}