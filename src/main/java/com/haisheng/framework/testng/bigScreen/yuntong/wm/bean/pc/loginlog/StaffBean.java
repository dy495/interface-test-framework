package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.loginlog;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 29.1. 员工登录日志-分页 （华成裕）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class StaffBean implements Serializable {
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
     * 描述 账号Id
     * 版本 v5.3
     */
    @JSONField(name = "id")
    private String id;

    /**
     * 描述 账号名称
     * 版本 v5.3
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 描述 手机号
     * 版本 v5.3
     */
    @JSONField(name = "phone")
    private String phone;

    /**
     * 描述 最早登录日期
     * 版本 v5.3
     */
    @JSONField(name = "start_date")
    private String startDate;

    /**
     * 描述 最晚登录日期
     * 版本 v5.3
     */
    @JSONField(name = "end_date")
    private String endDate;

}