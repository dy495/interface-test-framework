package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 接待管理 -> 购买固定套餐
 */
@Builder
public class ReceptionPurchaseFixedPackageScene extends BaseScene {
    private final Integer receptionId;
    private final Long customerId;
    private final String customerPhone;
    private final Long packageId;
    private final String packagePrice;
    private final Integer expiryDate;
    private final Integer expireType;
    private final String carType;
    private final Integer selectNumber;
    private final String remark;
    private final String extendedInsuranceYear;
    private final String extendedInsuranceCopies;
    private final String voucherType;
    private final Integer type;
    private final String subjectType;
    private final Long subjectId;
    private final String plateNumber;
    private final Integer shopId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_phone", customerPhone);
        object.put("reception_id", receptionId);
        object.put("customer_id", customerId);
        object.put("package_id", packageId);
        object.put("package_price", packagePrice);
        object.put("car_type", carType);
        object.put("select_number", selectNumber);
        object.put("expiry_date", expiryDate);
        object.put("expire_type", expireType);
        object.put("remark", remark);
        object.put("extended_insurance_year", extendedInsuranceYear);
        object.put("extended_insurance_copies", extendedInsuranceCopies);
        object.put("voucher_type", voucherType);
        object.put("type", type);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        object.put("plate_number", plateNumber);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/reception-manage/purchase-fixed-package";
    }
}
