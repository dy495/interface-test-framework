package com.haisheng.framework.testng.bigScreen.itemPorsche.caseonline.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.PublicParmOnline;
import com.haisheng.framework.testng.bigScreen.itemPorsche.caseonline.xmf.interfaceOnline.FinishReceptionOnline;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;

import java.util.List;
import java.util.Random;

public class PackFunctionOnline {
    CrmScenarioUtilOnlineX crm = CrmScenarioUtilOnlineX.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParmOnline pp=new PublicParmOnline();
    FileUtil file=new FileUtil();
    Random random=new Random();

    public String genPhoneNum() {
        String num = "177" + (random.nextInt(89999999) + 10000000);

        return num;
    }

    public String username(String sale_id) throws Exception {
        crm.login(pp.superManger,pp.superpassword);
        String userLoginName = "";
        JSONArray userlist = crm.userPage(1, 100).getJSONArray("list");
        for (int i = 0; i < userlist.size(); i++) {
            JSONObject obj = userlist.getJSONObject(i);

            if (obj.getString("user_id").equals(sale_id)) {
                userLoginName = obj.getString("user_login_name");
            }
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


    //pc新建活动方法，返回文章id和文章id
    public Long[] createAArcile_id(String valid_start, String simulation_num) throws Exception {
        Long article_id;
        Long[] aid = new Long[2];
        crm.login(pp.zongjingli, pp.adminpassword);
        String[] customer_types = {"PRE_SALES", "AFTER_SALES"};
        int[] customer_level = {};
        String[] customer_property = {};
        String positions = pp.positions; //投放位置车型推荐 单选
        String valid_end = dt.getHistoryDate(4);
        int[] car_types = {};
        String article_title = "app任务报名品牌上新，优惠多多，限时4天---" + dt.getHistoryDate(0);
        String article_bg_pic = file.texFile(pp.filePath);  //base 64
        String article_content = "品牌上新，优惠多多，限时4天,活动内容";
        String article_remarks = "品牌上新，优惠多多，限时4天,备注";

        boolean is_online_activity;  //是否线上报名活动
        is_online_activity = true;
//            String reception_name = manage(13)[0];  //接待人员名
//            String reception_phone = manage(13)[1]; //接待人员电话
        String reception_name = pp.reception_name;  //接待人员名
        String reception_phone = pp.reception_phone; //接待人员电话
        String customer_max = "50";                    //人数上限

        String activity_start = dt.getHistoryDate(1);
        String activity_end = dt.getHistoryDate(4);
        Integer role_id = 13;  //13 销售  15定损（原维修顾问）     16服务
        Boolean is_create_poster = true;//是否生成海报
        int task_customer_num = 5;
        //新建文章并返回文章/活动id
        article_id = crm.createArticle(positions, valid_start, valid_end, customer_types, car_types, customer_level, customer_property, article_title, false, article_bg_pic, article_content, article_remarks, is_online_activity, reception_name, reception_phone, customer_max, simulation_num, activity_start, activity_end, role_id, Integer.toString(task_customer_num), is_create_poster).getLong("id");
        Long activity_id = crm.appartilceDetail(article_id, positions).getLong("activity_id");
        aid[0] = article_id;  //文章id
        aid[1] = activity_id;  //活动id
        return aid;
    }

    public Long[] createAArcile_idRole(String valid_start, String simulation_num, Integer role_id) throws Exception {
        Long article_id;
        Long[] aid = new Long[2];
        crm.login(pp.zongjingli, pp.adminpassword);
        String[] customer_types = {"PRE_SALES", "AFTER_SALES"};
        int[] customer_level = {};
        String[] customer_property = {};
        String positions = pp.positions; //投放位置车型推荐 单选
        String valid_end = dt.getHistoryDate(4);
        int[] car_types = {};
        String article_title = "app任务报名(售后)" + dt.getHistoryDate(0);
        String article_bg_pic = file.texFile(pp.filePath);  //base 64
        String article_content = "品牌上新，优惠多多，限时4天,活动内容";
        String article_remarks = "品牌上新，优惠多多，限时4天,备注";

        boolean is_online_activity;  //是否线上报名活动
        is_online_activity = true;
//            String reception_name = manage(13)[0];  //接待人员名
//            String reception_phone = manage(13)[1]; //接待人员电话
        String reception_name = pp.reception_name;  //接待人员名
        String reception_phone = pp.reception_phone; //接待人员电话
        String customer_max = "50";                    //人数上限

        String activity_start = dt.getHistoryDate(1);
        String activity_end = dt.getHistoryDate(4);
//        Integer role_id = 13;  //13 销售  15定损（原维修顾问）     16服务
        Boolean is_create_poster = true;//是否生成海报
        int task_customer_num = 5;
        //新建文章并返回文章/活动id
        article_id = crm.createArticle(positions, valid_start, valid_end, customer_types, car_types, customer_level, customer_property, article_title, false, article_bg_pic, article_content, article_remarks, is_online_activity, reception_name, reception_phone, customer_max, simulation_num, activity_start, activity_end, role_id, Integer.toString(task_customer_num), is_create_poster).getLong("id");
        Long activity_id = crm.appartilceDetail(article_id, positions).getLong("activity_id");
        aid[0] = article_id;  //文章id
        aid[1] = activity_id;  //活动id
        return aid;
    }

    //创建文章返回文章id
    public Long createArcile(String positionsA, String article_title) throws Exception {
        String[] customer_types = {"PRE_SALES", "AFTER_SALES"};
        int[] customer_level = {};                                //客户等级
        String[] customer_property = {};
        String valid_start = dt.getHistoryDate(0);
        String valid_end = dt.getHistoryDate(4);
        int[] car_types = {};
        String article_bg_pic = file.texFile(pp.filePath);  //base 64
        String article_remarks = "品牌上新，优惠多多，限时4天,备注";
        //新建文章，获取id
        return crm.createArticleReal(positionsA, valid_start, valid_end, customer_types, car_types, customer_level, customer_property, article_title, false, article_bg_pic, pp.article_content, article_remarks, false).getLong("id");
    }

    //前台点击创建接待按钮创建顾客
    public JSONObject creatCust() throws Exception {
        //前台登陆
        crm.login(pp.qiantai, pp.adminpassword);
        JSONObject jsonP = new JSONObject();
        String name = "auto" + dt.getHHmm(0);
        String phone = genPhoneNum();
        //获取当前空闲第一位销售id
        String sale_id = crm.freeSaleList().getJSONArray("list").getJSONObject(0).getString("sale_id");
        String userLoginName = username(sale_id);
        boolean isDes = true;
        JSONObject list = new JSONObject();
        JSONArray ll = new JSONArray();
        list.put("customer_name", name);
        list.put("is_decision", isDes);
        ll.add(0, list);

        //创建接待
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

    public JSONObject creatCust2(String phone) throws Exception {
        //前台登陆
        crm.login(pp.qiantai, pp.adminpassword);
        JSONObject jsonP = new JSONObject();
        String name = pp.customer_name;
        //获取当前空闲第一位销售id
        String sale_id = crm.freeSaleList().getJSONArray("list").getJSONObject(0).getString("sale_id");
        String userLoginName = username(sale_id);
        boolean isDes = true;
        JSONObject list = new JSONObject();
        JSONArray ll = new JSONArray();
        list.put("customer_name", name);
        list.put("is_decision", isDes);
        ll.add(0, list);

        //创建接待
        crm.login(pp.qiantai,pp.qtpassword);
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

    //前台点击创建接待按钮接待老客
    public JSONObject creatCustOld(String phone) throws Exception {
        JSONObject jsonCO = new JSONObject();
        //前台登陆
        crm.login(pp.qiantai, pp.adminpassword);
        //搜索手机号
        JSONObject data = crm.phoneCheck(phone);
        Long customer_id = data.getLong("customer_id");
        String belongs_sale_id = data.getString("belongs_sale_id");
        String userLoginName = username(belongs_sale_id);
        if(customer_id!=null) {
            crm.receptionOld(customer_id, "AGAIN_VISIT");
            //销售登陆，获取当前接待id
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
                if (customer_phone.equals(phone)){
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
        }else {
            jsonCO=creatCust2(phone);
        }
        return jsonCO;
    }
    //获取试驾车系名
    public String getdrivercarStyleName(Long carId){
        String CarStyleName="";
        JSONArray list=crm.driverCarList().getJSONArray("list");
        for(int i=0;i<list.size();i++){
            CarStyleName=list.getJSONObject(i).getString("car_style_name");
            Long id=list.getJSONObject(i).getLong("test_car_id");
            if(id.equals(carId)){
                break;
            }
        }
        return CarStyleName;
    }

    //新建试驾+审核封装 ok
    public String creatDriver(Long receptionId, Long customer_id, String name, String phone, int audit_status) throws Exception {  //1-通过，2-拒绝

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
        //销售总监登陆
        crm.login(pp.xiaoshouZongjian, pp.adminpassword);
        int driverid = crm.testDriverAppList("", "", "", 10, 1).getJSONArray("list").getJSONObject(0).getInteger("id");
        if (audit_status == 1 || audit_status == 2) {
            crm.driverAudit(driverid, audit_status);
        }
        return car_style_name;
        //最后销售要再登陆一次
    }
    //订车+交车封装  copy lxq debug ok
    public void creatDeliver(Long reception_id, Long customer_id, String customer_name, String deliver_car_time, Boolean accept_show) throws Exception {
        //订车
//        crm.orderCar(customer_id);
        String vehicle_chassis_code = "ASD123456" + (random.nextInt(89999999) + 10000000);
        Long car_id = crm.addOrderCar(customer_id.toString(), reception_id.toString(), vehicle_chassis_code).getLong("car_id");
        //创建交车
        Long model = crm.customerOrderCar(customer_id.toString()).getJSONArray("list").getJSONObject(0).getLong("car_model_id");
        String path = file.texFile(pp.filePath);
        crm.deliverAdd(car_id, reception_id, customer_id, customer_name, deliver_car_time, model, path, accept_show, path, vehicle_chassis_code);
    }

    //老客试驾完成接待---for评价
    public Long driverEva() throws Exception {
        crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
        JSONObject data = crm.appointmentTestDrive("MALE", pp.customer_name, pp.customer_phone_number, dt.getHistoryDate(0), pp.car_type, pp.car_model);
        //预约试驾成功后，页面显示数据
        Long appointment_id = data.getLong("appointment_id");
        //前台分配老客
        JSONObject json;
        json = creatCustOld(pp.customer_phone_number);
        FinishReceptionOnline fr = new FinishReceptionOnline();
        fr.name = pp.customer_name;
        fr.reception_id = json.getString("id");
        fr.customer_id = json.getString("customerId");
        fr.belongs_sale_id = json.getString("sale_id");
        fr.phoneList = json.getJSONArray("phoneList");
        fr.reception_type = "BB";
        fr.remark = new JSONArray();
        String userLoginName = json.getString("userLoginName");
        String phone = json.getString("phone");
        //新建试驾,审核通过
        creatDriver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), pp.customer_name, pp.customer_phone_number, 1);
        crm.login(userLoginName, pp.adminpassword);            //销售登录完成接待
        crm.finishReception3(fr);
        return appointment_id;
    }

    //获取(销售)顾问接待次数
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
        String car_discount = "跑车多数人知道，少数人了解";
        String car_introduce = "保时捷Boxster是保时捷公司的一款双门双座敞篷跑车，引擎采中置后驱设计，最早以概念车形式亮相于北美车展展出。";
        String car_pic = file.texFile(pp.filePath);  //base 64
        String big_pic = file.texFile(pp.filePath);  //base 64
        String interior_pic = file.texFile(pp.filePath);  //base 64
        String space_pic = file.texFile(pp.filePath);  //base 64
        String appearance_pic = file.texFile(pp.filePath);  //base 64
        crm.addCarPc(car_type_name, lowest_price, highest_price, car_discount, car_introduce, car_pic, big_pic, interior_pic, space_pic, appearance_pic);
    }

    //创建车辆
    public Long createCarcode(String car_type_name) throws Exception {

        double lowest_price = 88.99;
        double highest_price = 8888.99;
        String car_discount = "跑车多数人知道，少数人了解";
        String car_introduce = "保时捷Boxster是保时捷公司的一款双门双座敞篷跑车，引擎采中置后驱设计，最早以概念车形式亮相于北美车展展出。";
        String car_pic = file.texFile(pp.filePath);  //base 64
        String big_pic = file.texFile(pp.filePath);  //base 64
        String interior_pic = file.texFile(pp.filePath);  //base 64
        String space_pic = file.texFile(pp.filePath);  //base 64
        String appearance_pic = file.texFile(pp.filePath);  //base 64
        Long code = crm.addCarPccode(car_type_name, lowest_price, highest_price, car_discount, car_introduce, car_pic, big_pic, interior_pic, space_pic, appearance_pic);
        return code;
    }

    //预约剩余时段次数查询
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


    //删除文本中手机号用户
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

    //查询活动列表，已下架状态，显示中状态，总列数
    public int[] statusNum() throws Exception {
        JSONObject data = crm.articlePage(1, 10, pp.positions);
        int[] aa = {0, 0, 0, 0};
        aa[2] = data.getInteger("total");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            String status = list.getJSONObject(i).getString("status_name");
            if (status.equals("显示中")) {
                aa[0]++;
            } else if (status.equals("已下架")) {
                aa[1]++;
            } else if (status.equals("排期中")) {
                aa[3]++;
            }
        }
        return aa;
    }

    //小程序获取车型id
    public long[] carModelId() {
        JSONArray list = crm.carStyleList().getJSONArray("list");
        long id[] = {0, 0};
        id[0] = list.getJSONObject(0).getLong("id");
        JSONArray list2 = crm.carModelList(id[0]).getJSONArray("list");
        id[2] = list2.getJSONObject(0).getLong("id");
        return id;
    }

    //添加试驾车
    public long newCarDriver(String carName) throws Exception {
        Random r = new Random();
//        long id[]=carModelId();      //0 试驾车系id, 1 车型id
        String plate_number = "黑Z12I1" + r.nextInt(100);
        String vehicle_chassis_code = "ASD145656" + (random.nextInt(89999999) + 10000000);
        Long start = dt.getHistoryDateTimestamp(-1);
        long end = dt.getHistoryDateTimestamp(3);
        JSONObject data = crm.carManagementAdd(carName, 1L, Long.valueOf(pp.car_model), plate_number, vehicle_chassis_code, start, end);
        return data.getLong("test_car_id");
    }

    //查询试驾数统计
    public int[] driverSum() {
        int a[] = new int[3];
        JSONObject dataTotal = crm.driverTotal();
        a[0] = dataTotal.getInteger("today_test_drive_total");   //今日
        a[1] = dataTotal.getInteger("test_drive_total");         //全部
        JSONObject dataList1 = crm.driverSelect(1, 10);
        a[2] = dataList1.getInteger("total");         //列表数
        return a;
    }

    //查询交车数统计
    public int[] deliverSum() {
        int a[] = new int[4];
        JSONObject dataTotal = crm.jiaocheTotal();
        a[0] = dataTotal.getInteger("today_deliver_car_total");   //今日
        a[1] = dataTotal.getInteger("deliver_car_total");    //实际交车
        a[2] = dataTotal.getInteger("total_order");         //全部交车
        JSONObject data = crm.deliverSelect(1, 10);
        a[3] = data.getInteger("total");   //列表数
        return a;
    }

    //接待列表数从查询
    public int[] receiptSum() {
        int a[] = new int[5];
        JSONObject dataTotal = crm.customerReceptionTotalInfo();
        a[0] = dataTotal.getInteger("total_reception");   //共计接待
        a[1] = dataTotal.getInteger("today_new_customer");    //今日新客接待
        a[2] = dataTotal.getInteger("today_order");         //今日订单
        a[3] = dataTotal.getInteger("total_old_customer");         //今日老客接待
        a[4] = crm.customerMyReceptionList("", "", "", 1, 10).getInteger("total");

        return a;
    }

    //可用试驾车
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

    //创建用户返回用户id
    public String createUserId(String userName,int roleId)throws Exception{
        crm.login(pp.zongjingli,pp.superpassword);
        //创建销售/顾问
        String phone=genPhoneNum();

        crm.addUser(userName,userName, phone,pp.adminpassword,roleId,"","");
        JSONObject data=crm.userPage(1,100);
        int total=data.getInteger("total");
        JSONArray list;
        if(total==200){
            throw new Exception("用户数量已达上线，case运行终止");
        }
        else if(total<100){
            list = data.getJSONArray("list");
        }else{
            list=crm.userPage(2,100).getJSONArray("list");
        }
        String userid = list.getJSONObject(list.size()-1).getString("user_id"); //获取用户id
        return userid;
    }

}
