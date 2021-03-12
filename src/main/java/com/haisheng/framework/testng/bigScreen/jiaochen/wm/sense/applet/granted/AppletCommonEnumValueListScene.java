package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/applet/granted/common/enum-value-list的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:53:03
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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("enum_type", enumType);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/common/enum-value-list";
    }
}