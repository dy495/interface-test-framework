package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.4. 获取指定枚举值列表（谢）（2021-02-19）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletCommonEnumValueListScene extends BaseScene {
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
        return "/shop/applet/granted/common/enum-value-list";
    }
}