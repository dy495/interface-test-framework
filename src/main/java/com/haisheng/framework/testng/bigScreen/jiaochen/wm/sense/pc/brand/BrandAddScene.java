package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.brand;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 34.5. 新建品牌（谢）
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class BrandAddScene extends BaseScene {
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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("logoPath", logoPath);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/brand/add";
    }
}