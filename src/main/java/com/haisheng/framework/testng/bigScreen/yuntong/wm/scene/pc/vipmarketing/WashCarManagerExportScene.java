package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.vipmarketing;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.9. 洗车记录导出 (华成裕) （2020-12-24）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class WashCarManagerExportScene extends BaseScene {
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
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/vip-marketing/wash-car-manager/export";
    }
}