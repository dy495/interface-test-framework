package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.realtime;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.7. 楼层到访趋势
 *
 * @author wangmin
 * @date 2021-07-28 16:58:57
 */
@Builder
public class RealTimeFloorVisitTrendScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 类型（VISIT到访，FLOOR_ENTER爬楼）
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
        return "/mall/visit-trend/real-time/floor-visit-trend";
    }
}