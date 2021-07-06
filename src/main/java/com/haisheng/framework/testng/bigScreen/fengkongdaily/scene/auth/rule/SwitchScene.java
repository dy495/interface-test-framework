package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.6. 风控规则开关
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class SwitchScene extends BaseScene {
    /**
     * 描述 规则Id
     * 是否必填 true
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 状态 1启动 0不启动
     * 是否必填 true
     * 版本 -
     */
    private final Integer status;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/rule/switch";
    }
}