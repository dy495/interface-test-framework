package com.haisheng.framework.testng.bigScreen.crm.wm.scene.app;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 售后完成接待接口
 *
 * @author wangmin
 */
@Builder
public class EditAfterSaleCustomerScene extends BaseScene {
    private final String afterRecordId;
    private final String analysisCustomerId;
    private final Boolean isDecision;
    private final String appointmentCustomerName;
    private final Integer appointmentId;
    private final String appointmentPhoneNumber;
    private final String appointmentSecondaryPhone;
    private final String customerName;
    private final String customerPhoneNumber;
    private final Integer customerSource;
    private final String firstRepairCarType;
    private final String maintainSaleId;
    private final Integer maintainType;
    private final String plateNumber;
    private final JSONArray remarks;
    @Builder.Default
    private final boolean serviceComplete = true;
    private final Integer travelMileage;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("after_record_id", afterRecordId);
        JSONArray alongList = new JSONArray();
        JSONObject object1 = new JSONObject();
        object1.put("analysis_customer_id", analysisCustomerId);
        object1.put("is_decision", isDecision);
        alongList.add(object1);
        if (!StringUtils.isEmpty(analysisCustomerId) || isDecision != null) {
            object.put("along_list", alongList);
        }
        object.put("appointment_customer_name", appointmentCustomerName);
        object.put("appointment_id", appointmentId);
        object.put("appointment_phone_number", appointmentCustomerName);
        object.put("appointment_secondary_phone", appointmentSecondaryPhone);
        object.put("customer_name", customerName);
        object.put("customer_phone_number", customerPhoneNumber);
        object.put("customer_source", customerSource);
        object.put("first_repair_car_type", firstRepairCarType);
        object.put("maintain_sale_id", maintainSaleId);
        object.put("maintain_type", maintainType);
        object.put("plate_number", plateNumber);
        object.put("remarks", remarks);
        object.put("service_complete", serviceComplete);
        object.put("travel_mileage", travelMileage);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/app/after_sale/edit_after_sale_customer";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
