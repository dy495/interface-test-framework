package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shop;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 33.1. 门店列表分页 （杨）（2021-03-23）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class PageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 门店名称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String name;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("name", name);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/shop/page";
    }
}