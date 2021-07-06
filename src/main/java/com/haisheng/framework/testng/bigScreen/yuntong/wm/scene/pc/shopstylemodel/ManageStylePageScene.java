package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.shopstylemodel;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 11.4. 试驾车系配置分页（谢）V3.0（2021-03-22）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class ManageStylePageScene extends BaseScene {
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
     * 描述 品牌
     * 是否必填 false
     * 版本 v3.0
     */
    private final String brandName;

    /**
     * 描述 生产商
     * 是否必填 false
     * 版本 v3.0
     */
    private final String manufacturer;

    /**
     * 描述 车系
     * 是否必填 false
     * 版本 v3.0
     */
    private final String carStyle;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("brand_name", brandName);
        object.put("manufacturer", manufacturer);
        object.put("car_style", carStyle);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/shop-style-model/manage/style/page";
    }
}