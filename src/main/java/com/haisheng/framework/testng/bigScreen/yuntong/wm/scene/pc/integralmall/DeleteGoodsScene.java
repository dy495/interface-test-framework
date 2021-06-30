package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.integralmall;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 26.30. 删除商品 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class DeleteGoodsScene extends BaseScene {
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
        return "/yt/pc/integral-mall/delete-goods";
    }
}