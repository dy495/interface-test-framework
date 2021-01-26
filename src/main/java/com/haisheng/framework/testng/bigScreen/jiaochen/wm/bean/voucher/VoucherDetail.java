package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.voucher;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 卡券详情
 *
 * @author wangmin
 * @date 2021/1/21 10:44
 */
@Data
public class VoucherDetail implements Serializable {
    /**
     * 卡券名称
     */
    @JSONField(name = "voucher_name")
    private String voucherName;

    /**
     * 主题类型
     */
    @JSONField(name = "subject_type")
    private String subjectType;

    /**
     * 主体类型id
     */
    @JSONField(name = "subject_id")
    private Integer subjectId;

    /**
     * 主题类型名称
     */
    @JSONField(name = "subject_name")
    private String subjectName;

    /**
     * 发放总量
     */
    @JSONField(name = "stock")
    private Integer stock;

    /**
     * 卡券类型
     */
    @JSONField(name = "card_type")
    private String cardType;

    /**
     * 卡券类型名称
     */
    @JSONField(name = "card_type_name")
    private String cardTypeName;

    /**
     * 是否有使用门槛
     */
    @JSONField(name = "is_threshold")
    private Boolean isThreshold;

    /**
     * 门槛价格
     */
    @JSONField(name = "threshold_price")
    private Double thresholdPrice;

    /**
     * 面值
     */
    @JSONField(name = "par_value")
    private Double parValue;

    /**
     * 折扣
     */
    @JSONField(name = "discount")
    private Double discount;

    /**
     * 最多优惠
     */
    @JSONField(name = "most_discount")
    private Double mostDiscount;

    /**
     * 兑换商品名称
     */
    @JSONField(name = "exchange_commodity_name")
    private String exchangeCommodityName;

    /**
     * 业务类型 0：门店 1:异业 2:全部
     */
    @JSONField(name = "shop_type")
    private Integer shopType;

    /**
     * 卡券描述
     */
    @JSONField(name = "voucher_description")
    private String voucherDescription;

    /**
     * 门店列表
     */
    @JSONField(name = "shop_ids")
    private JSONArray shopIds;

    /**
     * 优惠券样式
     */
    @JSONField(name = "voucher_url")
    private String voucherUrl;

    /**
     * 是否自助核销
     */
    @JSONField(name = "self_verification")
    private Boolean selfVerification;
}
