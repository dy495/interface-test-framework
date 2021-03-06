package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 15.2. 道路救援记录导出 （华成裕）（2020-12-24）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class RescueExportScene extends BaseScene {
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
     * 描述 客户vip类型 通过通用枚举接口获取，key为VIP_TYPE
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer vipType;

    /**
     * 描述 客户联系方式
     * 是否必填 false
     * 版本 v2.0
     */
    private final String customerPhone;

    /**
     * 描述 救援门店
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long shopId;

    /**
     * 描述 拨号时间范围查询开始日期
     * 是否必填 false
     * 版本 v2.0
     */
    private final String dialStart;

    /**
     * 描述 拨号时间范围查询结束日期
     * 是否必填 false
     * 版本 v2.0
     */
    private final String dialEnd;

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
        object.put("customer_name", customerName);
        object.put("vip_type", vipType);
        object.put("customer_phone", customerPhone);
        object.put("shop_id", shopId);
        object.put("dial_start", dialStart);
        object.put("dial_end", dialEnd);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/manage/rescue/export";
    }
}