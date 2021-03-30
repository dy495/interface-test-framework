package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 41.9. 商品品牌分页
 *
 * @author wangmin
 * @date 2021-03-30 14:00:03
 */
@Builder
public class BrandPageScene extends BaseScene {
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
     * 描述 品牌名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String brandName;

    /**
     * 描述 品牌状态
     * 是否必填 false
     * 版本 v2.0
     */
    private final Boolean brandStatus;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("brand_name", brandName);
        object.put("brand_status", brandStatus);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-mall/brand-page";
    }
}