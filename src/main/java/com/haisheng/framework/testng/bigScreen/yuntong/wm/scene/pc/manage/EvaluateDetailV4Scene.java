package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.3. 评价详情
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class EvaluateDetailV4Scene extends BaseScene {
    /**
     * 描述 评价id
     * 是否必填 false
     * 版本 v4.0
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/manage/evaluate/detail-v4";
    }
}