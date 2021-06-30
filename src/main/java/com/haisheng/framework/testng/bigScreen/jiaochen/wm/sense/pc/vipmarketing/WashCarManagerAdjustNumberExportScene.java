package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 20.11. 调整次数记录导出 (华成裕) （2020-12-24）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class WashCarManagerAdjustNumberExportScene extends BaseScene {
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

    /**
     * 描述 导出类型 ALL：导出全部，CURRENT_PAGE：导出当前页，SPECIFIED_DATA：导出特定数据
     * 是否必填 true
     * 版本 v2.0
     */
    private final String exportType;

    /**
     * 描述 导出数据id列表，特定数据时必填
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray ids;


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
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/vip-marketing/wash-car-manager/adjust-number/export";
    }
}