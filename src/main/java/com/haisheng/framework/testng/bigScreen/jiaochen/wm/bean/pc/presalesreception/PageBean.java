package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.presalesreception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 19.1. 接待分页（谢）v3.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
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
     * 描述 接待id
     * 版本 v3.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 接待门店id
     * 版本 v3.0
     */
    @JSONField(name = "shop_id")
    private Long shopId;

    /**
     * 描述 顾客归属 见字典表《顾客归属类型》
     * 版本 v3.0
     */
    @JSONField(name = "belong_type")
    private Integer belongType;

    /**
     * 描述 接待顾客名称
     * 版本 v3.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 顾客头像
     * 版本 v3.0
     */
    @JSONField(name = "customer_avatar")
    private String customerAvatar;

    /**
     * 描述 接待顾客电话
     * 版本 v3.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 接待顾客性别 0女 1男
     * 版本 v3.0
     */
    @JSONField(name = "customer_gender")
    private Integer customerGender;

    /**
     * 描述 所属顾问
     * 版本 v3.0
     */
    @JSONField(name = "belong_sale_name")
    private String belongSaleName;

    /**
     * 描述 售后接待员工id
     * 版本 v3.0
     */
    @JSONField(name = "receptor_id")
    private String receptorId;

    /**
     * 描述 接待顾问
     * 版本 v3.0
     */
    @JSONField(name = "reception_sale_name")
    private String receptionSaleName;

    /**
     * 描述 接待时间
     * 版本 v3.0
     */
    @JSONField(name = "reception_time")
    private String receptionTime;

    /**
     * 描述 意向车型
     * 版本 v3.0
     */
    @JSONField(name = "car_model")
    private String carModel;

    /**
     * 描述 预计购车时间
     * 版本 v3.0
     */
    @JSONField(name = "estimated_buy_time")
    private String estimatedBuyTime;

    /**
     * 描述 客户等级
     * 版本 v3.0
     */
    @JSONField(name = "customer_level_name")
    private String customerLevelName;

    /**
     * 描述 是否超时
     * 版本 v3.0
     */
    @JSONField(name = "is_overtime")
    private Boolean isOvertime;

    /**
     * 描述 预约客户标识，空则不展示
     * 版本 v3.0
     */
    @JSONField(name = "label")
    private String label;

    /**
     * 描述 是否可变更接待
     * 版本 v3.0
     */
    @JSONField(name = "is_can_change_receptor")
    private Boolean isCanChangeReceptor;

    /**
     * 描述 是否完成，完成后、确认购车、取消、完成按钮不可点击（或不出现）
     * 版本 v3.0
     */
    @JSONField(name = "is_finish")
    private Boolean isFinish;

    /**
     * 描述 客户创建日期
     * 版本 v3.0
     */
    @JSONField(name = "create_time")
    private String createTime;

    /**
     * 描述 意向品牌id
     * 版本 v3.0
     */
    @JSONField(name = "brand_id")
    private Long brandId;

    /**
     * 描述 意向车系id
     * 版本 v3.0
     */
    @JSONField(name = "car_style_id")
    private Long carStyleId;

    /**
     * 描述 意向车型id
     * 版本 v3.0
     */
    @JSONField(name = "car_model_id")
    private Long carModelId;

    /**
     * 描述 意向品牌名
     * 版本 v3.0
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 意向车系名
     * 版本 v3.0
     */
    @JSONField(name = "car_style_name")
    private String carStyleName;

    /**
     * 描述 意向车型名
     * 版本 v3.0
     */
    @JSONField(name = "car_model_name")
    private String carModelName;

    /**
     * 描述 接待门店id
     * 版本 v3.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 接待销售账号
     * 版本 v3.0
     */
    @JSONField(name = "reception_sale_account")
    private String receptionSaleAccount;

    /**
     * 描述 所属销售账号
     * 版本 v3.0
     */
    @JSONField(name = "belong_sale_account")
    private String belongSaleAccount;

    /**
     * 描述 接待类型
     * 版本 v3.0
     */
    @JSONField(name = "reception_type_name")
    private String receptionTypeName;

    /**
     * 描述 接待开始时间
     * 版本 v3.0
     */
    @JSONField(name = "reception_start_time")
    private String receptionStartTime;

    /**
     * 描述 接待结束时间
     * 版本 v3.0
     */
    @JSONField(name = "reception_end_time")
    private String receptionEndTime;

}