package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.integralcenter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 37.4. 积分兑换商品详情 (张小龙) v3.0 modify
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class ExchangeGoodsDetailBean implements Serializable {
    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 兑换商品类型（FICTITIOUS("虚拟商品"),REAL("实物")）
     * 版本 v2.0
     */
    @JSONField(name = "exchange_goods_type")
    private String exchangeGoodsType;

    /**
     * 描述 商品id
     * 版本 v2.0
     */
    @JSONField(name = "goods_id")
    private Long goodsId;

    /**
     * 描述 商品名称
     * 版本 v2.0
     */
    @JSONField(name = "commodity_name")
    private String commodityName;

    /**
     * 描述 商品图片
     * 版本 v2.0
     */
    @JSONField(name = "commodity_pic")
    private String commodityPic;

    /**
     * 描述 商品价格
     * 版本 v2.0
     */
    @JSONField(name = "commodity_price")
    private String commodityPrice;

    /**
     * 描述 商品库存
     * 版本 v2.0
     */
    @JSONField(name = "commodity_stock")
    private Long commodityStock;

    /**
     * 描述 商品描述
     * 版本 v2.0
     */
    @JSONField(name = "commodity_description")
    private String commodityDescription;

    /**
     * 描述 兑换有效期开始时间
     * 版本 v2.0
     */
    @JSONField(name = "exchange_start_time")
    private String exchangeStartTime;

    /**
     * 描述 兑换有效期结束时间
     * 版本 v2.0
     */
    @JSONField(name = "exchange_end_time")
    private String exchangeEndTime;

    /**
     * 描述 兑换价格
     * 版本 v2.0
     */
    @JSONField(name = "exchange_price")
    private Long exchangePrice;

    /**
     * 描述 兑换数量
     * 版本 v2.0
     */
    @JSONField(name = "exchange_num")
    private Long exchangeNum;

    /**
     * 描述 是否限制
     * 版本 v2.0
     */
    @JSONField(name = "is_limit")
    private Boolean isLimit;

    /**
     * 描述 兑换限制人数
     * 版本 v2.0
     */
    @JSONField(name = "exchange_people_num")
    private Integer exchangePeopleNum;

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

    /**
     * 描述 卡券有效期类型 选择发送卡券时必填，1：时间段，2：有效天数
     * 版本 v3.0,v2.0
     */
    @JSONField(name = "expire_type")
    private Integer expireType;

    /**
     * 描述 优惠券使用有效期开始时间
     * 版本 v3.0
     */
    @JSONField(name = "voucher_use_start_time")
    private String voucherUseStartTime;

    /**
     * 描述 优惠券使用有效期结束时间
     * 版本 v3.0
     */
    @JSONField(name = "voucher_use_end_time")
    private String voucherUseEndTime;

    /**
     * 描述 领取后使用时间
     * 版本 v3.0
     */
    @JSONField(name = "use_days")
    private Integer useDays;

}