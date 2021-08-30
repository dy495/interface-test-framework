package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.nofacerecwarn;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 16.1. 无人脸接待预警列表 （刘）
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class NoFaceRecWarnPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 门店id
     * 是否必填 false
     * 版本 v5.0
     */
    private final Long shopId;

    /**
     * 描述 销售顾问
     * 是否必填 false
     * 版本 v5.0
     */
    private final String salesName;

    /**
     * 描述 接待开始日期
     * 是否必填 false
     * 版本 v5.0
     */
    private final String recStartDate;

    /**
     * 描述 接待结束日期
     * 是否必填 false
     * 版本 v5.0
     */
    private final String recEndDate;

    /**
     * 描述 客户名称
     * 是否必填 false
     * 版本 v5.0
     */
    private final String customerName;

    /**
     * 描述 联系方式
     * 是否必填 false
     * 版本 v5.0
     */
    private final String contact;

    /**
     * 描述 客户等级
     * 是否必填 false
     * 版本 v5.0
     */
    private final Long level;

    /**
     * 描述 预计购车时间
     * 是否必填 false
     * 版本 v5.0
     */
    private final String estimatedBuyTime;

    /**
     * 描述 意向车系
     * 是否必填 false
     * 版本 v5.0
     */
    private final String intentionCarType;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("shop_id", shopId);
        object.put("sales_name", salesName);
        object.put("rec_start_date", recStartDate);
        object.put("rec_end_date", recEndDate);
        object.put("customer_name", customerName);
        object.put("contact", contact);
        object.put("level", level);
        object.put("estimated_buy_time", estimatedBuyTime);
        object.put("intention_car_type", intentionCarType);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/no-face-rec-warn/page";
    }
}