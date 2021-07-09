package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.integralmall;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 26.13. 商品品类详情 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class BrandDetailScene extends BaseScene {
    /**
     * 描述 唯一id
     * 是否必填 true
     * 版本 v2.0
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
        return "/account-platform/auth/integral-mall/brand-detail";
    }
}