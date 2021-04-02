package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.store;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 18.14. 分销员列表 v2.0(池)
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class SalesPageBean implements Serializable {
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
     * 描述 分销员手机号
     * 版本 v2.0
     */
    @JSONField(name = "sales_phone")
    private String salesPhone;

    /**
     * 描述 姓名
     * 版本 v2.0
     */
    @JSONField(name = "sales_name")
    private String salesName;

    /**
     * 描述 门店名称
     * 版本 v2.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 部门
     * 版本 v2.0
     */
    @JSONField(name = "dept_name")
    private String deptName;

    /**
     * 描述 岗位
     * 版本 v2.0
     */
    @JSONField(name = "job_name")
    private String jobName;

    /**
     * 描述 唯一标识
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

}