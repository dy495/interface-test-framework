package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 13.13. 小程序客户列表 (池) v1.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class WechatCustomerPageBean implements Serializable {
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
     * 描述 注册日期
     * 版本 v1.0
     */
    @JSONField(name = "create_date")
    private String createDate;

    /**
     * 描述 客户名称
     * 版本 v1.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 联系方式
     * 版本 v1.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 性别
     * 版本 v1.0
     */
    @JSONField(name = "sex")
    private String sex;

    /**
     * 描述 活跃类型
     * 版本 v1.0
     */
    @JSONField(name = "active_type")
    private String activeType;

    /**
     * 描述 注册类型
     * 版本 v1.0
     */
    @JSONField(name = "registration_type")
    private String registrationType;

    /**
     * 描述 报名活动
     * 版本 v1.0
     */
    @JSONField(name = "appointment_activity")
    private Integer appointmentActivity;

    /**
     * 描述 预约保养
     * 版本 v1.0
     */
    @JSONField(name = "appointment_maintain")
    private Integer appointmentMaintain;

    /**
     * 描述 预约维修
     * 版本 v1.0
     */
    @JSONField(name = "appointment_repair")
    private Integer appointmentRepair;

    /**
     * 描述 预约试驾
     * 版本 v1.0
     */
    @JSONField(name = "appointment_test_drive")
    private Integer appointmentTestDrive;

    /**
     * 描述 到点频次
     * 版本 v1.0
     */
    @JSONField(name = "arrive_times")
    private Integer arriveTimes;

    /**
     * 描述 消费频次
     * 版本 v1.0
     */
    @JSONField(name = "consume_times")
    private Integer consumeTimes;

    /**
     * 描述 总消费
     * 版本 v1.0
     */
    @JSONField(name = "total_price")
    private Double totalPrice;

    /**
     * 描述 所属销售id
     * 版本 v1.0
     */
    @JSONField(name = "sale_id")
    private String saleId;

    /**
     * 描述 所属销售名称
     * 版本 v1.0
     */
    @JSONField(name = "sale_name")
    private String saleName;

    /**
     * 描述 客户经理
     * 版本 v1.0
     */
    @JSONField(name = "after_sale_name")
    private String afterSaleName;

    /**
     * 描述 唯一标识
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private String id;

    /**
     * 描述 推荐人姓名
     * 版本 v2.0
     */
    @JSONField(name = "recommend_people_name")
    private String recommendPeopleName;

    /**
     * 描述 推荐人账号
     * 版本 v2.0
     */
    @JSONField(name = "recommend_people_account")
    private String recommendPeopleAccount;

    /**
     * 描述 推荐路径
     * 版本 2.0
     */
    @JSONField(name = "recommend_path")
    private String recommendPath;

    /**
     * 描述 会员类型 通用枚举VIP_TYPE
     * 版本 v2.0
     */
    @JSONField(name = "vip_type_name")
    private String vipTypeName;

}