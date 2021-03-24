package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.usedcar;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.2. 二手车商品详情（华成裕）的接口
 *
 * @author wangmin
 * @date 2021-03-24 14:46:43
 */
@Builder
public class DetailScene extends BaseScene {
    /**
     * 描述 商品ID
     * 是否必填 false
     * 版本 v5.3
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
        return "/jiaochen/used-car/detail";
    }
}