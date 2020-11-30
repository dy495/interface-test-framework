package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 套餐管理 -> 购买固定套餐
 */
@Builder
public class PurchaseFixedPackage extends BaseScene {
    private final String customerPhone;
    private final String customerName;
    private final Long customerId;
    private final List<Long> packageId;
    private final String carType;
    private final String plateNumber;
    private final Integer selectNumber;
    private final Integer price;
    private final Integer expiryDate;
    private final String remark;
    private final Integer extendedInsuranceYear;
    private final Integer extendedInsuranceCopies;
    private final Integer type;
    private final String subjectType;
    private final Long subjectId;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("customer_phone", customerPhone);
        object.put("customer_name", customerName);
        object.put("customer_id", customerId);
        object.put("package_id", packageId);
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
        return "/jiaochen/pc/package-manage/purchase-fixed-package";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
