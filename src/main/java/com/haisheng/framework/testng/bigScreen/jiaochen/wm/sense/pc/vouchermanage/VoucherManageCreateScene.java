package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 29.9. 创建卡券 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class VoucherManageCreateScene extends BaseScene {
    /**
     * 描述 卡券id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 卡券名称
     * 是否必填 true
     * 版本 v2.0
     */
    private final String voucherName;

    /**
     * 描述 主体类型
     * 是否必填 true
     * 版本 v2.0
     */
    private final String subjectType;

    /**
     * 描述 主体类型id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long subjectId;

    /**
     * 描述 主体类型名称
     * 是否必填 true
     * 版本 v2.0
     */
    private final String subjectName;

    /**
     * 描述 发放总量
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer stock;

    /**
     * 描述 卡券类型 FULL_DISCOUNT("满减券"),COUPON("折扣券"),COMMODITY_EXCHANGE("商品兑换券"),CUSTOM("自定义券"),CASH_COUPON("抵金券");
     * 是否必填 true
     * 版本 v2.0
     */
    private final String cardType;

    /**
     * 描述 是否有使用门槛
     * 是否必填 true
     * 版本 v2.0
     */
    private final Boolean isThreshold;

    /**
     * 描述 门槛价格
     * 是否必填 true
     * 版本 v2.0
     */
    private final Double thresholdPrice;

    /**
     * 描述 抵用价格
     * 是否必填 false
     * 版本 -
     */
    private final Double replacePrice;

    /**
     * 描述 面值
     * 是否必填 true
     * 版本 v2.0
     */
    private final Double parValue;

    /**
     * 描述 折扣
     * 是否必填 true
     * 版本 v2.0
     */
    private final Double discount;

    /**
     * 描述 最多优惠
     * 是否必填 true
     * 版本 v2.0
     */
    private final Double mostDiscount;

    /**
     * 描述 兑换商品名称
     * 是否必填 true
     * 版本 v2.0
     */
    private final String exchangeCommodityName;

    /**
     * 描述 业务类型 0：门店 1:异业 2:全部
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer shopType;

    /**
     * 描述 卡券描述
     * 是否必填 true
     * 版本 v2.0
     */
    private final String voucherDescription;

    /**
     * 描述 门店列表
     * 是否必填 true
     * 版本 v2.0
     */
    private final JSONArray shopIds;

    /**
     * 描述 是否使用默认图片
     * 是否必填 false
     * 版本 v2.0
     */
    private final Boolean isDefaultPic;

    /**
     * 描述 优惠券样式
     * 是否必填 true
     * 版本 v2.0
     */
    private final String voucherPic;

    /**
     * 描述 是否自助核销
     * 是否必填 false
     * 版本 v2.0
     */
    private final Boolean selfVerification;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("voucher_name", voucherName);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        object.put("subject_name", subjectName);
        object.put("stock", stock);
        object.put("card_type", cardType);
        object.put("is_threshold", isThreshold);
        object.put("threshold_price", thresholdPrice);
        object.put("replace_price", replacePrice);
        object.put("par_value", parValue);
        object.put("discount", discount);
        object.put("most_discount", mostDiscount);
        object.put("exchange_commodity_name", exchangeCommodityName);
        object.put("shop_type", shopType);
        object.put("voucher_description", voucherDescription);
        object.put("shop_ids", shopIds);
        object.put("is_default_pic", isDefaultPic);
        object.put("voucher_pic", voucherPic);
        object.put("self_verification", selfVerification);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/voucher-manage/create";
    }
}