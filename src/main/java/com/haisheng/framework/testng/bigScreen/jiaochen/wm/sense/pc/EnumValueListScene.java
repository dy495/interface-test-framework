package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 27.2. 获取指定枚举值列表（谢）（2021-02-19）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("enum_type", enumType);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/enum-value-list";
    }
}