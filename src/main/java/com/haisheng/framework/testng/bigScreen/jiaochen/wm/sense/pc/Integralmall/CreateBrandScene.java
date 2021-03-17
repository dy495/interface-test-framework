package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/integral-mall/create-brand的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class CreateBrandScene extends BaseScene {
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


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("brand_name", brandName);
        object.put("brand_description", brandDescription);
        object.put("brand_pic", brandPic);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/integral-mall/create-brand";
    }
}