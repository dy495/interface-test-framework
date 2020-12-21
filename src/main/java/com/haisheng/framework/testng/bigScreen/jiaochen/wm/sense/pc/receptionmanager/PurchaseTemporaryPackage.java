package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 接待管理 -> 购买临时套餐
 */
@Builder
public class PurchaseTemporaryPackage extends BaseScene {
    private final Long receptionId;
    private final Long customerId;
    private final List<Integer> voucherList;
    private final String carType;
    private final Integer selectNumber;
    private final Integer price;
    private final Integer expiryDate;
    private final String remark;
    private final Integer extendedInsuranceYear;
    private final Integer extendedInsuranceCopies;
    private final String voucherType;
    private final Integer type;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("reception_id", receptionId);
        object.put("customer_id", customerId);
        object.put("package_id", voucherList);
        object.put("car_type", carType);
        object.put("select_number", selectNumber);
        object.put("price", price);
        object.put("expiry_date", expiryDate);
        object.put("remark", remark);
        object.put("extended_insurance_year", extendedInsuranceYear);
        object.put("extended_insurance_copies", extendedInsuranceCopies);
        object.put("voucher_type", voucherType);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/reception-manage/purchase-temporary-package";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}