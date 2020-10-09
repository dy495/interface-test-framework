package com.haisheng.framework.testng.bigScreen.crm.commonDs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.EditAfterSaleCustomerScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.base.IScene;
import com.haisheng.framework.util.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicMethod {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();

    /**
     * 获取人员列表
     *
     * @return list
     */
    public List<Map<String, String>> getSaleList(String roleName) {
        List<Map<String, String>> array = new ArrayList<>();
        int total = crm.userUserPage(1, 10).getInteger("total");
        int s = CommonUtil.getTurningPage(total, 100);
        for (int i = 1; i < s; i++) {
            JSONArray list = crm.userUserPage(i, 100).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                if (list.getJSONObject(j).getString("role_name").equals(roleName)) {
                    Map<String, String> map = new HashMap<>(16);
                    String userId = list.getJSONObject(j).getString("user_id");
                    String userName = list.getJSONObject(j).getString("user_name");
                    map.put("userId", userId);
                    map.put("userName", userName);
                    array.add(map);
                }
            }
        }
        Map<String, String> map = new HashMap<>(16);
        map.put("userId", "");
        map.put("userName", "总经理");
        array.add(map);
        return array;
    }

    /**
     * 售后完成接待
     */
    public Integer afterSaleReception() {
        //预约中状态查询
        JSONArray list = crm.mainAppointmentList(1, 10).getJSONArray("list");
        int appointmentId = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.getJSONObject(i).getString("service_status_name").equals("预约中")) {
                appointmentId = list.getJSONObject(i).getInteger("appointment_id");
            }
        }
        //接待
        crm.reception_customer((long) appointmentId);
        //我的接待-编辑
        JSONArray list1 = crm.afterSaleCustList("", "", "", 1, 10).getJSONArray("list");
        int afterRecordId = 0;
        for (int i = 0; i < list1.size(); i++) {
            if (list1.getJSONObject(i).getInteger("appointment_id") == appointmentId) {
                afterRecordId = list1.getJSONObject(i).getInteger("after_record_id");
            }
        }
        JSONObject data = crm.afterSale_custDetail((long) afterRecordId);
        String appointmentCustomerName = data.getString("appointment_customer_name");
        int appointmentId1 = data.getInteger("appointment_id");
        String appointmentPhoneNumber = data.getString("appointment_phone_number");
        String appointmentSecondaryPhone = data.getString("appointment_secondary_phone");
        String customerName = data.getString("customer_name");
        String customerPhoneNumber = data.getString("customer_phone_number");
        int customerSource = data.getInteger("customer_source");
        String firstRepairCarType = data.getString("first_repair_car_type");
        String maintainSaleId = data.getString("maintain_sale_id");
        int maintainType = data.getInteger("maintain_type");
        String plateNumber = data.getString("plate_number");
        IScene scene = EditAfterSaleCustomerScene.builder()
                .afterRecordId(afterRecordId)
                .appointmentCustomerName(appointmentCustomerName)
                .appointmentId(appointmentId1)
                .appointmentPhoneNumber(appointmentPhoneNumber)
                .appointmentSecondaryPhone(appointmentSecondaryPhone)
                .customerName(customerName)
                .customerPhoneNumber(customerPhoneNumber)
                .customerSource(customerSource)
                .firstRepairCarType(firstRepairCarType)
                .maintainSaleId(maintainSaleId)
                .maintainType(maintainType)
                .plateNumber(plateNumber)
                .travelMileage(10000)
                .build();
        //完成接待
        return crm.editAfterSaleCustomer(scene).getInteger("id");
    }
}
