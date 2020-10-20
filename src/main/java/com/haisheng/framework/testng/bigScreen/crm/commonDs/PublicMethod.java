package com.haisheng.framework.testng.bigScreen.crm.commonDs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.EditAfterSaleCustomerScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.ReceptionAfterCustomerListScene;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;

import java.text.ParseException;
import java.util.*;

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
                    String account = list.getJSONObject(j).getString("user_login_name");
                    map.put("userId", userId);
                    map.put("userName", userName);
                    map.put("account", account);
                    array.add(map);
                }
            }
        }
        Map<String, String> map = new HashMap<>(16);
        map.put("userId", "");
        map.put("userName", "总经理123456");
        map.put("account", "zjl");
        array.add(map);
        return array;
    }

    /**
     * 售后完成接待
     */
    public void afterSaleReception() {
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
        JSONObject data = crm.detailAfterSaleCustomer(String.valueOf(afterRecordId));
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
                .afterRecordId(String.valueOf(afterRecordId))
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
        crm.invokeApi(scene);
    }

    /**
     * 获取未接待完成的售后记录id
     *
     * @param serviceComplete 是否完成接待
     * @param n               n天之内的数据，eg:n=2,据今天两天内的数据
     */
    public Integer getAfterRecordId(boolean serviceComplete, int n) throws ParseException {
        String date = DateTimeUtil.getFormat(new Date());
        IScene scene = ReceptionAfterCustomerListScene.builder().build();
        int s = crm.invokeApi(scene).getInteger("total");
        int afterRecordId = 0;
        for (int i = 1; i < s; i++) {
            IScene scene1 = ReceptionAfterCustomerListScene.builder().page(i).size(100).build();
            JSONArray list = crm.invokeApi(scene1).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                String serviceDate = list.getJSONObject(j).getString("service_date");
                if (list.getJSONObject(j).getBoolean("service_complete") == serviceComplete
                        && new DateTimeUtil().calTimeDayDiff(serviceDate, date) <= n) {
                    afterRecordId = list.getJSONObject(j).getInteger("after_record_id");
                }
            }
        }
        return afterRecordId;
    }

    /**
     * 完成接待
     *
     * @param afterRecordId {@link PublicMethod#getAfterRecordId(boolean serviceComplete, int dayNum)}
     */
    public void completeReception(String afterRecordId) {
        JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
        String appointmentCustomerName = response.getString("appointment_customer_name");
        Integer appointmentId = response.getInteger("appointment_id");
        if (appointmentId == null) {
            appointmentId = 0;
        }
        String appointmentPhoneNumber = response.getString("appointment_phone_number");
        String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
        String customerName = response.getString("customer_name");
        String customerPhoneNumber = response.getString("customer_phone_number");
        int customerSource = response.getInteger("customer_source");
        String firstRepairCarType = response.getString("first_repair_car_type");
        String maintainSaleId = response.getString("maintain_sale_id");
        int maintainType = response.getInteger("maintain_type");
        String plateNumber = response.getString("plate_number");
        IScene scene = EditAfterSaleCustomerScene.builder().afterRecordId(afterRecordId).appointmentCustomerName(appointmentCustomerName)
                .appointmentId(appointmentId).appointmentPhoneNumber(appointmentPhoneNumber).appointmentSecondaryPhone(appointmentSecondaryPhone)
                .customerName(customerName).customerPhoneNumber(customerPhoneNumber).customerSource(customerSource).firstRepairCarType(firstRepairCarType)
                .maintainSaleId(maintainSaleId).maintainType(maintainType).plateNumber(plateNumber).travelMileage(1000).build();
        crm.invokeApi(scene);
    }

    public int getStatusNum(String type) {
        IScene scene = ReceptionAfterCustomerListScene.builder().build();
        int total = crm.invokeApi(scene).getInteger("total");
        int s = CommonUtil.getTurningPage(total, 100);
        int count = 0;
        for (int i = 1; i < s; i++) {
            IScene scene1 = ReceptionAfterCustomerListScene.builder().size(100).page(i).build();
            JSONArray list = crm.invokeApi(scene1).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                if (list.getJSONObject(j).getString("reception_status_name").equals(type)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 车辆进店车牌号上传
     *
     * @param carNum 车牌号
     * @param status 车辆进店状态 0入店/1出店/3线上
     */
    public void uploadShopCarPlate(String carNum, Integer status) throws Exception {
        String router = "/business/porsche/PLATE_UPLOAD/v1.0";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String deviceId;
        switch (status) {
            case 0:
                deviceId = "7709867521115136";
                break;
            case 1:
                deviceId = "7724082825888768";
                break;
            case 3:
                deviceId = "7736789438301184";
                break;
            default:
                throw new RuntimeException("状态值只能为0/1/3");
        }
        upload(picPath, carNum, router, deviceId);
    }

    /**
     * 上传
     *
     * @param picPath  图片地址
     * @param carNum   车牌号
     * @param router   地址
     * @param deviceId deviceId
     */
    private void upload(String picPath, String carNum, String router, String deviceId) throws Exception {
        ImageUtil imageUtil = new ImageUtil();
        String[] resource = new String[]{imageUtil.getImageBinary(picPath)};
        JSONObject object = new JSONObject();
        object.put("plate_num", carNum);
        object.put("plate_pic", "@0");
        object.put("time", System.currentTimeMillis());
        if (deviceId.equals("7736789438301184")) {
            crm.carUploadToOnline(router, deviceId, resource, JSON.toJSONString(object));
        } else {
            crm.carUploadToDaily(router, deviceId, resource, JSON.toJSONString(object));
        }
    }
}
