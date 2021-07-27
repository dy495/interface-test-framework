package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 23.4. 评价配置详情（谢）v3.0（2021-03-12）
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class ConfigDetailScene extends BaseScene {
    /**
     * 描述 评价类型 枚举见字典表《评价类型》
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/manage/evaluate/config/detail";
    }
}