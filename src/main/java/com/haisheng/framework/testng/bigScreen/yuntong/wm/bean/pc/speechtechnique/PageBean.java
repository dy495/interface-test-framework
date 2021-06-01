package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.speechtechnique;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 3.2. 话术分页（谢）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:52
 */
@Data
public class PageBean implements Serializable {
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
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

    /**
     * 描述 关键词
     * 版本 v1.0
     */
    @JSONField(name = "keywords")
    private String keywords;

    /**
     * 描述 建议
     * 版本 v1.0
     */
    @JSONField(name = "advice")
    private String advice;

    /**
     * 描述 todo 标签，和产品确认是任意填写还是枚举
     * 版本 v1.0
     */
    @JSONField(name = "label")
    private String label;

    /**
     * 描述 适用品牌列表
     * 版本 v1.0
     */
    @JSONField(name = "brands")
    private JSONArray brands;

    /**
     * 描述 适用环节
     * 版本 v1.0
     */
    @JSONField(name = "link_name")
    private String linkName;

    /**
     * 描述 创建时间
     * 版本 v1.0
     */
    @JSONField(name = "create_time")
    private String createTime;

}