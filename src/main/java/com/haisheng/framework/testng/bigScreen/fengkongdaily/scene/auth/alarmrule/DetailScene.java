package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.3. 风控告警规则详情
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class DetailScene extends BaseScene {
    /**
     * 描述 规则Id
     * 是否必填 true
     * 版本 -
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
        return "/risk-control/auth/alarm-rule/detail";
    }
}