package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralmall;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.13. 商品品类详情 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-03-31 12:29:35
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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/integral-mall/brand-detail";
    }
}