package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 35.12. 兑换商品规格详情列表 (张小龙) v2.0 （2021-01-11）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class CommoditySpecificationsListBean implements Serializable {
    /**
     * 描述 第一品类
     * 版本 v2.0
     */
    @JSONField(name = "first_specification_type")
    private Long firstSpecificationType;

    /**
     * 描述 第一品类名称
     * 版本 v2.0
     */
    @JSONField(name = "first_specification_type_name")
    private String firstSpecificationTypeName;

    /**
     * 描述 第二品类
     * 版本 v2.0
     */
    @JSONField(name = "second_specification_type")
    private Long secondSpecificationType;

    /**
     * 描述 第二品类名称
     * 版本 v2.0
     */
    @JSONField(name = "second_specification_type_name")
    private String secondSpecificationTypeName;

    /**
     * 描述 商品规格列表
     * 版本 v2.0
     */
    @JSONField(name = "specification_detail_list")
    private JSONArray specificationDetailList;

    /**
     * 描述 品类详情id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 第一品类
     * 版本 v2.0
     */
    @JSONField(name = "first_specifications")
    private Long firstSpecifications;

    /**
     * 描述 第一品类名称
     * 版本 v2.0
     */
    @JSONField(name = "first_specifications_name")
    private String firstSpecificationsName;

    /**
     * 描述 第二品类
     * 版本 v2.0
     */
    @JSONField(name = "second_specifications")
    private Long secondSpecifications;

    /**
     * 描述 第二品类名称
     * 版本 v2.0
     */
    @JSONField(name = "second_specifications_name")
    private String secondSpecificationsName;

    /**
     * 描述 头图
     * 版本 v2.0
     */
    @JSONField(name = "head_pic")
    private String headPic;

    /**
     * 描述 销售价
     * 版本 v2.0
     */
    @JSONField(name = "price")
    private String price;

    /**
     * 描述 库存数量
     * 版本 v2.0
     */
    @JSONField(name = "num")
    private Long num;

    /**
     * 描述 累计数量
     * 版本 v2.0
     */
    @JSONField(name = "sold_num")
    private Long soldNum;

}