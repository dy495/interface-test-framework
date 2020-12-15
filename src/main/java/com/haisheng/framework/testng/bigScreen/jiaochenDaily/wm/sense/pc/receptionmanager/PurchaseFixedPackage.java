package com.haisheng.framework.testng.bigScreen.jiaochenDaily.wm.sense.pc.receptionmanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crmDaily.wm.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 接待管理 -> 购买固定套餐
 */
@Builder
public class PurchaseFixedPackage extends BaseScene {
    private final Long receptionId;
    private final Long customerId;
    private final List<Integer> packageId;
    private final String carType;
    private final Integer selectNumber;
    private final Integer price;
    private final Integer expiryDate;
    private final String remark;
    private final Integer extendedInsuranceYear;
    private final Integer extendedInsuranceCopies;
    private final String voucherType;
    private final Integer type;
    private final String subjectType;
    private final Integer subjectId;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("reception_id", receptionId);
        object.put("customer_id", customerId);
        object.put("package_id", packageId);
        object.put("car_type", carType);
        object.put("select_number", selectNumber);
        object.put("price", price);
        object.put("expiry_date", expiryDate);
        object.put("remark", remark);
        object.put("extended_insurance_year", extendedInsuranceYear);
        object.put("extended_insurance_copies", extendedInsuranceCopies);
        object.put("voucher_type", voucherType);
        object.put("type", type);
        object.put("subject_type", subjectType);
        object.put("subjet_id", subjectId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/reception-manage/purchase-fixed-package";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
