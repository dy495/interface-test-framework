package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.shopstylemodel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 14.6. 试驾车系配置更新（谢）V3.0（2020-12-18）
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class StyleEditScene extends BaseScene {
    /**
     * 描述 车系id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long id;

    /**
     * 描述 预约状态 ENABLE：开启，DISABLE：关闭
     * 是否必填 true
     * 版本 v3.0
     */
    private final String status;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/shop-style-model/manage/style/edit";
    }
}