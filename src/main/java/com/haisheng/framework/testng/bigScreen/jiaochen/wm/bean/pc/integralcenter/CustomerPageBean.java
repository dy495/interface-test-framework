package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 35.25. 积分客户管理 (谢志东) v2.2 （2021-03-15）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class CustomerPageBean implements Serializable {
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
     * 描述 顾客积分id
     * 版本 v2.2
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 客户名称
     * 版本 v2.2
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 客户联系方式
     * 版本 v2.2
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 当前积分
     * 版本 v2.2
     */
    @JSONField(name = "integral")
    private String integral;

}