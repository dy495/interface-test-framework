package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 客流分析列表
 *
 * @author wangmin
 * @date 2021-03-30 14:00:03
 */
@Builder
public class ShopPagePassengerFlowScene extends BaseScene {
    @Builder.Default
    private Integer size = 10;
    @Builder.Default
    private Integer page = 1;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("page", page);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/enum-value-list";
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }
}