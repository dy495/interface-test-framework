package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 21.3. 接待详情（谢）v3.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class DetailScene extends BaseScene {
    /**
     * 描述 接待id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long id;

    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long shopId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/pre-sales-reception/detail";
    }
}