package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.realtime;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.4. 实时全场到访趋势折线图
 *
 * @author wangmin
 * @date 2021-07-28 16:58:57
 */
@Builder
public class FullCourtTrendScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 到访趋势类型 人数='UV' 人次='PV'
     * 是否必填 false
     * 版本 -
     */
    private final String type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/visit-trend/real-time/full-court/trend";
    }
}