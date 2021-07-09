package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.4. 评价配置详情
 *
 * @author wangmin
 * @date 2021-06-01 19:09:09
 */
@Builder
public class EvaluateV4ConfigDetailScene extends BaseScene {

    @Override
    protected JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/manage/evaluate/v4/config/detail";
    }
}