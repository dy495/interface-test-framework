package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/vip-marketing/wash-car-manager/adjust-number/record的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:17
 */
@Builder
public class WashCarManagerAdjustNumberRecordScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    @Builder.Default
    private Integer page = 1;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    @Builder.Default
    private Integer size = 10;

    /**
     * 描述 客户名称
     * 是否必填 false
     * 版本 2.0
     */
    private final String customerName;

    /**
     * 描述 客户手机号
     * 是否必填 false
     * 版本 2.0
     */
    private final String customerPhone;

    /**
     * 描述 调整门店
     * 是否必填 false
     * 版本 2.0
     */
    private final Long adjustShopId;

    /**
     * 描述 调整时间(开始)
     * 是否必填 false
     * 版本 2.0
     */
    private final String adjustStartTime;

    /**
     * 描述 调整时间(结束)
     * 是否必填 false
     * 版本 2.0
     */
    private final String adjustEndTime;

    /**
     * 描述 客户类型
     * 是否必填 false
     * 版本 2.0
     */
    private final Integer customerType;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("adjust_shop_id", adjustShopId);
        object.put("adjust_start_time", adjustStartTime);
        object.put("adjust_end_time", adjustEndTime);
        object.put("customer_type", customerType);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/vip-marketing/wash-car-manager/adjust-number/record";
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }
}