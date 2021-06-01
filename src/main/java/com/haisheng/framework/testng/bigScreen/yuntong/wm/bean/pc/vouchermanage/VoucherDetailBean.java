package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 7.10. 卡券详情 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class VoucherDetailBean implements Serializable {
    /**
     * 描述 卡券名称
     * 版本 v2.0
     */
    @JSONField(name = "voucher_name")
    private String voucherName;

    /**
     * 描述 主体类型
     * 版本 v2.0
     */
    @JSONField(name = "subject_type")
    private String subjectType;

    /**
     * 描述 主体类型id
     * 版本 v2.0
     */
    @JSONField(name = "subject_id")
    private Long subjectId;

    /**
     * 描述 主体类型名称
     * 版本 v2.0
     */
    @JSONField(name = "subject_name")
    private String subjectName;

    /**
     * 描述 发放总量
     * 版本 v2.0
     */
    @JSONField(name = "stock")
    private Integer stock;

    /**
     * 描述 卡券类型 FULL_DISCOUNT("满减券"),COUPON("折扣券"),COMMODITY_EXCHANGE("商品兑换券"),CUSTOM("自定义券");
     * 版本 v2.0
     */
    @JSONField(name = "card_type")
    private String cardType;

    /**
     * 描述 卡券类型 FULL_DISCOUNT("满减券"),COUPON("折扣券"),COMMODITY_EXCHANGE("商品兑换券"),CUSTOM("自定义券");
     * 版本 v2.0
     */
    @JSONField(name = "card_type_name")
    private String cardTypeName;

    /**
     * 描述 是否有使用门槛
     * 版本 v2.0
     */
    @JSONField(name = "is_threshold")
    private Boolean isThreshold;

    /**
     * 描述 门槛价格
     * 版本 v2.0
     */
    @JSONField(name = "threshold_price")
    private String thresholdPrice;

    /**
     * 描述 面值
     * 版本 v2.0
     */
    @JSONField(name = "par_value")
    private String parValue;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "doubleParValue")
    private Double doubleParValue;

    /**
     * 描述 折扣
     * 版本 v2.0
     */
    @JSONField(name = "discount")
    private String discount;

    /**
     * 描述 最多优惠
     * 版本 v2.0
     */
    @JSONField(name = "most_discount")
    private String mostDiscount;

    /**
     * 描述 兑换商品名称
     * 版本 v2.0
     */
    @JSONField(name = "exchange_commodity_name")
    private String exchangeCommodityName;

    /**
     * 描述 业务类型 0：门店 1:异业 2:全部
     * 版本 v2.0
     */
    @JSONField(name = "shop_type")
    private Integer shopType;

    /**
     * 描述 卡券描述
     * 版本 v2.0
     */
    @JSONField(name = "voucher_description")
    private String voucherDescription;

    /**
     * 描述 门店列表
     * 版本 v2.0
     */
    @JSONField(name = "shop_ids")
    private JSONArray shopIds;

    /**
     * 描述 门店id
     * 版本 v1.0
     */
    @JSONField(name = "shop_id")
    private Long shopId;

    /**
     * 描述 门店名称
     * 版本 v1.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 是否默认图片
     * 版本 v3.0
     */
    @JSONField(name = "is_default_pic")
    private Boolean isDefaultPic;

    /**
     * 描述 优惠券样式
     * 版本 v2.0
     */
    @JSONField(name = "voucher_url")
    private String voucherUrl;

    /**
     * 描述 是否自助核销
     * 版本 v2.0
     */
    @JSONField(name = "self_verification")
    private Boolean selfVerification;

    /**
     * 描述 成本
     * 版本 v2.0
     */
    @JSONField(name = "cost")
    private String cost;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "doubleCost")
    private Double doubleCost;

    /**
     * 描述 创建人
     * 版本 v2.0
     */
    @JSONField(name = "creator_name")
    private String creatorName;

    /**
     * 描述 卡券状态
     * 版本 v2.0
     */
    @JSONField(name = "status")
    private Integer status;

    /**
     * 描述 卡券状态描述
     * 版本 v2.0
     */
    @JSONField(name = "status_name")
    private String statusName;

    /**
     * 描述 剩余库存
     * 版本 -
     */
    @JSONField(name = "surplusNum")
    private Integer surplusNum;

    /**
     * 描述 抵金价
     * 版本 -
     */
    @JSONField(name = "replace_price")
    private String replacePrice;

    /**
     * 描述 卡券ossKey
     * 版本 v3.0
     */
    @JSONField(name = "voucher_oss_key")
    private String voucherOssKey;

}