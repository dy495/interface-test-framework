package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.2. 评价记录导出
 *
 * @author wangmin
 * @date 2021-06-01 19:09:09
 */
@Builder
public class EvaluateV4ExportScene extends BaseScene {
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
     * 描述 客户经理id 通过权限员工列表获取下拉列表 auth_type，售后：AFTER_SALE_RECEPTION，售前：PRE_SALE_RECEPTION
     * 是否必填 false
     * 版本 v2.0
     */
    private final String serviceSaleId;

    /**
     * 描述 进店情况 使用"获取指定枚举值列表"接口获取 enum_type为 ENTER_STORE_STATUS_LIST
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer enterStatus;

    /**
     * 描述 接待环节 使用"获取指定枚举值列表"接口获取 enum_type为 RECEPTION_LINKS
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer linkType;

    /**
     * 描述 星级 [1,5]
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer score;

    /**
     * 描述 客户姓名
     * 是否必填 false
     * 版本 v1.0
     */
    private final String customerName;

    /**
     * 描述 客户联系方式
     * 是否必填 false
     * 版本 v1.0
     */
    private final String customerPhone;

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
     * 描述 车牌号
     * 是否必填 false
     * 版本 v2.0
     */
    private final String plateNumber;

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
     * 描述 导出类型 ALL：导出全部，CURRENT_PAGE：导出当前页
     * 是否必填 true
     * 版本 v2.0
     */
    private final String exportType;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("service_sale_id", serviceSaleId);
        object.put("enter_status", enterStatus);
        object.put("link_type", linkType);
        object.put("score", score);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("reception_start", receptionStart);
        object.put("reception_end", receptionEnd);
        object.put("plate_number", plateNumber);
        object.put("evaluate_type", evaluateType);
        object.put("shop_id", shopId);
        object.put("evaluate_start", evaluateStart);
        object.put("evaluate_end", evaluateEnd);
        object.put("is_follow_up", isFollowUp);
        object.put("is_have_msg", isHaveMsg);
        object.put("source_create_start", sourceCreateStart);
        object.put("source_create_end", sourceCreateEnd);
        object.put("export_type", exportType);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/manage/evaluate/v4/export";
    }
}