package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 卡券管理 -> 新建卡券
 */
@Builder
public class CreateVoucherScene extends BaseScene {
    /**
     * 优惠券样式
     */
    private final String voucherPic;

    /**
     * 卡券名称
     */
    private final String voucherName;

    /**
     * 卡券描述
     */
    private final String voucherDescription;

    /**
     * 发放总量
     */
    private final Integer stock;

    /**
     * 成本
     */
    private final Double cost;

    /**
     * 业务类型 0：门店 1:异业 2:全部
     */
    private final Integer shopType;

    /**
     * 门店列表
     */
    private final List<Long> shopIds;

    /**
     * 是否自助核销
     */
    private final Boolean selfVerification;

    /**
     * 主体类型
     */
    private final String subjectType;

    /**
     * 主体类型id
     */
    private final Long subjectId;

    /**
     * 卡券类型 FULL_DISCOUNT("满减券"),COUPON("折扣券"),COMMODITY_EXCHANGE("商品兑换券"),CUSTOM("自定义券");
     */
    private final String cardType;

    /**
     * 是否有使用门槛
     */
    private final Boolean isThreshold;

    /**
     * 门槛价格
     */
    private final Double thresholdPrice;

    /**
     * 面值
     */
    private final Double parValue;

    /**
     * 折扣
     */
    private final Double discount;

    /**
     * 最多优惠
     */
    private final Double mostDiscount;

    /**
     * 兑换商品名称
     */
    private final String exchangeCommodityName;

    /**
     * 是否使用默认图片
     */
    private final Boolean isDefaultPic;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("voucher_pic", voucherPic);
        object.put("is_default_pic", isDefaultPic);
        object.put("voucher_name", voucherName);
        object.put("voucher_description", voucherDescription);
        object.put("stock", stock);
        object.put("cost", cost);
        object.put("shop_type", shopType);
        object.put("shop_ids", shopIds);
        object.put("self_verification", selfVerification);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        object.put("card_type", cardType);
        object.put("is_threshold", isThreshold);
        object.put("threshold_price", thresholdPrice);
        object.put("par_value", parValue);
        object.put("discount", discount);
        object.put("most_discount", mostDiscount);
        object.put("exchange_commodity_name", exchangeCommodityName);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/voucher-manage/create";
    }
}
