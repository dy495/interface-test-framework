package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.store;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 20.6. 特惠商品上架或下架 v2.0(池)
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class CommodityUpOrDownScene extends BaseScene {
    /**
     * 描述 状态 UP 上架 DOWN 下架
     * 是否必填 false
     * 版本 v2.0
     */
    private final String status;

    /**
     * 描述 id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("status", status);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/store/commodity/up-or-down";
    }
}