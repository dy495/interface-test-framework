package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shop;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 33.3. 门店详情 （杨）（2021-03-23）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class DetailScene extends BaseScene {
    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v1.0
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
        return "/car-platform/pc/shop/detail";
    }
}