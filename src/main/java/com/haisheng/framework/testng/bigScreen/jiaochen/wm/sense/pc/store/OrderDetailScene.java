package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.store;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/store/order/detail的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class OrderDetailScene extends BaseScene {
    /**
     * 描述 id 订单id
     * 是否必填 false
     * 版本 -
     */
    private final Long id;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/store/order/detail";
    }
}