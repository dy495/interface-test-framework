package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.2. 评价记录导出 （华成裕）v4.0（2020-05-06）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class EvaluateExportV4Scene extends BaseScene {
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
     * 版本 v2.0
     */
    private final String plateNumber;

    /**
     * 描述 客户经理id 通过权限员工列表获取下拉列表 auth_type，售后：AFTER_SALE_RECEPTION，售前：PRE_SALE_RECEPTION
     * 是否必填 false
     * 版本 v2.0
     */
    private final String serviceSaleId;

    /**
     * 描述 评价类型 枚举见字典表《评价类型》,v3.0变更为必填
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer evaluateType;

    /**
     * 描述 归属门店
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long shopId;

    /**
     * 描述 客户名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String customerName;

    /**
     * 描述 评价星级 （1-5）
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer score;

    /**
     * 描述 评价时间范围查询开始日期
     * 是否必填 false
     * 版本 v2.0
     */
    private final String evaluateStart;

    /**
     * 描述 评价时间范围查询结束日期
     * 是否必填 false
     * 版本 v2.0
     */
    private final String evaluateEnd;

    /**
     * 描述 是否跟进
     * 是否必填 false
     * 版本 v2.0
     */
    private final Boolean isFollowUp;

    /**
     * 描述 客户联系方式
     * 是否必填 false
     * 版本 v2.0
     */
    private final String customerPhone;

    /**
     * 描述 是否留言
     * 是否必填 false
     * 版本 v2.0
     */
    private final Boolean isHaveMsg;

    /**
     * 描述 来源项生成时间（任务时间）范围查询开始日期
     * 是否必填 false
     * 版本 v2.0
     */
    private final String sourceCreateStart;

    /**
     * 描述 来源项生成时间（任务时间）范围查询结束日期
     * 是否必填 false
     * 版本 v2.0
     */
    private final String sourceCreateEnd;

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
        object.put("service_sale_id", serviceSaleId);
        object.put("evaluate_type", evaluateType);
        object.put("shop_id", shopId);
        object.put("customer_name", customerName);
        object.put("score", score);
        object.put("evaluate_start", evaluateStart);
        object.put("evaluate_end", evaluateEnd);
        object.put("is_follow_up", isFollowUp);
        object.put("customer_phone", customerPhone);
        object.put("is_have_msg", isHaveMsg);
        object.put("source_create_start", sourceCreateStart);
        object.put("source_create_end", sourceCreateEnd);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/manage/evaluate/export-v4";
    }
}