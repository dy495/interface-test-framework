package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.20. 修改商品规格 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-03-31 12:29:35
 */
@Builder
public class EditSpecificationsScene extends BaseScene {
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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
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