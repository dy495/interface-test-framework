package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 套餐管理 -> 购买固定套餐
 */
@Builder
public class PurchaseFixedPackageScene extends BaseScene {
    private final String customerPhone;
    private final String customerName;
    private final Long customerId;
    private final Long packageId;
    private final String carType;
    private final String plateNumber;
    private final Integer selectNumber;
    private final String packagePrice;
    private final String expiryDate;
    private final String remark;
    private final Integer extendedInsuranceYear;
    private final Integer extendedInsuranceCopies;
    private final Integer type;
    private final String subjectType;
    private final Long subjectId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_phone", customerPhone);
        object.put("customer_name", customerName);
        object.put("customer_id", customerId);
        object.put("package_id", packageId);
        object.put("car_type", carType);
        object.put("plate_number", plateNumber);
        object.put("select_number", selectNumber);
        object.put("package_price", packagePrice);
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
        return "/car-platform/pc/package-manage/purchase-fixed-package";
    }


}
