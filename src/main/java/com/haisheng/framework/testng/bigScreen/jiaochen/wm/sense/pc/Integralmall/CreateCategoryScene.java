package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.2. 创建品类 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-03-31 12:29:35
 */
@Builder
public class CreateCategoryScene extends BaseScene {
    /**
     * 描述 品类id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 品类名称
     * 是否必填 true
     * 版本 v2.0
     */
    private final String categoryName;

    /**
     * 描述 品类级别
     * 是否必填 true
     * 版本 v2.0
     */
    private final String categoryLevel;

    /**
     * 描述 所属品类
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long belongCategory;

    /**
     * 描述 所属logo
     * 是否必填 true
     * 版本 v2.0
     */
    private final String belongPic;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("category_name", categoryName);
        object.put("category_level", categoryLevel);
        object.put("belong_category", belongCategory);
        object.put("belong_pic", belongPic);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/integral-mall/create-category";
    }
}