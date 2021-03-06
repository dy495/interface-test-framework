package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.integralmall;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.18. 修改规格状态 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-03-31 12:29:35
 */
@Builder
public class ChangeSpecificationsStatusScene extends BaseScene {
    /**
     * 描述 名称
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 状态
     * 是否必填 true
     * 版本 v2.0
     */
    private final Boolean status;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-mall/change-specifications-status";
    }
}