package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.8. 洗车管理 (池) v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class WashCarManagerPageScene extends BaseScene {
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
     * 描述 客户名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String customerName;

    /**
     * 描述 客户类型 会员类型
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer customerType;

    /**
     * 描述 开始时间
     * 是否必填 false
     * 版本 v2.0
     */
    private final String washStartTime;

    /**
     * 描述 结束时间
     * 是否必填 false
     * 版本 v2.0
     */
    private final String washEndTime;

    /**
     * 描述 洗车门店
     * 是否必填 false
     * 版本 v2.0
     */
    private final String shopId;

    /**
     * 描述 联系方式
     * 是否必填 false
     * 版本 v2.0
     */
    private final String phone;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("customerName", customerName);
        object.put("customer_type", customerType);
        object.put("wash_start_time", washStartTime);
        object.put("wash_end_time", washEndTime);
        object.put("shop_id", shopId);
        object.put("phone", phone);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/vip-marketing/wash-car-manager/page";
    }
}