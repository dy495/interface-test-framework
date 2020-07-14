package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.codec.binary.Base64;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.FileUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * @author : yu
 * @date :  2020/05/30
 */

public class CrmApp2_0_DataConsistency extends TestCaseCommon implements TestCaseStd {

    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    String sale_id = "uid_562be6aa"; //销售顾问-自动化 id

    String sh_name1 = "";
    String sh_pwd1 = "";


    FileUtil fileUtil = new FileUtil();
    String jpgPath = "src/main/java/com/haisheng/framework/testng/bigScreen/dailyImages/2019-10-22_1.jpg";
    String picurl = fileUtil.getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/dailyImages/2019-10-22_1.jpg");

    String phone = "一个假的手机号" + dt.getHistoryDate(0);


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "lxq";


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "CRM 日常");

        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getProscheShop();
        beforeClassInit(commonConfig);

        logger.debug("crm: " + crm);
        crm.login(sh_name1, sh_pwd1);

    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    /**
     * ====================销售顾问======================
     */

    @Test
    public void addScheduledesc10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("PC端添加工作安排，工作描述字数=10");

        }


    }

    /**
     * ====================售后======================
     */

    @Test
    public void custChkallEQList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //列表总数
            int listtotal = crm.afterSale_custList("", "", "", 1, 1).getInteger("total");
            //统计数据-全部车辆数
            int allcar = crm.afterSale_custTotal().getInteger("all_reception_total");
            Preconditions.checkArgument(allcar == listtotal, "全部车辆{}!=列表数{}", listtotal, allcar);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后客户管理：全部车辆==列表数");

        }

    }

    @Test
    public void custChkTotalGTNew() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject list = crm.afterSale_custTotal();
            //统计数据-今日接待售后数量
            int todayTotal = list.getInteger("today_reception_total");
            //统计数据-今日新增售后数量
            int todayNew = list.getInteger("today_new_after_sale_customer");

            Preconditions.checkArgument(todayTotal >= todayNew, "今日接待售后数量{}<今日新增售后数量{}", todayTotal, todayNew);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后客户管理：今日接待售后车辆>=今日新增售后车辆");

        }
    }

    @Test
    public void custChkAllGTTodatTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject list = crm.afterSale_custTotal();
            //统计数据-今日接待售后数量
            int todayTotal = list.getInteger("today_reception_total");
            //统计数据-全部车辆
            int all = list.getInteger("all_reception_total");

            Preconditions.checkArgument(all >= todayTotal, "全部车辆{}<今日接待售后数量{}", all, todayTotal);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后客户管理：全部车辆>=今日接待售后车辆");

        }
    }

    @Test
    public void afterSaleAddremarks21() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //小程序预约，销售查看客户信息
            Long after_record_id = repair();
            List <String> remarks = new ArrayList<String>();
            for (int i = 1;i < 22;i++){
                remarks.add("备注备注备注"+i);
            }
            String customer_name = "customer_name";
            String customer_phone_number = "15037286013";
            String plate_number = "吉H12345";
            int travel_mileage = 1;
            int car_type = 1;
            int maintain_type = 1;
            boolean service_complete = false;
            int customer_source = 0;
            //添加备注
            crm.afterSale_custList(after_record_id,customer_name,customer_phone_number,"",plate_number,travel_mileage,
                    car_type,maintain_type,-1,service_complete,customer_source,remarks);
            //查看客户信息的备注
            JSONArray remarkList = crm.afterSale_custDetail(after_record_id).getJSONArray("remarks");
            int size = remarkList.size();//备注条数
            Preconditions.checkArgument(size==20,"备注有{}条"+size);
            for (int i = 0 ; i < remarkList.size();i++){
                JSONObject obj = remarkList.getJSONObject(i);
                String search_remark = obj.getString("remark");
                Preconditions.checkArgument(!search_remark.equals("备注备注备注1"),"包含第一条备注");
            }

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后客户管理：添加21条备注，展示最新20条");

        }
    }

    @Test
    public void afterSaleAddVisitRecord21() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //小程序预约
            repair();
            //销售查看回访记录获取id
            Long recordid = crm.afterSale_VisitRecordList(1,1,"","","").getJSONArray("list").getJSONObject(0).getLong("id");
            //添加回访
            String return_visit_pic = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/分辨率较低.png");
            String next_return_visit_time = dt.getHistoryDate(1);
            for (int i = 1; i < 22;i++){
                String comment = "回访描述回访描述回访描述" + i;
                crm.afterSale_addVisitRecord(recordid,return_visit_pic,comment,next_return_visit_time);
            }

            //查看顾客信息
            JSONArray visit_list =  crm.afterSale_custDetail(recordid).getJSONArray("visit");
            int size = visit_list.size();
            Preconditions.checkArgument(size==20,"回访记录条数="+size);
            //查看回访内容
            for (int i = 0; i < size;i++){
                JSONObject obj = visit_list.getJSONObject(i);
                String comment = obj.getString("comment");
                Preconditions.checkArgument(!comment.equals("回访描述回访描述回访描述1"),"包含第一条回访记录");
            }


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后客户管理：添加21条回访，展示最新20条");

        }
    }



    //---------------------------

    // 小程序预约维修，售后顾问点击接待按钮
    public Long repair() throws Exception{

        //小程序登陆
        crm.appletlogin("");
        //预约使用参数
        String customer_name = "customer_name";
        String customer_phone_number = "15037286013";
        String appointment_date = dt.getHistoryDate(1);  //预约日期取当前天的前一天
        int car_type = 1;
        String car_type_name = "";
        JSONObject carData=crm.myCarList();
        JSONArray list=carData.getJSONArray("list");
        if(list==null){
            throw new Exception("暂无车辆");
        }
        String my_car_id=list.getJSONObject(0).getString("my_car_id");
        String appointment_time="09:30";
        String description="故障说明";
        Long repair_id =crm.appointmentRepair(Long.parseLong(my_car_id),customer_name,customer_phone_number,appointment_date,appointment_time,description).getLong("appointment_id");

        //维修顾问登陆，点击接待按钮
        crm.login("","");
        Long after_record_id = crm.reception_customer(repair_id).getLong("after_record_id");
        return after_record_id;
    }


    //预约保养
    public Long maintain() throws Exception{

        //小程序登陆
        crm.appletlogin("");
        //预约使用参数
        String customer_name = "customer_name";
        String customer_phone_number = "15037286013";
        String appointment_date = dt.getHistoryDate(1);  //预约日期取当前天的前一天
        int car_type = 1;
        String car_type_name = "";
        JSONObject carData=crm.myCarList();
        JSONArray list=carData.getJSONArray("list");
        if(list==null){
            throw new Exception("暂无车辆");
        }
        String my_car_id=list.getJSONObject(0).getString("my_car_id");
        String appointment_time="09:00";
        Long maintain_id=crm.appointmentMaintain(Long.parseLong(my_car_id),customer_name,customer_phone_number,appointment_date,appointment_time).getLong("appointment_id");

        //维修顾问登陆,点击接待按钮
        crm.login("","");
        Long after_record_id = crm.reception_customer(maintain_id).getLong("after_record_id");
        return after_record_id;

    }

    //获取图片base64
    public static String getImgStr(String imgFile) { //图片转base64
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(data));
    }


}
