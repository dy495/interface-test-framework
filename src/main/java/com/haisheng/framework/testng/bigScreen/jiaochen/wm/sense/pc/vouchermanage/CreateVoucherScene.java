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
    private final String voucherPic;
    private final String voucherName;
    private final String voucherDescription;
    private final Integer stock;
    private final Double cost;
    private final Integer shopType;
    private final List<Long> shopIds;
    private final Boolean selfVerification;
    private final String subjectType;
    private final Long subjectId;
    private final String cardType;
    private final Boolean isThreshold;
    private final Double thresholdPrice;
    private final Double parValue;
    private final Double discount;
    private final Double mostDiscount;
    private final String exchangeCommodityName;
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
