package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 55.14. 修改商品品牌
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class EditBrandScene extends BaseScene {
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
     * 描述 品牌id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 品牌名称
     * 是否必填 true
     * 版本 v2.0
     */
    private final String brandName;

    /**
     * 描述 品牌说明
     * 是否必填 false
     * 版本 v2.0
     */
    private final String brandDescription;

    /**
     * 描述 品牌logo
     * 是否必填 false
     * 版本 v2.0
     */
    private final String brandPic;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String brandPicTempFullUrl;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("brand_name", brandName);
        object.put("brand_description", brandDescription);
        object.put("brand_pic", brandPic);
        object.put("brand_pic_temp_full_url", brandPicTempFullUrl);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-mall/edit-brand";
    }
}