package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.integralmall;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 26.4. 所属品类列表 (张小龙) (2020-12-28)
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class BelongsCategoryScene extends BaseScene {
    /**
     * 描述 品类级别
     * 是否必填 false
     * 版本 v2.0
     */
    private final String categoryLevel;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("category_level", categoryLevel);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/integral-mall/belongs-category";
    }
}