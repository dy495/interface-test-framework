package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.userange;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 44.2. 使用主体内容详情
 *
 * @author wangmin
 * @date 2021-07-27 18:26:29
 */
@Builder
public class UseRangeDetailScene extends BaseScene {
    /**
     * 描述 null[enum:]
     * 是否必填 false
     * 版本 -
     */
    private final String subjectKey;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("subject_key", subjectKey);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/use-range/detail";
    }
}