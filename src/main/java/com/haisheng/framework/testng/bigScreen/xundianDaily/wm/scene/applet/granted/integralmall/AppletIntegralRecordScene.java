package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.granted.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.9. 小程序 - 个人积分详情记录 (张小龙) v2.0car_platform_path: /jiaochen/applet/granted/integral-mall/integral-record
 *
 * @author wangmin
 * @date 2021-03-30 15:23:58
 */
@Builder
public class AppletIntegralRecordScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

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
     * 描述 类型（ ALL("全部"),GAIN("获得"),CONSUME("消耗")）
     * 是否必填 false
     * 版本 v2.0
     */
    private final String type;

    /**
     * 描述 开始时间
     * 是否必填 false
     * 版本 v2.0
     */
    private final String startTime;

    /**
     * 描述 结束时间(最大月份)
     * 是否必填 false
     * 版本 v2.0
     */
    private final String endTime;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("size", size);
        object.put("last_value", lastValue);
        object.put("type", type);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/integral-mall/integral-record";
    }
}