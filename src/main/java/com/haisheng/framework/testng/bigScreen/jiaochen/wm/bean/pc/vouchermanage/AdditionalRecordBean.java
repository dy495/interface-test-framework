package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 5.4. 增发记录 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class AdditionalRecordBean implements Serializable {
    /**
     * 描述 当前页
     * 版本 v1.0
     */
    @JSONField(name = "page")
    private Integer page;

    /**
     * 描述 当前页的数量
     * 版本 v1.0
     */
    @JSONField(name = "size")
    private Integer size;

    /**
     * 描述 每页的数量
     * 版本 v1.0
     */
    @JSONField(name = " page_size")
    private Integer  pageSize;

    /**
     * 描述 总数
     * 版本 v1.0
     */
    @JSONField(name = "total")
    private Long total;

    /**
     * 描述 总页数
     * 版本 v1.0
     */
    @JSONField(name = "pages")
    private Integer pages;

    /**
     * 描述 详细数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 唯一id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 卡券名称
     * 版本 v2.0
     */
    @JSONField(name = "voucher_name")
    private String voucherName;

    /**
     * 描述 时间
     * 版本 v2.0
     */
    @JSONField(name = "time")
    private String time;

    /**
     * 描述 操作人
     * 版本 v2.0
     */
    @JSONField(name = "operate_sale_name")
    private String operateSaleName;

    /**
     * 描述 操作账号
     * 版本 v2.0
     */
    @JSONField(name = "operate_sale_account")
    private String operateSaleAccount;

    /**
     * 描述 申请增发数量
     * 版本 v2.0
     */
    @JSONField(name = "additional_num")
    private String additionalNum;

    /**
     * 描述 状态
     * 版本 v2.0
     */
    @JSONField(name = "status")
    private String status;

    /**
     * 描述 状态名称
     * 版本 v2.0
     */
    @JSONField(name = "status_name")
    private String statusName;

    /**
     * 描述 是否显示撤回
     * 版本 v2.0
     */
    @JSONField(name = "is_recall")
    private Boolean isRecall;

}