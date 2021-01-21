package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * @author wangmin
 * @date 2021/1/20 14:36
 * @desc 编辑优惠券
 */
@Builder
public class EditVoucher extends BaseScene {
    private final Long id;
    private final String voucherName;
    private final String subjectType;
    private final Long subjectId;
    private final String subjectName;
    private final Long stock;
    private final String cardType;
    private final Boolean isThreshold;
    private final Boolean thresholdPrice;
    private final Boolean parValue;
    private final Double discount;
    private final Double mostDiscount;
    private final String exchangeCommodityName;
    private final Integer shopType;
    private final String voucherDescription;
    private final List<Long> shopIds;
    private final String voucherPic;
    private final Boolean selfVerification;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("voucher_pic", voucherPic);
        object.put("voucher_name", voucherName);
        object.put("voucher_description", voucherDescription);
        object.put("stock", stock);
        object.put("id", id);
        object.put("shop_type", shopType);
        object.put("shop_ids", shopIds);
        object.put("self_verification", selfVerification);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        object.put("subject_name", subjectName);
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
        return "/jiaochen/pc/voucher-manage/edit-voucher";
    }
}
