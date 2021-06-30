package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.homepage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.2. 今日数据 (池) v1.0
 *
 * @author wangmin
 * @date 2021-03-31 13:03:23
 */
@Builder
public class AppTodayDataScene extends BaseScene {
    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 上次请求最后值
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONObject lastValue;

    /**
     * 描述 是否全部数据
     * 是否必填 false
     * 版本 v1.0
     */
    private final String type;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("last_value", lastValue);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/home-page/today-data";
    }
}