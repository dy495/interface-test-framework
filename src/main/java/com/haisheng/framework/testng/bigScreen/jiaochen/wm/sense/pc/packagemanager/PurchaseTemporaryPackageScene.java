package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 套餐管理 -> 购买临时套餐
 */
@Builder
public class PurchaseTemporaryPackageScene extends BaseScene {
    private final String customerPhone;
    private final String customerName;
    private final Long customerId;
    private final JSONArray voucherList;
    private final String carType;
    private final String plateNumber;
    private final Integer selectNumber;
    private final Integer price;
    private final String expiryDate;
    private final String remark;
    private final String extendedInsuranceYear;
    private final String extendedInsuranceCopies;
    private final Integer type;
    private final String subjectType;
    private final Long subjectId;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("customer_phone", customerPhone);
        object.put("customer_name", customerName);
        object.put("customer_id", customerId);
        object.put("voucher_list", voucherList);
        object.put("car_type", carType);
        object.put("plate_number", plateNumber);
        object.put("select_number", selectNumber);
        object.put("price", price);
        object.put("expiry_date", expiryDate);
        object.put("remark", remark);
        object.put("extended_insurance_year", extendedInsuranceYear);
        object.put("extended_insurance_copies", extendedInsuranceCopies);
        object.put("type", type);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/package-manage/purchase-temporary-package";
    }
}
