package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 7.5. app接待分页（谢）v3.0 （2021-03-29）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppPageBean implements Serializable {
    /**
     * 描述 总数 首次查询或刷新时返回
     * 版本 v1.0
     */
    @JSONField(name = "total")
    private Long total;

    /**
     * 描述 本次查询最后一条数据主键
     * 版本 v1.0
     */
    @JSONField(name = "last_value")
    private JSONObject lastValue;

    /**
     * 描述 展示列（部分接口返回列按权限展示时需要）
     * 版本 v4.0
     */
    @JSONField(name = "key_list")
    private JSONArray keyList;

    /**
     * 描述 key名称（展示列名称）
     * 版本 v4.0
     */
    @JSONField(name = "key_name")
    private String keyName;

    /**
     * 描述 key值（实际取值key）
     * 版本 v4.0
     */
    @JSONField(name = "key_value")
    private String keyValue;

    /**
     * 描述 返回的结果list
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
     * 描述 接待顾客Id
     * 版本 v4.0
     */
    @JSONField(name = "customer_id")
    private Long customerId;

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
     * 描述 顾客归属标签
     * 版本 v4.0
     */
    @JSONField(name = "customer_belong_labels")
    private JSONArray customerBelongLabels;

}