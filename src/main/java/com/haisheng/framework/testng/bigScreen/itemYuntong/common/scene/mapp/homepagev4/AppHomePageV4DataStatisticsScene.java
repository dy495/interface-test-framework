package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.homepagev4;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.3. 统计数据 (刘) v5.0
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppHomePageV4DataStatisticsScene extends BaseScene {
    /**
     * 描述 选择统计时间（2021-07）
     * 是否必填 true
     * 版本 5.0
     */
    private final String date;

    /**
     * 描述 选择统计时间（2021-07）
     * 是否必填 true
     * 版本 5.0
     */
    private final String time;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("date", date);
        object.put("time", time);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/home-page-v4/data-statistics";
    }
}