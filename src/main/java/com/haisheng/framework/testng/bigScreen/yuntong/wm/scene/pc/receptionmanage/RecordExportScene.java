package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.receptionmanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 23.2. 接待导出 （谢）（2020-12-23）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class RecordExportScene extends BaseScene {
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
     * 描述 车牌号
     * 是否必填 false
     * 版本 v1.0
     */
    private final String plateNumber;

    /**
     * 描述 接待人id，通过《权限员工列表》接口获取，权限为AFTER_SALE_RECEPTION
     * 是否必填 false
     * 版本 v2.0
     */
    private final String receptionSaleId;

    /**
     * 描述 接待时间范围查询开始日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String receptionStart;

    /**
     * 描述 接待时间范围查询结束日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String receptionEnd;

    /**
     * 描述 客户名称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String customerName;

    /**
     * 描述 接待状态，使用通用枚举接口获取，key为RECEPTION_STATUS
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer receptionStatus;

    /**
     * 描述 完成接待时间范围查询开始日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String finishStart;

    /**
     * 描述 完成接待时间范围查询结束日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String finishEnd;

    /**
     * 描述 客户手机号
     * 是否必填 false
     * 版本 v1.0
     */
    private final String customerPhone;

    /**
     * 描述 接待状态，使用通用枚举接口获取，key为AFTER_SALE_RECEPTION_TYPE
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer receptionType;

    /**
     * 描述 归属门店id
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long shopId;

    /**
     * 描述 接待来源类型
     * 是否必填 false
     * 版本 v3.0
     */
    private final String receptionSourceType;

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
        object.put("plate_number", plateNumber);
        object.put("reception_sale_id", receptionSaleId);
        object.put("reception_start", receptionStart);
        object.put("reception_end", receptionEnd);
        object.put("customer_name", customerName);
        object.put("reception_status", receptionStatus);
        object.put("finish_start", finishStart);
        object.put("finish_end", finishEnd);
        object.put("customer_phone", customerPhone);
        object.put("reception_type", receptionType);
        object.put("shop_id", shopId);
        object.put("reception_source_type", receptionSourceType);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/reception-manage/record/export";
    }
}