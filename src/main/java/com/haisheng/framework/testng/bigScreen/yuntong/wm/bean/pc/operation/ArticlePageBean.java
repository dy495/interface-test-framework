package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.operation;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 26.1. 内容运营 : 文章列表 （谢）（2021-01-08）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class ArticlePageBean implements Serializable {
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
     * 描述 文章id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 文章标题
     * 版本 v1.0
     */
    @JSONField(name = "title")
    private String title;

    /**
     * 描述 文章创建时间
     * 版本 v1.0
     */
    @JSONField(name = "create_time")
    private String createTime;

    /**
     * 描述 文章最新更新时间
     * 版本 v1.0
     */
    @JSONField(name = "modify_time")
    private String modifyTime;

    /**
     * 描述 操作人账号
     * 版本 v2.0
     */
    @JSONField(name = "operator_account")
    private String operatorAccount;

    /**
     * 描述 状态枚举 SHOW：显示中，SCHEDULING：排期中，EXPIRE：已过期，REMOVE：已下架
     * 版本 v1.0
     */
    @JSONField(name = "status")
    private String status;

    /**
     * 描述 状态名称
     * 版本 v1.0
     */
    @JSONField(name = "status_name")
    private String statusName;

}