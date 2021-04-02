package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 21.3. 智能提醒项分页 （谢）（2021-01-05）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class IntelligentRemindPageBean implements Serializable {
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
     * 描述 提醒项目id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 提醒项目（提醒类型）
     * 版本 v2.0
     */
    @JSONField(name = "item")
    private String item;

    /**
     * 描述 奖励卡券
     * 版本 v2.0
     */
    @JSONField(name = "vouchers")
    private JSONArray vouchers;

    /**
     * 描述 卡券id
     * 版本 v1.0
     */
    @JSONField(name = "voucher_id")
    private Long voucherId;

    /**
     * 描述 卡券名
     * 版本 v1.0
     */
    @JSONField(name = "voucher_name")
    private String voucherName;

    /**
     * 描述 提醒说明
     * 版本 v2.0
     */
    @JSONField(name = "content")
    private String content;

    /**
     * 描述 提醒里程数
     * 版本 v2.0
     */
    @JSONField(name = "mileage")
    private Integer mileage;

    /**
     * 描述 提醒天数
     * 版本 v2.0
     */
    @JSONField(name = "days")
    private Integer days;

    /**
     * 描述 奖励卡券有效期天数
     * 版本 v2.0
     */
    @JSONField(name = "effective_days")
    private Integer effectiveDays;

}