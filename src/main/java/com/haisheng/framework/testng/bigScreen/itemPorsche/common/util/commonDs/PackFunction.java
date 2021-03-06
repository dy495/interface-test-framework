package com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.commonDs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.xmf.interfaceDemo.FinishReception;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;

import java.util.List;
import java.util.Random;

public class PackFunction {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp = new PublicParm();
    FileUtil file = new FileUtil();
    Random random = new Random();



    public String genPhoneNum() {
        String num = "177" + (random.nextInt(89999999) + 10000000);

        return num;
    }

    public String getname(JSONArray userlist, String sale_id) {
        String userLoginName = "";
        for (int i = 0; i < userlist.size(); i++) {
            JSONObject obj = userlist.getJSONObject(i);
            if (obj.getString("user_id").equals(sale_id)) {
                userLoginName = obj.getString("user_login_name");
                break;
            }
        }
        return userLoginName;
    }

    public String username(String sale_id) throws Exception {
        crm.login(pp.superManger,pp.superpassword);
        String userLoginName = "";
        JSONArray userlist = crm.userPage(1, 100).getJSONArray("list");
        userLoginName = getname(userlist, sale_id);
        if (userLoginName.equals("")) {
            JSONArray userlist2 = crm.userPage(2, 100).getJSONArray("list");
            userLoginName = getname(userlist2, sale_id);
        }
        return userLoginName;
    }

    public JSONArray phoneList(String phonenum, String phonenum2) {
        JSONArray PhoneList = new JSONArray();
        JSONObject phone1 = new JSONObject();
        phone1.put("phone", phonenum);
        phone1.put("phone_order", 0);
        JSONObject phone2 = new JSONObject();
        phone2.put("phone", phonenum2);
        phone2.put("phone_order", 1);
        PhoneList.add(0, phone1);
        PhoneList.add(1, phone2);
        return PhoneList;
    }


    //pc?????????????????????????????????id?????????id
    public Long[] createAArcile_id(String valid_start, String simulation_num) throws Exception {
        Long article_id;
        Long[] aid = new Long[2];
        crm.login(pp.zongjingli, pp.adminpassword);
        String[] customer_types = {"PRE_SALES", "AFTER_SALES"};
        int[] customer_level = {};
        String[] customer_property = {};
        String positions = pp.positions; //???????????????????????? ??????
        String valid_end = dt.getHistoryDate(4);
        int[] car_types = {};
        String article_title = "app????????????????????????????????????????????????4???---" + dt.getHistoryDate(0);
        String article_bg_pic = file.texFile(pp.filePath);  //base 64
        String article_content = "????????????????????????????????????4???,????????????";
        String article_remarks = "????????????????????????????????????4???,??????";

        boolean is_online_activity;  //????????????????????????
        is_online_activity = true;
//            String reception_name = manage(13)[0];  //???????????????
//            String reception_phone = manage(13)[1]; //??????????????????
        String reception_name = pp.reception_name;  //???????????????
        String reception_phone = pp.reception_phone; //??????????????????
        String customer_max = "50";                    //????????????

        String activity_start = dt.getHistoryDate(1);
        String activity_end = dt.getHistoryDate(4);
        Integer role_id = 13;  //13 ??????  15???????????????????????????     16??????
        Boolean is_create_poster = true;//??????????????????
        int task_customer_num = 5;
        //???????????????????????????/??????id
        article_id = crm.createArticle(positions, valid_start, valid_end, customer_types, car_types, customer_level, customer_property, article_title, false, article_bg_pic, article_content, article_remarks, is_online_activity, reception_name, reception_phone, customer_max, simulation_num, activity_start, activity_end, role_id, Integer.toString(task_customer_num), is_create_poster).getLong("id");
        Long activity_id = crm.appartilceDetail(article_id, positions).getLong("activity_id");
        aid[0] = article_id;  //??????id
        aid[1] = activity_id;  //??????id
        return aid;
    }

    public Long[] createAArcile_idRole(String valid_start, String simulation_num, Integer role_id) throws Exception {
        Long article_id;
        Long[] aid = new Long[2];
        crm.login(pp.zongjingli, pp.adminpassword);
        String[] customer_types = {"PRE_SALES", "AFTER_SALES"};
        int[] customer_level = {};
        String[] customer_property = {};
        String positions = pp.positions; //???????????????????????? ??????
        String valid_end = dt.getHistoryDate(4);
        int[] car_types = {};
        String article_title = "app????????????(??????)" + dt.getHistoryDate(0);
        String article_bg_pic = file.texFile(pp.filePath);  //base 64
        String article_content = "????????????????????????????????????4???,????????????";
        String article_remarks = "????????????????????????????????????4???,??????";

        boolean is_online_activity;  //????????????????????????
        is_online_activity = true;
//            String reception_name = manage(13)[0];  //???????????????
//            String reception_phone = manage(13)[1]; //??????????????????
        String reception_name = pp.reception_name;  //???????????????
        String reception_phone = pp.reception_phone; //??????????????????
        String customer_max = "50";                    //????????????

        String activity_start = dt.getHistoryDate(1);
        String activity_end = dt.getHistoryDate(4);
//        Integer role_id = 13;  //13 ??????  15???????????????????????????     16??????
        Boolean is_create_poster = true;//??????????????????
        int task_customer_num = 5;
        //???????????????????????????/??????id
        article_id = crm.createArticle(positions, valid_start, valid_end, customer_types, car_types, customer_level, customer_property, article_title, false, article_bg_pic, article_content, article_remarks, is_online_activity, reception_name, reception_phone, customer_max, simulation_num, activity_start, activity_end, role_id, Integer.toString(task_customer_num), is_create_poster).getLong("id");
        Long activity_id = crm.appartilceDetail(article_id, positions).getLong("activity_id");
        aid[0] = article_id;  //??????id
        aid[1] = activity_id;  //??????id
        return aid;
    }

    //????????????????????????id
    public Long createArcile(String positionsA, String article_title) throws Exception {
        String[] customer_types = {"PRE_SALES", "AFTER_SALES"};
        int[] customer_level = {};                                //????????????
        String[] customer_property = {};
        String valid_start = dt.getHistoryDate(0);
        String valid_end = dt.getHistoryDate(4);
        int[] car_types = {};
        String article_bg_pic = file.texFile(pp.filePath);  //base 64
        String article_remarks = "????????????????????????????????????4???,??????";
        //?????????????????????id
        return crm.createArticleReal(positionsA, valid_start, valid_end, customer_types, car_types, customer_level, customer_property, article_title, false, article_bg_pic, pp.article_content, article_remarks, false).getLong("id");
    }

    //??????????????????????????????????????????
    public JSONObject creatCust() throws Exception {
        //????????????
        crm.login(pp.qiantai, pp.adminpassword);
        JSONObject jsonP = new JSONObject();
        String name = "auto" + dt.getHHmm(0);
        String phone = genPhoneNum();
        //?????????????????????????????????id
        String sale_id = crm.freeSaleList().getJSONArray("list").getJSONObject(0).getString("sale_id");
        String userLoginName = username(sale_id);
        boolean isDes = true;
        JSONObject list = new JSONObject();
        JSONArray ll = new JSONArray();
        list.put("customer_name", name);
        list.put("is_decision", isDes);
        ll.add(0, list);

        //????????????
        crm.creatReception2("FIRST_VISIT", ll);
        crm.login(userLoginName, pp.adminpassword);

        JSONObject data = crm.customerMyReceptionList("", "", "", 10, 1);

        Long receiptId = data.getJSONArray("list").getJSONObject(0).getLong("id");
        Long customerID = data.getJSONArray("list").getJSONObject(0).getLong("customer_id");

        JSONArray PhoneList = new JSONArray();
        PhoneList = phoneList(phone, "");

        jsonP.put("name", name);
        jsonP.put("phone", phone);
        jsonP.put("phoneList", PhoneList);
        jsonP.put("reception_id", receiptId);
        jsonP.put("customerId", customerID);
        jsonP.put("userLoginName", userLoginName);
        jsonP.put("sale_id", sale_id);

        return jsonP;
    }
    //???????????????????????????
    public JSONObject creatCust2(String phone) throws Exception {
        //????????????
        crm.login(pp.qiantai, pp.adminpassword);
        JSONObject jsonP = new JSONObject();
        String name = pp.customer_name;
        //?????????????????????????????????id
        String sale_id = crm.freeSaleList().getJSONArray("list").getJSONObject(0).getString("sale_id");
        String userLoginName = username(sale_id);
        boolean isDes = true;
        JSONObject list = new JSONObject();
        JSONArray ll = new JSONArray();
        list.put("customer_name", name);
        list.put("is_decision", isDes);
        ll.add(0, list);

        //????????????
        crm.login(pp.qiantai, pp.adminpassword);
        crm.creatReception2("FIRST_VISIT", ll);
        crm.login(userLoginName, pp.adminpassword);

        JSONObject data = crm.customerMyReceptionList("", "", "", 10, 1);

        Long receiptId = data.getJSONArray("list").getJSONObject(0).getLong("id");
        Long customerID = data.getJSONArray("list").getJSONObject(0).getLong("customer_id");

        JSONArray PhoneList = new JSONArray();
        PhoneList = phoneList(phone, "");

        jsonP.put("name", name);
        jsonP.put("phone", phone);
        jsonP.put("phoneList", PhoneList);
        jsonP.put("reception_id", receiptId);
        jsonP.put("customerId", customerID);
        jsonP.put("userLoginName", userLoginName);
        jsonP.put("sale_id", sale_id);

        return jsonP;
    }

    //??????????????????????????????????????????
    public JSONObject creatCustOld(String phone) throws Exception {
        JSONObject jsonCO = new JSONObject();
        //????????????
        crm.login(pp.qiantai, pp.adminpassword);
        //???????????????
        JSONObject data = crm.phoneCheck(phone);
        Long customer_id = data.getLong("customer_id");
        String belongs_sale_id = data.getString("belongs_sale_id");
        String userLoginName = username(belongs_sale_id);
        if (customer_id != null) {
            crm.receptionOld(customer_id, "AGAIN_VISIT");
            //?????????????????????????????????id
            crm.login(userLoginName, pp.adminpassword);
            Long id = 0L;
            Long customerId = 0L;
            Long receiptId = 0L;
            JSONArray dataC = crm.customerMyReceptionList("", "", "", 10, 1).getJSONArray("list");
            for (int i = 0; i < dataC.size(); i++) {
                id = dataC.getJSONObject(i).getLong("id");
                customerId = dataC.getJSONObject(i).getLong("customer_id");
                receiptId = dataC.getJSONObject(i).getLong("id");
                String customer_phone = dataC.getJSONObject(i).getString("customer_phone");
                if (customer_phone.equals(phone)) {
                    break;
                }
            }
            JSONArray PhoneList;
            PhoneList = phoneList(phone, "");
            jsonCO.put("id", id);
            jsonCO.put("customerId", customerId);
            jsonCO.put("userLoginName", userLoginName);
            jsonCO.put("sale_id", belongs_sale_id);
            jsonCO.put("reception_id", receiptId);
            jsonCO.put("phoneList", PhoneList);
        } else {
            jsonCO = creatCust2(phone);
        }
        return jsonCO;
    }

    //?????????????????????
    public String getdrivercarStyleName(Long carId) {
        String CarStyleName = "";
        JSONArray list = crm.driverCarList().getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            CarStyleName = list.getJSONObject(i).getString("car_style_name");
            Long id = list.getJSONObject(i).getLong("test_car_id");
            if (id.equals(carId)) {
                break;
            }
        }
        return CarStyleName;
    }

    //????????????+???????????? ok
    public String creatDriver(Long receptionId, Long customer_id, String name, String phone, int audit_status) throws Exception {  //1-?????????2-??????

        String driverLicensePhoto1Url = file.texFile(pp.filePath);
        String sign_date = dt.getHistoryDate(0);
        String sign_time = dt.getHHmm(0);

        JSONArray list = crm.testDriverList().getJSONArray("list");
        String apply_time = "";
        Long test_drive_car = dt.getHistoryDateTimestamp(1);
        for (int i = 0; i < list.size(); i++) {
            test_drive_car = list.getJSONObject(i).getLong("test_car_id");
            JSONArray timelist = crm.driverTimelist(test_drive_car).getJSONArray("list");
            if (timelist.size() != 0) {
                apply_time = timelist.getString(0);
                break;
            }
        }
        String car_style_name = getdrivercarStyleName(test_drive_car);
        String oss = crm.addressDiscernment(driverLicensePhoto1Url).getString("license_face_path");

        crm.driveradd5(receptionId, customer_id, name, phone, driverLicensePhoto1Url, sign_date, sign_time, apply_time.toString(), test_drive_car, oss);
        //??????????????????
        crm.login(pp.xiaoshouZongjian, pp.adminpassword);
        int driverid = crm.testDriverAppList("", "", "", 10, 1).getJSONArray("list").getJSONObject(0).getInteger("id");
        if (audit_status == 1 || audit_status == 2) {
            crm.driverAudit(driverid, audit_status);
        }
        return car_style_name;
        //??????????????????????????????
    }

    //??????+????????????  copy lxq debug ok
    public void creatDeliver(Long reception_id, Long customer_id, String customer_name, String deliver_car_time, Boolean accept_show) throws Exception {
        //??????
        String vehicle_chassis_code = "ASD123456" + (random.nextInt(89999999) + 10000000);
        Long car_id = crm.addOrderCar(customer_id.toString(), reception_id.toString(), vehicle_chassis_code).getLong("car_id");
        //????????????
        Long model = crm.customerOrderCar(customer_id.toString()).getJSONArray("list").getJSONObject(0).getLong("car_model_id");
        String path = file.texFile(pp.filePath);
        crm.deliverAdd(car_id, reception_id, customer_id, customer_name, deliver_car_time, model, path, accept_show, path, vehicle_chassis_code);
    }

    //????????????????????????---for??????
    public Long driverEva() throws Exception {
        crm.appletLoginToken(EnumAppletToken.BSJ_XMF_DAILY.getToken());
        JSONObject data = crm.appointmentTestDrive("MALE", pp.customer_name, pp.customer_phone_number, dt.getHistoryDate(0), pp.car_type, pp.car_model);
        //??????????????????????????????????????????
        Long appointment_id = data.getLong("appointment_id");
        //??????????????????
        JSONObject json;
        json = creatCustOld(pp.customer_phone_number);
        FinishReception fr = new FinishReception();
        fr.name = pp.customer_name;
        fr.reception_id = json.getString("id");
        fr.customer_id = json.getString("customerId");
        fr.belongs_sale_id = json.getString("sale_id");
        fr.phoneList = json.getJSONArray("phoneList");
        fr.reception_type = "BB";
        fr.remark = new JSONArray();
        String userLoginName = json.getString("userLoginName");
        String phone = json.getString("phone");
        //????????????,????????????
        creatDriver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), pp.customer_name, pp.customer_phone_number, 1);
        crm.login(userLoginName, pp.adminpassword);            //????????????????????????
        crm.finishReception3(fr);
        return appointment_id;
    }

    //??????(??????)??????????????????
    public Integer jiedaiTimes(int roleId, String guwen) throws Exception {
        crm.login(pp.zongjingli, pp.adminpassword);
        JSONArray list = crm.ManageList(roleId).getJSONArray("list");
        if (list == null || list.size() == 0) {
            return 0;
        }
        int num = 0;
        for (int i = 0; i < list.size(); i++) {
            String name = list.getJSONObject(i).getString("name");
            if (name.equals(guwen)) {
                num = list.getJSONObject(i).getInteger("num");
            }
        }
        return num;
    }

    public void createCar(String car_type_name) throws Exception {

        double lowest_price = 88.99;
        double highest_price = 888.99;
        String car_discount = "???????????????????????????????????????";
        String car_introduce = "?????????Boxster??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
        String car_pic = file.texFile(pp.filePath);  //base 64
        String big_pic = file.texFile(pp.filePath);  //base 64
        String interior_pic = file.texFile(pp.filePath);  //base 64
        String space_pic = file.texFile(pp.filePath);  //base 64
        String appearance_pic = file.texFile(pp.filePath);  //base 64
        crm.addCarPc(car_type_name, lowest_price, highest_price, car_discount, car_introduce, car_pic, big_pic, interior_pic, space_pic, appearance_pic);
    }

    //????????????
    public Long createCarcode(String car_type_name) throws Exception {

        double lowest_price = 88.99;
        double highest_price = 8888.99;
        String car_discount = "???????????????????????????????????????";
        String car_introduce = "?????????Boxster??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
        String car_pic = file.texFile(pp.filePath);  //base 64
        String big_pic = file.texFile(pp.filePath);  //base 64
        String interior_pic = file.texFile(pp.filePath);  //base 64
        String space_pic = file.texFile(pp.filePath);  //base 64
        String appearance_pic = file.texFile(pp.filePath);  //base 64
        Long code = crm.addCarPccode(car_type_name, lowest_price, highest_price, car_discount, car_introduce, car_pic, big_pic, interior_pic, space_pic, appearance_pic);
        return code;
    }

    //??????????????????????????????
    public Long appointmentTimeList(String type, int i, String appointment_date) throws Exception {
        return crm.timeList(type, appointment_date).getJSONArray("list").getJSONObject(i).getLong("id");
    }

    public JSONObject appointmentTimeListO(String type, String appointment_date) throws Exception {
        JSONObject result = new JSONObject();
        JSONArray object = crm.timeList(type, appointment_date).getJSONArray("list");
        for (int i = 1; i < object.size(); i++) {
            JSONObject data = object.getJSONObject(object.size()-i);
            int left_num = data.getInteger("left_num");
            if (left_num != 0) {
                Long id = data.getLong("id");
                String time = data.getString("start_time");
                result.put("time_id", id);
                result.put("start_time", time);
                break;
            }
        }
        return result;
    }


    //??????????????????????????????
    public void deleteUser(String filePath) {
        List<String> list = file.getFileContent(filePath);
        for (String value : list) {
            JSONArray anwer = crm.customerListPC(value, 1, 10).getJSONArray("list");
            Long customer_id = anwer.getJSONObject(0).getLong("customer_id");
            if (anwer.size() != 0) {
                crm.customerDeletePC(customer_id);
            }
        }
    }

    //??????????????????????????????????????????????????????????????????
    public int[] statusNum() throws Exception {
        JSONObject data = crm.articlePage(1, 10, pp.positions);
        int[] aa = {0, 0, 0, 0};
        aa[2] = data.getInteger("total");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            String status = list.getJSONObject(i).getString("status_name");
            if (status.equals("?????????")) {
                aa[0]++;
            } else if (status.equals("?????????")) {
                aa[1]++;
            } else if (status.equals("?????????")) {
                aa[3]++;
            }
        }
        return aa;
    }

    //?????????????????????id
    public long[] carModelId() {
        JSONArray list = crm.carStyleList().getJSONArray("list");
        long id[] = {0, 0};
        id[0] = list.getJSONObject(0).getLong("id");
        JSONArray list2 = crm.carModelList(id[0]).getJSONArray("list");
        id[2] = list2.getJSONObject(0).getLong("id");
        return id;
    }

    //???????????????
    public long newCarDriver() throws Exception {
        Random r = new Random();
        String carName = "?????????" + r.nextInt(1000) + dt.getHHmm(0);
//        long id[]=carModelId();      //0 ????????????id, 1 ??????id
        String plate_number = "???Z12I1" + r.nextInt(100);
        String vehicle_chassis_code = "ASD145656" + (random.nextInt(89999999) + 10000000);
        Long start = dt.getHistoryDateTimestamp(-1);
        long end = dt.getHistoryDateTimestamp(3);
        JSONObject data = crm.carManagementAdd(carName, 1L, Long.valueOf(pp.car_model), plate_number, vehicle_chassis_code, start, end);
        return data.getLong("test_car_id");
    }

    //?????????????????????
    public int[] driverSum() {
        int a[] = new int[3];
        JSONObject dataTotal = crm.driverTotal();
        a[0] = dataTotal.getInteger("today_test_drive_total");   //??????
        a[1] = dataTotal.getInteger("test_drive_total");         //??????
        JSONObject dataList1 = crm.driverSelect(1, 10);
        a[2] = dataList1.getInteger("total");         //?????????
        return a;
    }

    //?????????????????????
    public int[] deliverSum() {
        int a[] = new int[4];
        JSONObject dataTotal = crm.jiaocheTotal();
        a[0] = dataTotal.getInteger("today_deliver_car_total");   //??????
        a[1] = dataTotal.getInteger("deliver_car_total");    //????????????
        a[2] = dataTotal.getInteger("total_order");         //????????????
        JSONObject data = crm.deliverSelect(1, 10);
        a[3] = data.getInteger("total");   //?????????
        return a;
    }

    //????????????????????????
    public int[] receiptSum() {
        int a[] = new int[5];
        JSONObject dataTotal = crm.customerReceptionTotalInfo();
        a[0] = dataTotal.getInteger("total_reception");   //????????????
        a[1] = dataTotal.getInteger("today_new_customer");    //??????????????????
        a[2] = dataTotal.getInteger("today_order");         //????????????
        a[3] = dataTotal.getInteger("total_old_customer");         //??????????????????
        a[4] = crm.customerMyReceptionList("", "", "", 1, 10).getInteger("total");

        return a;
    }

    //???????????????
    public JSONObject cartime() {
        JSONArray list = crm.testDriverList().getJSONArray("list");
        JSONObject carMess = new JSONObject();
        for (int i = 0; i < list.size(); i++) {
            Long test_drive_car = list.getJSONObject(i).getLong("test_car_id");
            JSONArray timelist = crm.driverTimelist(test_drive_car).getJSONArray("list");
            if (timelist.size() != 0) {
                String apply_time = timelist.getString(0);
                carMess.put("test_car_id", test_drive_car);
                carMess.put("apply_time", apply_time);
                break;
            }
        }
        return carMess;
    }

    //????????????????????????id
    public String createUserId(String userName, int roleId) throws Exception {
        crm.login(pp.zongjingli, pp.superpassword);
        //????????????/??????
        String phone = genPhoneNum();

        JSONObject data1 = crm.userPage(1, 100);
        int total = data1.getInteger("total");
        JSONArray list;
        if (total == 200) {
            throw new Exception("???????????????????????????case????????????");
        }
        else {
            crm.addUser(userName, userName, phone, pp.adminpassword, roleId, "", "");
            list = crm.userPage(1, 100).getJSONArray("list");
        }
        String userid = list.getJSONObject(0).getString("user_id"); //????????????id ?????? ??????????????????
        return userid;
    }

    //?????????????????????
    public int[] qtcustomer(JSONArray list) {
        int num[] = new int[2];
        for (int i = 0; i < list.size(); i++) {
            String customer_identity_name = list.getJSONObject(i).getString("customer_identity_name");
            if (customer_identity_name.equals("??????")) {
                num[0]++;
            } else {
                num[1]++;
            }
        }
        return num;
    }

    //??????????????????????????????
    public JSONObject customermess(JSONArray list, String type) {
        JSONObject date = new JSONObject();
        String analysis_customer_id = "";
        String customer_id = "";
        for (int i = 0; i < list.size(); i++) {
            String customer_identity_name = list.getJSONObject(i).getString("customer_identity_name");
            if (customer_identity_name.equals(type)) {
                analysis_customer_id = list.getJSONObject(i).getString("analysis_customer_id");
                customer_id = list.getJSONObject(i).getString("customer_id");
                date.put("analysis_customer_id", analysis_customer_id);
                date.put("customer_id", customer_id);
                break;
            }
        }
        return date;
    }

}
