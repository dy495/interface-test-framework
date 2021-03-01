package com.haisheng.framework.testng.bigScreen.crm.wm.scene.app;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 客户管理->编辑售后记录内的客户及预约记录信息
 * 我的接待 -> 客户编辑
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
    private final boolean serviceComplete = false;
    private final double travelMileage;
    private final String customerSecondaryPhone;
    private final String firstContactName;
    private final String secondContactName;
    private final String secondPlateNumber;
    private final String thirdPlateNumber;
    private final Double estimateRepairDays;
    private final String firstContactPhone;
    private final Integer firstContactRelation;
    private final String secondContactPhone;
    private final Integer secondContactRelation;
    private final String secondAccompanyCar;

    @Override
    public JSONObject getRequestBody() {
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
        object.put("customer_secondary_phone", customerSecondaryPhone);
        object.put("first_contact_name", firstContactName);
        object.put("first_contact_phone", firstContactPhone);
        object.put("first_contact_relation", firstContactRelation);
        object.put("second_contact_name", secondContactName);
        object.put("second_contact_phone", secondContactPhone);
        object.put("second_plate_number", secondPlateNumber);
        object.put("third_plate_number", thirdPlateNumber);
        object.put("estimate_repair_days", estimateRepairDays);
        object.put("second_contact_relation", secondContactRelation);
        object.put("second_accompany_car", secondAccompanyCar);
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
