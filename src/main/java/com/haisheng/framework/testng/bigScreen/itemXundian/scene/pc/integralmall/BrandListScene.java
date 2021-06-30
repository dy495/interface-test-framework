package com.haisheng.framework.testng.bigScreen.itemXundian.scene.pc.integralmall;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 41.13. 品牌列表
 *
 * @author wangmin
 * @date 2021-03-30 14:00:03
 */
@Builder
public class BrandListScene extends BaseScene {

    @Override
    public JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-mall/brand-list";
    }
}