package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 4.8. 洗车管理 (池) v2.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class WashCarManagerPageBean implements Serializable {
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
     * 描述 洗车时间
     * 版本 v2.0
     */
    @JSONField(name = "wash_car_date")
    private String washCarDate;

    /**
     * 描述 洗车门店
     * 版本 v2.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 客户类型 VIP_TYPE 通用枚举里面取
     * 版本 v2.0
     */
    @JSONField(name = "customer_vip_type")
    private Integer customerVipType;

    /**
     * 描述 客户类型 VIP_TYPE 通用枚举里面取
     * 版本 v2.0
     */
    @JSONField(name = "customer_vip_type_name")
    private String customerVipTypeName;

    /**
     * 描述 客户名称
     * 版本 v2.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 联系方式
     * 版本 v2.0
     */
    @JSONField(name = "phone")
    private String phone;

    /**
     * 描述 常用车辆
     * 版本 v2.0
     */
    @JSONField(name = "plate_number")
    private String plateNumber;

    /**
     * 描述 洗车次数
     * 版本 v2.0
     */
    @JSONField(name = "wash_number")
    private Integer washNumber;

}