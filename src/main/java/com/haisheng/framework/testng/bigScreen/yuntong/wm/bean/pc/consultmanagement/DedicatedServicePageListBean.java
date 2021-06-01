package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.consultmanagement;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 10.6. 专属服务咨询列表 （池）(2021-03-12)
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class DedicatedServicePageListBean implements Serializable {
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
     * 描述 归属门店
     * 版本 v3.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 咨询时间
     * 版本 v3.0
     */
    @JSONField(name = "consult_date")
    private String consultDate;

    /**
     * 描述 咨询类型
     * 版本 v3.0
     */
    @JSONField(name = "sale_type")
    private String saleType;

    /**
     * 描述 联系人
     * 版本 v3.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 车辆品牌
     * 版本 v3.0
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 车型
     * 版本 v3.0
     */
    @JSONField(name = "model_name")
    private String modelName;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "style_name")
    private String styleName;

    /**
     * 描述 联系电话
     * 版本 v3.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 是否超时
     * 版本 v3.0
     */
    @JSONField(name = "is_over_time")
    private String isOverTime;

    /**
     * 描述 跟进时间
     * 版本 v3.0
     */
    @JSONField(name = "follow_date")
    private String followDate;

    /**
     * 描述 跟进人员
     * 版本 v3.0
     */
    @JSONField(name = "follow_sales_name")
    private String followSalesName;

    /**
     * 描述 跟进账号
     * 版本 v3.0
     */
    @JSONField(name = "follow_login_name")
    private String followLoginName;

    /**
     * 描述 咨询内容
     * 版本 v3.0
     */
    @JSONField(name = "consult_content")
    private String consultContent;

    /**
     * 描述 唯一id
     * 版本 v3.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 是否回复
     * 版本 v3.0
     */
    @JSONField(name = "is_reply")
    private Boolean isReply;

    /**
     * 描述 No comments found.
     * 版本 v3.0 备注内容
     */
    @JSONField(name = "remark_content")
    private String remarkContent;

    /**
     * 描述 回复内容
     * 版本 v3.1
     */
    @JSONField(name = "reply_content")
    private String replyContent;

}