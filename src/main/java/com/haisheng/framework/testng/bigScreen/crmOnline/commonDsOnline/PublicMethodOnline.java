package com.haisheng.framework.testng.bigScreen.crmOnline.commonDsOnline;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.SaleInfo;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppointmentType;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCarModel;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.exception.DataException;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.EditAfterSaleCustomerScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.ReceptionAfterCustomerListScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.applet.AppointmentMaintainScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.applet.AppointmentRepairScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.applet.AppointmentTestDriverScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.crmOnline.CrmScenarioUtilOnline;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class PublicMethodOnline {
    CrmScenarioUtilOnline crm = CrmScenarioUtilOnline.getInstance();
    EnumAccount zjl = EnumAccount.ZJL_ONLINE;

    /**
     * 获取人员列表
     *
     * @return list
     */
    public List<Map<String, String>> getSaleListByRoleName(String roleName) {
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
        map.put("userName", "总经理");
        map.put("account", "zjl");
        array.add(map);
        return array;
    }

    public List<SaleInfo> getSaleList(String roleName) {
        List<SaleInfo> list = new ArrayList<>();
        int total = crm.userUserPage(1, 10).getInteger("total");
        int s = CommonUtil.getTurningPage(total, 100);
        for (int i = 1; i < s; i++) {
            JSONArray array = crm.userUserPage(i, 100).getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).filter(e -> e.getString("role_name").equals(roleName))
                    .map(e -> JSON.parseObject(JSON.toJSONString(e), SaleInfo.class)).collect(Collectors.toList()));
        }
        SaleInfo saleInfo = new SaleInfo();
        saleInfo.setUserId(null);
        saleInfo.setUserName("zjl");
        saleInfo.setAccount("zjl");
        list.add(saleInfo);
        return list;
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
     * @param status          status 接待状态
     */
    public Integer getAfterRecordId(boolean serviceComplete, String status, int n) throws ParseException {
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
                        && list.getJSONObject(j).getString("reception_status_name").equals(status)
                        && new DateTimeUtil().calTimeDayDiff(serviceDate, date) <= n) {
                    afterRecordId = list.getJSONObject(j).getInteger("after_record_id");
                }
            }
        }
        return afterRecordId;
    }

    public Integer getAfterRecordId(boolean serviceComplete, int n) throws ParseException {
        return getAfterRecordId(serviceComplete, "维修中", n);
    }

    /**
     * 完成接待
     *
     * @param afterRecordId {@link PublicMethodOnline#getAfterRecordId(boolean serviceComplete, String status, int dayNum)}
     */
    public void completeReception(String afterRecordId) {
        JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
        String appointmentCustomerName = response.getString("appointment_customer_name");
        Integer appointmentId = response.getInteger("appointment_id");
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

    /**
     * 保存
     *
     * @param remarks 备注
     */
    public void saveReception(String afterRecordId, JSONArray remarks) {
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
                .maintainSaleId(maintainSaleId).maintainType(maintainType).plateNumber(plateNumber).serviceComplete(false).remarks(remarks).travelMileage(1000).build();
        crm.invokeApi(scene);
    }

    /**
     * 获取已完成/维修中数量
     *
     * @param type 接待状态
     * @return 数量
     */
    public int getStatusNum(String type) {
        ReceptionAfterCustomerListScene.ReceptionAfterCustomerListSceneBuilder b = ReceptionAfterCustomerListScene.builder();
        int total = crm.invokeApi(b.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, 100);
        int count = 0;
        for (int i = 1; i < s; i++) {
            JSONArray list = crm.invokeApi(b.page(i).size(100).build()).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                if (list.getJSONObject(j).getString("reception_status_name").equals(type)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 预约
     *
     * @param type 类型
     * @param date 日期
     */
    public void appointment(EnumAppointmentType type, String date) {
        EnumCarModel carModel = EnumCarModel.PANAMERA;
        UserUtil.loginApplet(EnumAppletToken.BSJ_WM_ONLINE);
        int id = getTimeId(type.getType(), date);
        IScene scene;
        switch (type) {
            case MAINTAIN:
                scene = AppointmentMaintainScene.builder().myCarId(String.valueOf(getCarId())).customerName("大马猴")
                        .customerGender("MALE").customerPhoneNumber("15321527989").timeRangeId(id).build();
                break;
            case REPAIR:
                scene = AppointmentRepairScene.builder().myCarId(String.valueOf(getCarId())).customerName("大马猴")
                        .customerGender("MALE").customerPhoneNumber("15321527989").timeRangeId(id).build();
                break;
            default:
                scene = AppointmentTestDriverScene.builder().appointmentDate(date).customerGender("MALE").customerName("大马猴")
                        .carModel(Integer.parseInt(carModel.getModelId())).carStyle(Integer.parseInt(carModel.getStyleId()))
                        .customerPhoneNumber("15321527989").build();
                break;
        }
        crm.invokeApi(scene);
    }

    /**
     * 获取预约时间id
     *
     * @param date 预约日期
     * @return 时间id
     */
    private Integer getTimeId(String type, String date) {
        if (type.equals(EnumAppointmentType.TEST_DRIVE.getType())) {
            return 0;
        }
        JSONArray list = crm.timeList(type, date).getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            if (!(list.getJSONObject(i).getInteger("left_num") == 0)) {
                return list.getJSONObject(i).getInteger("id");
            }
        }
        throw new DataException("当前时间段可预约次数为0");
    }

    /**
     * 获取车辆id
     */
    private Integer getCarId() {
        JSONArray list = crm.myCarList().getJSONArray("list");
        if (!list.isEmpty()) {
            return list.getJSONObject(0).getInteger("my_car_id");
        }
        throw new DataException("该用户小程序没有绑定车");
    }

    /**
     * 获取非重复电话号
     *
     * @return phone
     */
    public String getDistinctPhone() {
        UserUtil.login(zjl);
        String phone = "153" + CommonUtil.getRandom(8);
        int a = crm.customerList("", phone, "", "", "", 1, 10).getInteger("total");
        int b = crm.dccList("", phone, "", "", 1, 10).getInteger("total");
        return a == 0 && b == 0 ? phone : getDistinctPhone();
    }

    public String getDistinctPlat(String title, int digitNumber) {
        return title + CommonUtil.getRandom(digitNumber);
    }
}
