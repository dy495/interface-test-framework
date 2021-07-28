package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.realtime;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.8. 柱状图排名通用接口
 *
 * @author wangmin
 * @date 2021-07-28 16:58:57
 */
@Builder
public class RealTimeColumnarRankingScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 排名类型（PASS过店人数，ENTRY进店人数，ENTRY_PERCENTAGE进店率，VISIT_PERCENTAGE光顾率）
     * 是否必填 false
     * 版本 -
     */
    private final String type;

    /**
     * 描述 排名顺序（TOP_FIVE前五，LAST_FIVE后五）
     * 是否必填 false
     * 版本 -
     */
    private final String sort;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("type", type);
        object.put("sort", sort);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/visit-trend/real-time/columnar-ranking";
    }
}