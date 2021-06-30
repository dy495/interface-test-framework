package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 34.7. 权限员工列表 （谢） （2020-12-23）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class AuthListScene extends BaseScene {
    /**
     * 描述 权限类型枚举 各个使用的地方提供枚举值
     * 是否必填 true
     * 版本 v2.0
     */
    private final String authType;

    /**
     * 描述 门店id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long shopId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("auth_type", authType);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/staff/auth-list";
    }
}