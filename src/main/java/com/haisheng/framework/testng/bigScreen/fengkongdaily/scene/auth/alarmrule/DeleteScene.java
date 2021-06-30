package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.5. 删除风控告警
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class DeleteScene extends BaseScene {
    /**
     * 描述 告警规则Id
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
        return "/risk-control/auth/alarm-rule/delete";
    }
}