package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.shopstylemodel;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 11.3. 保养、维修车型配置更新，替换"原保养配置更新"（谢）V3.0（2020-12-18）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class ManageModelEditScene extends BaseScene {
    /**
     * 描述 预约类型 MAINTAIN：保养，REPAIR：维修
     * 是否必填 true
     * 版本 v3.0
     */
    private final String type;

    /**
     * 描述 车型id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;

    /**
     * 描述 保养价格
     * 是否必填 false
     * 版本 v1.0
     */
    private final Double price;

    /**
     * 描述 预约状态 ENABLE：开启，DISABLE：关闭
     * 是否必填 false
     * 版本 v1.0
     */
    private final String status;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("id", id);
        object.put("price", price);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/shop-style-model/manage/model/edit";
    }
}