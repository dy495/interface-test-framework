package com.haisheng.framework.testng.bigScreen.xundian.scene.pc.integralmall;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.16. 商品规格列表 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-03-31 12:29:35
 */
@Builder
public class SpecificationsPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
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