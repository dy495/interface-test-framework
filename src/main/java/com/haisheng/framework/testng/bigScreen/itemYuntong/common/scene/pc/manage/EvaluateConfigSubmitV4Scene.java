package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.4. 新增/修改评价配置详情（池）v4.0（2021-05-06）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class EvaluateConfigSubmitV4Scene extends BaseScene {
    /**
     * 描述 评价类型 枚举见字典表《评价类型》
     * 是否必填 true
     * 版本 v4.0
     */
    private final Integer type;

    /**
     * 描述 评价详情
     * 是否必填 false
     * 版本 v4.0
     */
    private final JSONArray evaluateConfigs;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("evaluateConfigs", evaluateConfigs);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/manage/evaluate/config/submit-v4";
    }
}