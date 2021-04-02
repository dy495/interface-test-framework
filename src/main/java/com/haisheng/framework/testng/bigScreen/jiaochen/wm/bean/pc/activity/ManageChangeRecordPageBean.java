package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 26.23. 活动变更记录分页 （谢）（2020-12-23）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class ManageChangeRecordPageBean implements Serializable {
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
     * 描述 记录id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 时间
     * 版本 v2.0
     */
    @JSONField(name = "time")
    private String time;

    /**
     * 描述 操作人姓名
     * 版本 v2.0
     */
    @JSONField(name = "operator_name")
    private String operatorName;

    /**
     * 描述 操作人手机号
     * 版本 v2.0
     */
    @JSONField(name = "operator_phone")
    private String operatorPhone;

    /**
     * 描述 操作人角色
     * 版本 v2.0
     */
    @JSONField(name = "operator_role")
    private String operatorRole;

    /**
     * 描述 变更内容
     * 版本 v2.0
     */
    @JSONField(name = "content")
    private String content;

}