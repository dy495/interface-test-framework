package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.2. 获取指定枚举值列表（谢）（2021-02-19）
 *
 * @author wangmin
 * @date 2021-08-06 17:47:04
 */
@Builder
public class PcEnumValueListScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

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
        object.put("enum_type", enumType);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/enum-value-list";
    }
}