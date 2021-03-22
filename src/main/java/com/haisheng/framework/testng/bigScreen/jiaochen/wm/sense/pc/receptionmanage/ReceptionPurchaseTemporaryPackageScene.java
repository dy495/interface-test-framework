package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 接待管理 -> 购买临时套餐
 */
@Builder
public class ReceptionPurchaseTemporaryPackageScene extends BaseScene {
    private final String customerPhone;
    private final String carType;
    private final String plateNumber;
    private final JSONArray voucherList;
    private final String expiryDate;
    private final Integer expireType;
    private final String remark;
    private final String subjectType;
    private final Long subjectId;
    private final String extendedInsuranceYear;
    private final String extendedInsuranceCopies;
    private final Integer type;
    private final Integer receptionId;
    private final Long customerId;
    private final Integer shopId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_phone", customerPhone);
        object.put("car_type", carType);
        object.put("plate_number", plateNumber);
        object.put("voucher_list", voucherList);
        object.put("expire_type", expireType);
        object.put("expiry_date", expiryDate);
        object.put("remark", remark);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        object.put("extended_insurance_year", extendedInsuranceYear);
        object.put("extended_insurance_copies", extendedInsuranceCopies);
        object.put("type", type);
        object.put("reception_id", receptionId);
        object.put("customer_id", customerId);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/reception-manage/purchase-temporary-package";
    }
}
