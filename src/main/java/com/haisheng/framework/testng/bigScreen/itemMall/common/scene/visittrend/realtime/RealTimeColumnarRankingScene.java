package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.realtime;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.7. 柱状图排名通用接口
 *
 * @author wangmin
 * @date 2021-08-06 17:47:04
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
     * 描述 购物中心id
     * 是否必填 true
     * 版本 -
     */
    private final Long mallId;

    /**
     * 描述 排名类型（PASS过店人数，ENTRY进店人数，ENTRY_PERCENTAGE进店率，VISIT_PERCENTAGE光顾率）
     * 是否必填 false
     * 版本 -
     */
    private final String type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("mall_id", mallId);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/visit-trend/real-time/columnar-ranking";
    }
}