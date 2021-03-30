package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 42.24. 积分兑换规则设置
 *
 * @author wangmin
 * @date 2021-03-30 14:00:03
 */
@Builder
public class IntegralExchangeRulesScene extends BaseScene {
    /**
     * 描述 当前页
     * 是否必填 true
     * 版本 -
     */
    private final Integer page;

    /**
     * 描述 当前页的数量
     * 是否必填 true
     * 版本 -
     */
    private final Integer size;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-center/integral-exchange-rules";
    }
}