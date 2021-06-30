package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 21.2. 获取指定枚举值列表（谢）（2021-02-19）
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class EnumValueListScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 枚举类型
     * 是否必填 true
     * 版本 v2.0
     */
    private final String enumType;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("enum_type", enumType);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/enum-value-list";
    }
}