package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.26. 核销记录导出（华成裕）（2020-12-23）
 *
 * @author wangmin
 * @date 2021-03-30 14:00:03
 */
@Builder
public class VerificationRecordExportScene extends BaseScene {
    /**
     * 描述 当前页
     * 是否必填 true
     * 版本 -
     */
    private final Integer page;

    /**
     * 描述 当前页的数量
     * 是否必填 true
     * 版本 -
     */
    private final Integer size;

    /**
     * 描述 卡券id
     * 是否必填 false
     * 版本 -
     */
    private final Long voucherId;

    /**
     * 描述 卡券名称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String voucherName;

    /**
     * 描述 发券人
     * 是否必填 false
     * 版本 v1.0
     */
    private final String sender;

    /**
     * 描述 发券人手机号
     * 是否必填 false
     * 版本 -
     */
    private final String salePhone;

    /**
     * 描述 发卡时间范围查询开始日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String startTime;

    /**
     * 描述 发卡时间范围查询结束日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String endTime;

    /**
     * 描述 客户名称
     * 是否必填 false
     * 版本 v2.2
     */
    private final String customerName;

    /**
     * 描述 客户手机号
     * 是否必填 false
     * 版本 v2.2
     */
    private final String customerPhone;

    /**
     * 描述 核销查询开始日期
     * 是否必填 false
     * 版本 v2.2
     */
    private final String verifyStartTime;

    /**
     * 描述 核销查询结束日期
     * 是否必填 false
     * 版本 v2.2
     */
    private final String verifyEndTime;

    /**
     * 描述 操作人名称
     * 是否必填 false
     * 版本 -
     */
    private final String verifySaleName;

    /**
     * 描述 操作人凉席方式
     * 是否必填 false
     * 版本 -
     */
    private final String verifySalePhone;

    /**
     * 描述 核销码
     * 是否必填 false
     * 版本 v2.2
     */
    private final String verifyCode;

    /**
     * 描述 核销渠道
     * 是否必填 false
     * 版本 v2.2
     */
    private final Integer verifyChannel;

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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("voucher_id", voucherId);
        object.put("voucher_name", voucherName);
        object.put("sender", sender);
        object.put("sale_phone", salePhone);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("verify_start_time", verifyStartTime);
        object.put("verify_end_time", verifyEndTime);
        object.put("verify_sale_name", verifySaleName);
        object.put("verify_sale_phone", verifySalePhone);
        object.put("verify_code", verifyCode);
        object.put("verify_channel", verifyChannel);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/voucher-manage/verification-record/export";
    }
}