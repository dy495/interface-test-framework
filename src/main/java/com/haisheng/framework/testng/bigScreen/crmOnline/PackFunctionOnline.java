package com.haisheng.framework.testng.bigScreen.crmOnline;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.experiment.enumerator.EnumAppletCode;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicParm;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;

import java.util.List;

public class PackFunctionOnline {
    CrmScenarioUtilOnline crm = CrmScenarioUtilOnline.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParmOnline pp=new PublicParmOnline();
    FileUtil file=new FileUtil();
    //pc新建活动方法，返回文章id和文章id
    public Long[] createAArcile_id(String valid_start, String simulation_num)throws Exception{
        Long article_id;
        Long [] aid=new Long[2];
            crm.login(pp.zongjingli,pp.adminpassword);
            String[] customer_types = {"PRE_SALES", "AFTER_SALES"};
            int[] customer_level = {};
            String[] customer_property = {};
            String positions = pp.positions; //投放位置车型推荐 单选
            String valid_end = dt.getHistoryDate(4);
            int[] car_types = {1};
            String article_title = "app任务报名品牌上新，优惠多多，限时4天---" + dt.getHistoryDate(0);
            String article_bg_pic = file.texFile(pp.filePath);  //base 64
            String article_content = "品牌上新，优惠多多，限时4天,活动内容";
            String article_remarks = "品牌上新，优惠多多，限时4天,备注";

            boolean is_online_activity;  //是否线上报名活动
            is_online_activity = true;
//            String reception_name = manage(13)[0];  //接待人员名
//            String reception_phone = manage(13)[1]; //接待人员电话
            String reception_name = "xx";  //接待人员名
            String reception_phone = "15037286013"; //接待人员电话
            String customer_max = "50";                    //人数上限

            String activity_start = dt.getHistoryDate(1);
            String activity_end = dt.getHistoryDate(4);
            Integer role_id = 13;
            Boolean is_create_poster = true;//是否生成海报
            int task_customer_num=5;
            //新建文章并返回文章/活动id
            article_id = crm.createArticle(positions, valid_start, valid_end, customer_types, car_types, customer_level, customer_property, article_title,false, article_bg_pic, article_content, article_remarks, is_online_activity, reception_name, reception_phone, customer_max, simulation_num, activity_start, activity_end, role_id, Integer.toString(task_customer_num), is_create_poster).getLong("id");
            Long activity_id=crm.appartilceDetail(article_id,positions).getLong("activity_id");
            aid[0]=article_id;  //文章id
            aid[1]=activity_id;  //活动id
        return aid;
    }

    //创建文章返回文章id
    public Long createArcile(String positionsA,String article_title)throws Exception{
        String[] customer_types = {"PRE_SALES", "AFTER_SALES"};
        int[] customer_level = {};                                //客户等级
        String[] customer_property = {};
        String valid_start = dt.getHistoryDate(0);
        String valid_end = dt.getHistoryDate(4);
        int[] car_types = {};
        String article_bg_pic = file.texFile(pp.filePath);  //base 64
        String article_remarks = "品牌上新，优惠多多，限时4天,备注";
        //新建文章，获取id
        return crm.createArticleReal(positionsA,valid_start,valid_end,customer_types,car_types,customer_level,customer_property,article_title,false,article_bg_pic,pp.article_content,article_remarks,false).getLong("id");
    }

    //前台点击创建接待按钮创建顾客
    public JSONObject creatCust() throws Exception {
        //前台登陆
        crm.login(pp.qiantai, pp.qtpassword);
        String name="auto"+dt.getHHmm(0);
        StringBuilder phone = new StringBuilder("1");
        for (int i = 0; i < 10; i++) {
            String a = Integer.toString((int) (Math.random() * 10));
            phone.append(a);
        }
        JSONObject jsonP=new JSONObject();
        //获取当前空闲第一位销售id
        String sale_id = crm.freeSaleList().getJSONArray("list").getJSONObject(0).getString("sale_id");
        String userLoginName = "";
        JSONArray userlist = crm.userPage(1, 100).getJSONArray("list");
        for (int i = 0; i < userlist.size(); i++) {
            JSONObject obj = userlist.getJSONObject(i);
            if (obj.getString("user_id").equals(sale_id)) {
                userLoginName = obj.getString("user_login_name");
            }
        }
        //创建接待
        crm.creatReception("FIRST_VISIT");
        crm.login(userLoginName, pp.adminpassword);

        JSONObject data = crm.customerMyReceptionList("", "", "", 10, 1);

        Long receiptId=data.getJSONArray("list").getJSONObject(0).getLong("id");
        Long customerID=data.getJSONArray("list").getJSONObject(0).getLong("customer_id");
        //创建某级客户
        crm.customerEdit_onlyNec(customerID, 7, name, phone.toString(), "自动化---------创建----------H级客户");
        jsonP.put("name",name);
        jsonP.put("phone", phone.toString());
        jsonP.put("reception_id",receiptId);
        jsonP.put("customerId",customerID);
        jsonP.put("userLoginName",userLoginName);
        return jsonP;
    }

    //前台点击创建接待按钮接待老客
    public JSONObject creatCustOld(String phone) throws Exception {
        JSONObject jsonCO=new JSONObject();
        //前台登陆
        crm.login(pp.qiantai, pp.qtpassword);
        //搜索手机号
        JSONObject data=crm.phoneCheck(phone);
        Long customer_id=data.getLong("customer_id");
        String belongs_sale_name=data.getString("belongs_sale_name");
        String userLoginName = "";
        JSONArray userlist = crm.userPage(1, 100).getJSONArray("list");
        for (int i = 0; i < userlist.size(); i++) {
            JSONObject obj = userlist.getJSONObject(i);
            if (obj.getString("user_name").equals(belongs_sale_name)) {
                userLoginName = obj.getString("user_login_name");
            }
        }
        crm.receptionOld(customer_id,"AGAIN_VISIT");
        //销售登陆，获取当前接待id
        crm.login(userLoginName, pp.adminpassword);

        JSONObject dataC = crm.customerMyReceptionList("", "", "", 10, 1);

        Long id=dataC.getJSONArray("list").getJSONObject(0).getLong("id");
        Long customerId=dataC.getJSONArray("list").getJSONObject(0).getLong("customer_id");
        jsonCO.put("id",id);
        jsonCO.put("customerId",customerId);
        jsonCO.put("userLoginName",userLoginName);
        return jsonCO;
    }

    //新建试驾+审核封装 ok
    public void creatDriver(Long receptionId,Long customer_id,String name,String phone, int audit_status) throws Exception {  //1-通过，2-拒绝

        Long model = 1L;
        String country = "中国";
        String city = "图们";
        String email = dt.getHistoryDate(0)+"@qq.com";
        String address = "北京市昌平区";
        String ward_name = "小小";
        String driverLicensePhoto1Url = file.texFile(pp.filePath);
        String driverLicensePhoto2Url = file.texFile(pp.filePath);
        String electronicContractUrl = file.texFile(pp.filePath);
        String sign_date=dt.getHistoryDate(0);
        String sign_time=dt.getHHmm(0);

        String call="MEN";
        int driverid = crm.driveradd3(receptionId,customer_id,name,phone,2L,model,country,city,email,address,ward_name,driverLicensePhoto1Url,driverLicensePhoto2Url,electronicContractUrl,sign_date,sign_time,call).getInteger("id");
        //销售总监登陆
        crm.login(pp.xiaoshouZongjian,pp.adminpassword);
        crm.driverAudit(driverid,audit_status);
        //最后销售要再登陆一次

    }

    //订车+交车封装  copy lxq debug ok
    public void creatDeliver(Long reception_id,Long customer_id,String customer_name,String deliver_car_time, Boolean accept_show) throws Exception {
        //订车
        crm.orderCar(customer_id);
        //创建交车
        Long model = 3L;
        String path = file.texFile(pp.filePath);
        crm.deliverAdd(reception_id,customer_id,customer_name,deliver_car_time,model,path,accept_show,path);
    }
    //老客试驾完成接待---for评价
    public Long driverEva()throws Exception{
        crm.appletLoginToken(EnumAppletCode.XMF.getCode());
        JSONObject data = crm.appointmentTestDrive("MALE", pp.customer_name, pp.customer_phone_number,dt.getHistoryDate(0) , pp.car_type);
        //预约试驾成功后，页面显示数据
        Long appointment_id = data.getLong("appointment_id");
        //前台分配老客
        JSONObject json;
        json = creatCustOld(pp.customer_phone_number);
        Long id=json.getLong("id");
        Long customerId=json.getLong("customerId");
        String userLoginName=json.getString("userLoginName");
        //新建试驾,审核通过
        creatDriver(id,customerId,pp.customer_name,pp.customer_phone_number,1);
        crm.login(userLoginName,pp.adminpassword);            //销售登录完成接待
        crm.finishReception(customerId,7,pp.customer_name,pp.customer_phone_number,pp.remark);
        return appointment_id;
    }
    //获取(销售)顾问接待次数
    public Integer jiedaiTimes(int roleId,String guwen)throws Exception{
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
    public void createCar(String car_type_name) throws Exception{

        double lowest_price=88.99;
        double highest_price=888.99;
        String car_discount="跑车多数人知道，少数人了解";
        String car_introduce="保时捷Boxster是保时捷公司的一款双门双座敞篷跑车，引擎采中置后驱设计，最早以概念车形式亮相于北美车展展出。";
        String car_pic=file.texFile(pp.filePath);  //base 64
        String big_pic=file.texFile(pp.filePath);  //base 64
        String interior_pic=file.texFile(pp.filePath);  //base 64
        String space_pic=file.texFile(pp.filePath);  //base 64
        String appearance_pic=file.texFile(pp.filePath);  //base 64
        crm.addCarPc(car_type_name,lowest_price,highest_price,car_discount,car_introduce,car_pic,big_pic,interior_pic,space_pic,appearance_pic);
    }
    //创建车辆
    public Long createCarcode(String car_type_name) throws Exception{

        double lowest_price=88.99;
        double highest_price=8888.99;
        String car_discount="跑车多数人知道，少数人了解";
        String car_introduce="保时捷Boxster是保时捷公司的一款双门双座敞篷跑车，引擎采中置后驱设计，最早以概念车形式亮相于北美车展展出。";
        String car_pic=file.texFile(pp.filePath);  //base 64
        String big_pic=file.texFile(pp.filePath);  //base 64
        String interior_pic=file.texFile(pp.filePath);  //base 64
        String space_pic=file.texFile(pp.filePath);  //base 64
        String appearance_pic=file.texFile(pp.filePath);  //base 64
        Long code=crm.addCarPccode(car_type_name,lowest_price,highest_price,car_discount,car_introduce,car_pic,big_pic,interior_pic,space_pic,appearance_pic);
        return code;
    }
    //预约剩余时段次数查询
    public Long appointmentTimeList(String type,int i,String appointment_date) throws Exception {
        return crm.timeList(type, appointment_date).getJSONArray("list").getJSONObject(i).getLong("id");
    }
    public JSONObject appointmentTimeListO(String type,int i,String appointment_date) throws Exception {
        JSONObject data=crm.timeList(type, appointment_date).getJSONArray("list").getJSONObject(i);
        Long id=data.getLong("id");
        String time=data.getString("start_time");
        JSONObject object=new JSONObject();
        object.put("time_id",id);
        object.put("start_time",time);
        return object;
    }
   //删除文本中手机号用户
    public void deleteUser(String filePath){
        List<String> list=file.getFileContent(filePath);
        for(String value : list){
            JSONArray anwer=crm.customerListPC(value,1,10).getJSONArray("list");
            Long customer_id=anwer.getJSONObject(0).getLong("customer_id");
            if(anwer.size()!=0){
                crm.customerDeletePC(customer_id);
            }
        }
    }

    //查询活动列表，已下架状态，显示中状态，总列数
    public int [] statusNum() throws Exception {
        JSONObject data = crm.articlePage(1, 10, pp.positions);
        int[] aa = {0,0,0,0};
        aa[2]= data.getInteger("total");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            String status = list.getJSONObject(i).getString("status_name");
            if (status.equals("显示中")) {
                aa[0]++;
            } else if (status.equals("已下架")) {
                aa[1]++;
            }else if(status.equals("排期中")){
                aa[3]++;
            }
        }
        return aa;
    }
}
