package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 38.24. 积分兑换规则设置 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class IntegralExchangeRulesScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/integral-center/integral-exchange-rules";
    }
}