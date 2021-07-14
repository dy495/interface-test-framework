package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.homepagev4;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.2. 今日数据
 *
 * @author wangmin
 * @date 2021-05-07 19:22:48
 */
@Builder
public class AppTodayDataScene extends BaseScene {
    /**
     * 描述 查询数据周期 取值见字典表《数据查询周期》
     * 是否必填 true
     * 版本 v1.0
     */
    private final String type;

    /**
     * 描述 自定义查询开始日期，周期类型为自定义时必填
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONObject lastValue;

    /**
     * 描述 自定义查询结束日期，周期类型为自定义时必填
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer size;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("last_value", lastValue);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/home-page-v4/today-data";
    }
}