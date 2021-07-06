package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.userange;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 39.2. 使用主体内容详情
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class DetailScene extends BaseScene {
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
        return "/yt/pc/use-range/detail";
    }
}