package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vipmarketing;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.13. 签到配置列表 (池) v2.0
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class SignInConfigPageBean implements Serializable {
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
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 签到类型
     * 版本 v2.0
     */
    @JSONField(name = "type")
    private String type;

    /**
     * 描述 签到类型
     * 版本 v2.0
     */
    @JSONField(name = "typeName")
    private String typeName;

    /**
     * 描述 签到积分
     * 版本 v2.0
     */
    @JSONField(name = "award_score")
    private Integer awardScore;

    /**
     * 描述 状态
     * 版本 v2.0
     */
    @JSONField(name = "status")
    private String status;

    /**
     * 描述 状态
     * 版本 v2.0
     */
    @JSONField(name = "status_name")
    private String statusName;

    /**
     * 描述 签到配置海报图
     * 版本 -
     */
    @JSONField(name = "picture_url")
    private String pictureUrl;

}