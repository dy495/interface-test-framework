package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.receptionmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 23.3. 售后接待员工列表（谢）（2020-12-15）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class ReceptorListScene extends BaseScene {
    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long shopId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/reception-manage/receptor/list";
    }
}