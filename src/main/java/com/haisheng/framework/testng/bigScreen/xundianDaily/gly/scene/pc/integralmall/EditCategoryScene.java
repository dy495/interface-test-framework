package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 55.7. 修改商品品类
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class EditCategoryScene extends BaseScene {
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

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String belongPicTempFullUrl;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("category_name", categoryName);
        object.put("category_level", categoryLevel);
        object.put("belong_category", belongCategory);
        object.put("belong_pic", belongPic);
        object.put("belong_pic_temp_full_url", belongPicTempFullUrl);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-mall/edit-category";
    }
}