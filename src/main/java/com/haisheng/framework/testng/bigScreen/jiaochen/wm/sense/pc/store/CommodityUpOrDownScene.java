package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.store;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.6. 特惠商品上架或下架 v2.0(池)
 *
 * @author wangmin
 * @date 2021-03-31 12:47:26
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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("status", status);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/store/commodity/up-or-down";
    }
}