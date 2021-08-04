package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.2. 获取指定枚举值列表（谢）（2021-02-19）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:23
 */
@Builder
public class AppEnumValueListScene extends BaseScene {
    /**
     * 描述 枚举类型
     * 是否必填 true
     * 版本 v2.0
     */
    private final String enumType;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("enum_type", enumType);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/enum-value-list";
    }
}