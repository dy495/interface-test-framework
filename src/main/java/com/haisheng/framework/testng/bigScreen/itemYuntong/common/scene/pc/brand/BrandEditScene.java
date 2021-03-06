package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.brand;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.6. 编辑品牌（谢）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class BrandEditScene extends BaseScene {
    /**
     * 描述 品牌名称
     * 是否必填 true
     * 版本 v1.0
     */
    private final String name;

    /**
     * 描述 品牌logo oss路径
     * 是否必填 true
     * 版本 v1.0
     */
    private final String logoPath;

    /**
     * 描述 品牌id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("logoPath", logoPath);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/brand/edit";
    }
}