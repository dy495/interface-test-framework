package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.brand;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 26.5. 新建品牌（谢）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class AddScene extends BaseScene {
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


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("logoPath", logoPath);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/brand/add";
    }
}