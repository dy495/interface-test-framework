package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.brand;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.8. 品牌车系分页（谢）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class CarStylePageScene extends BaseScene {
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
     * 描述 品牌id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long brandId;

    /**
     * 描述 品牌名称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String name;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("brand_id", brandId);
        object.put("name", name);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/brand/car-style/page";
    }
}