package com.haisheng.framework.testng.bigScreen.xundian.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 16.2. 获取指定枚举值列表
 *
 * @author wangmin
 * @date 2021-03-30 14:00:03
 */
@Builder
public class EnumValueListScene extends BaseScene {
    /**
     * 描述 枚举类型
     * 是否必填 true
     * 版本 v2.0
     */
    private final String enumType;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("enum_type", enumType);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/enum-value-list";
    }
}