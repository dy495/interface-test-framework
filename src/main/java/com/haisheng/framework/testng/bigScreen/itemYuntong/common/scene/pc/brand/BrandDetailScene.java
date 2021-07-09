package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.brand;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.4. 品牌详情（谢）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class BrandDetailScene extends BaseScene {
    /**
     * 描述 品牌id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/brand/detail";
    }
}