package com.haisheng.framework.testng.bigScreen.crm.wm.scene.app;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.experiment.enumerator.EnumAddress;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.base.BaseScene;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 售后完成接待接口
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class EditAfterSaleCustomerScene extends BaseScene {
    private Integer afterRecordId;
    private String analysisCustomerId;
    private boolean isDecision;
    private String appointmentCustomerName;
    private Integer appointmentId;
    private String appointmentPhoneNumber;
    private String appointmentSecondaryPhone;
    private String customerName;
    private String customerPhoneNumber;
    private Integer customerSource;
    private String firstRepairCarType;
    private String maintainSaleId;
    private Integer maintainType;
    private String plateNumber;
    private JSONArray remarks;
    @Builder.Default
    private boolean serviceComplete = true;
    private Integer travelMileage;

    @Override
    public JSONObject getJson() {
        JSONObject object = new JSONObject();
        object.put("after_record_id", afterRecordId);
        JSONArray alongList = new JSONArray();
        JSONObject object1 = new JSONObject();
        object1.put("analysis_customer_id", analysisCustomerId);
        object1.put("is_decision", isDecision);
        alongList.add(object1);
        object.put("along_list", alongList);
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
        return EnumAddress.PORSCHE.getAddress();
    }
}
