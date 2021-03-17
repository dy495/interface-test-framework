package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/manage/evaluate/config/detail的接口
 *
 * @author wangmin
 * @date 2021-03-15 10:12:39
 */
@Builder
public class EvaluateConfigDetailScene extends BaseScene {
    /**
     * 描述 评价类型 1：预约评价，2：新车评价
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer type;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/manage/evaluate/config/detail";
    }
}