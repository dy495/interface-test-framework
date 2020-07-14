package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

public class CrmPcTwoSystemCase extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    public String adminname="";    //pc登录密码，最好销售总监或总经理权限
    public String adminpassword="";
    public String code="";

    public Integer car_type = 1;
    public String car_type_name = "";
    public Long activity_id =0L;

    //读取文件内容
    public String texFile(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String str = in.readLine();
        return str;
    }
    //获取接待人员名称电话 --for createArticle
    public String [] manage(Integer role_id) throws Exception{
        JSONArray list=crm.manageList(role_id).getJSONArray("list");
        String [] name=new String[2];
        for(int i=0;i<list.size();i++){
            int status=list.getJSONObject(i).getInteger("status");
            if(status==1){
                name[0]=list.getJSONObject(i).getString("name");
                name[1]=list.getJSONObject(i).getString("phone");
                return name;
            }else {
                continue;}
        }
        return name;
    }

    //pc新建活动方法，返回活动id
    public Long createActivity(Integer simulation_num){
        Long article_id=0L;
        try {
            String[] customer_types = {"PRE_SALES", "AFTER_SALES"};
            int[] customer_level = {1};           //TODO:客户等级
            String[] customer_property = {"LOST", "MAINTENANCE", "LOYAL"};
            String[] positions = {"MODEL_RECOMMENDATION"}; //投放位置车型推荐 单选
            // String [] positions={"MODEL_RECOMMENDATION","PURCHASE_GUIDE","BRAND_CULTURE","CAR_ACTIVITY"};
            String valid_start = dt.getHistoryDate(0);
            String valid_end = dt.getHistoryDate(4);
            int[] car_types = {car_type};
            String article_title = "品牌上新，优惠多多，限时4天---" + dt.getHistoryDate(0);
            String article_bg_pic = texFile("src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic");  //base 64
            String article_content = "品牌上新，优惠多多，限时4天,活动内容";
            String article_remarks = "品牌上新，优惠多多，限时4天,备注";

            boolean is_online_activity = false;  //是否线上报名活动
            String reception_name = manage(13)[0];  //接待人员名
            String reception_phone = manage(13)[1]; //接待人员电话
            Integer customer_max = 20;                    //人数上限

            String activity_start = dt.getHistoryDate(0);
            String activity_end = dt.getHistoryDate(4);
            Integer role_id = 13;
            Boolean is_create_poster = true;//是否生成海报
            Integer task_customer_num = crm.groupTotal(customer_types, car_types, customer_level, customer_property).getInteger("total");
            //新建文章并返回文章/活动id
            article_id = crm.createArticle(positions, valid_start, valid_end, customer_types, car_types, customer_level, customer_property, article_title, article_bg_pic, article_content, article_remarks, is_online_activity, reception_name, reception_phone, customer_max, simulation_num, activity_start, activity_end, role_id, task_customer_num, is_create_poster).getLong("id");
            //activity_id = article_id;
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("创建活动");
        }
        return article_id;
    }

    //创建车辆
    public void createCar(String car_type_name) throws Exception{

        double lowest_price=899999.99;
        double highest_price=899999.99;
        String car_discount="限时优惠";
        String car_introduce="车型介绍，超大空间";
        String car_pic=texFile("src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic");  //base 64
        String big_pic=texFile("src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic");  //base 64
        String interior_pic=texFile("src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic");  //base 64
        String space_pic=texFile("src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic");  //base 64
        crm.addCarPc(car_type_name,lowest_price,highest_price,car_discount,car_introduce,car_pic,big_pic,interior_pic,space_pic);

    }
   /**
    * @description :pc新建活动，数据一致，pc活动列表+1，新建活动时投放人数等于该活动发送短信页总人数
    * @date :2020/7/14 10:23
    **/
    @Test(priority = 1)
    public void articleManage(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //pc登录创建活动，因获取发送短信人数参数要求一致，故不调用创建方法
            Long totalB=crm.articlePage(1,10).getLong("total");
            String []customer_types={"PRE_SALES","AFTER_SALES"};
            int []customer_level={2}; //TODO:客户等级
            String []customer_property={"LOST","MAINTENANCE","LOYAL"};
            String [] positions={"PURCHASE_GUIDE"}; //投放位置购买指南单选
            // String [] positions={"MODEL_RECOMMENDATION","PURCHASE_GUIDE","BRAND_CULTURE","CAR_ACTIVITY"};
            String valid_start=dt.getHistoryDate(0);
            String valid_end=dt.getHistoryDate(4);
            int []car_types={car_type};
            String article_title="购买指南，品牌上新，优惠多多，限时4天---"+dt.getHistoryDate(0);
            String article_bg_pic=texFile("src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic");  //base 64
            String article_content="购买指南，品牌上新，优惠多多，限时4天,活动内容";
            String article_remarks="购买指南，品牌上新，优惠多多，限时4天,备注";

            boolean is_online_activity=false;  //是否线上报名活动
            String reception_name=manage(13)[0];  //接待人员名
            String reception_phone=manage(13)[1]; //接待人员电话
            Integer customer_max=20;                    //人数上限
            Integer simulation_num=10;                   //假定基数
            String activity_start=dt.getHistoryDate(0);
            String activity_end=dt.getHistoryDate(4);
            Integer role_id=13;
            Boolean is_create_poster=true;//是否生成海报
            Integer task_customer_num=crm.groupTotal(customer_types,car_types,customer_level,customer_property).getInteger("total");
            //新建活动并返回活动id
            Long article_id=crm.createArticle(positions,valid_start,valid_end,customer_types,car_types,customer_level,customer_property,article_title,article_bg_pic,article_content,article_remarks,is_online_activity,reception_name,reception_phone,customer_max,simulation_num,activity_start,activity_end,role_id,task_customer_num,is_create_poster).getLong("id");
            activity_id = article_id;  //TODO:活动id

            crm.artilceDetail(article_id);  //pc活动详情 TODO：详情页信息校验
            //新增活动，活动列表+1
            Long totalA=crm.articlePage(1,10).getLong("total");
            Preconditions.checkArgument((totalA-totalB)==1,"新建活动，活动详情列表总数没+1");
            Integer activityTotal=crm.activityPeople(article_id).getInteger("total");
            Preconditions.checkArgument(activityTotal==task_customer_num,"新建活动时投放人数不等于，该活动发送短信页总人数");
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("pc新建活动，数据一致，活动列表+1，新建活动时投放人数等于该活动发送短信页总人数");
        }
    }

    /**
     * @description :pc新建活动，pc & app活动页数据一致 applet报名人数=假定基数+报名人数（0：不预约）
     * @date :2020/7/13 20:35
     **/
     @Test
     public void appletctivityPage(){
         logger.logCaseStart(caseResult.getCaseName());
         try{
             Integer simulation_num=30; //假定基数
             Long article_id=createActivity(simulation_num);  //创建活动方法
             crm.appletlogin(code);
             Integer registered_numA=crm.articleDetial(article_id).getInteger("registered_num");  //文章详情
             //pc 创建活动 不预约，创建活动假定基数==applet报名人数
             Preconditions.checkArgument(registered_numA==simulation_num,"创建后，不预约创建活动假定基数！=applet报名人数");

         }catch (AssertionError e){
             appendFailreason(e.toString());
         }catch (Exception e){
             appendFailreason(e.toString());
         }finally {
             saveData("pc新建活动，applet报名人数=假定基数+报名人数");
         }
     }

     /**
      * @description :pc新建文章，applet文章列表+1 //本期首页坑位只展示当前最新文章，不展示历史文章列表；下期可能会改动
      * @date :2020/7/14 11:46
      **/
//     @Test
     public void createArctile(){
         logger.logCaseStart(caseResult.getCaseName());
         try{
             //小程序登录 记录小程序首页文章列表中总数
             crm.appletlogin(code);
             JSONArray list=crm.articleList().getJSONArray("list");
             int total=0;
             if(list==null||list.size()==0){
                 total=0;
             }else {
                 total=list.size();
             }
             crm.login(adminname,adminpassword);
             //创建文章


         }catch (AssertionError e){
             appendFailreason(e.toString());
         }catch (Exception e){
             appendFailreason(e.toString());
         }finally {
             saveData("pc新建文章，applet文章列表+1");
         }
     }

    /**
     * @description :发送推广短信
     * @date :2020/7/13 19:37
     **/
//     @Test
     public void sendMessage(){
         logger.logCaseStart(caseResult.getCaseName());
         try{


         }catch(AssertionError e){
             appendFailreason(e.toString());
         }catch (Exception e){
             appendFailreason(e.toString());
         }finally {
             saveData("发送推广短信");
         }
     }

     /**
      * @description :新建文章，小程序显示文章内容校验
      * @date :2020/7/14 20:16
      **/
     @Test(dataProvider = "POSITIONS",dataProviderClass = CrmScenarioUtil.class)
     public void articleTitleCompare(String positionsA){
         logger.logCaseStart(caseResult.getCaseName());
         try{
             crm.login(adminname,adminpassword);
             String[] customer_types = {"PRE_SALES", "AFTER_SALES"};
             int[] customer_level = {1};                                //TODO:客户等级
             String[] customer_property = {"LOST", "MAINTENANCE", "LOYAL"};
             String[] positions = {positionsA};         //投放位置车型推荐 单选
             // String [] positions={"MODEL_RECOMMENDATION","PURCHASE_GUIDE","BRAND_CULTURE","CAR_ACTIVITY"};
             String valid_start = dt.getHistoryDate(0);
             String valid_end = dt.getHistoryDate(4);
             int[] car_types = {car_type};
             String article_title = "品牌上新，优惠多多，限时4天---" + dt.getHistoryDate(0);
             String article_bg_pic = texFile("src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic");  //base 64
             String article_content = "品牌上新，优惠多多，限时4天,活动内容";
             String article_remarks = "品牌上新，优惠多多，限时4天,备注";
             //新建文章，获取id
             Long actriclereal_id=crm.createArticleReal(positions,valid_start,valid_end,customer_types,car_types,customer_level,customer_property,article_title,article_bg_pic,article_content,article_remarks,false).getLong("id");
            //小程序查看文章内容
             crm.appletlogin(code);
             JSONObject detail=crm.articleDetial(actriclereal_id);
             String article_titlA=detail.getString("article_titl");
             String article_contentA=detail.getString("article_content");
             String article_remarksA=detail.getString("article_remarks");

             Preconditions.checkArgument(article_titlA.equals(article_title),"小程序文章标题错误");
             Preconditions.checkArgument(article_contentA.equals(article_content),"小程序文章内容错误");
             Preconditions.checkArgument(article_remarksA.equals(article_remarks),"小程序文章备注错误");



         }catch (AssertionError e){
             appendFailreason(e.toString());
         }catch (Exception e){
             appendFailreason(e.toString());
         }finally {
             saveData("");
         }
     }

     /**
      * @description :pc 内容管理看车；pc创建车辆后，pc车辆列表数未+1
      * @date :2020/7/14 11:35
      **/
     @Test
     public void goodsManage(){
         logger.logCaseStart(caseResult.getCaseName());
         try{
             JSONArray list=crm.carList().getJSONArray("list");
             int total=0;
             if(list==null||list.size()==0){
                 total=0;
             }else{ total=list.size(); }
             //pc 新建车辆
             String car_type_name="bsj自动化车型"+dt.currentDateToTimestamp();
             createCar(car_type_name);
             JSONArray listA=crm.carList().getJSONArray("list");
             int totalA=0;
             if(list==null||list.size()==0){
                 totalA=0;
             }else{ total=list.size(); }
             Preconditions.checkArgument((totalA-total)==1,"pc创建车辆后，pc车辆列表数未+1");

         }catch (AssertionError e){
             appendFailreason(e.toString());
         }catch (Exception e){
             appendFailreason(e.toString());
         }finally {
             saveData("pc创建车辆后，pc车辆列表数+1");
         }
     }


     /**
      * @description :看车。pc新建车型，applet看车页车辆列表+1&信息校验
      * @date :2020/7/14 18:34
      **/
     @Test
     public void watchCarConsistency(){
         logger.logCaseStart(caseResult.getCaseName());
         try{
             crm.appletlogin(code);
             JSONArray list=crm.appletwatchCarList().getJSONArray("list");
             int total=0;
             if(list==null||list.size()==0){
                 total=0;
             }else{
                 total=list.size();
             }
             //pc创建车辆
             crm.login(adminname,adminpassword);
             String car_type_name="bsj自动化车型"+dt.currentDateToTimestamp();
             createCar(car_type_name);
             crm.appletlogin(code);
             JSONArray listA=crm.appletwatchCarList().getJSONArray("list");
             int totalA=0;
             if(list==null||list.size()==0){
                 totalA=0;
             }else{
                 totalA=list.size();
             }
             String car_type_nameA=listA.getJSONObject(0).getString("car_type_name");//此处认为新建排在第一位
             String price=listA.getJSONObject(0).getString("price");//此处认为新建排在第一位
             Preconditions.checkArgument((totalA-total)==1,"pc新建车型，applet看车页车辆列表没+1");
             Preconditions.checkArgument(car_type_name.equals(car_type_nameA),"pc新建车辆，applet未显示");
             //TODO:价格校验
             Preconditions.checkArgument(price.equals(""),"pc新建车辆，applet售价显示异常");


         }catch (AssertionError e){
             appendFailreason(e.toString());
         }catch (Exception e){
             appendFailreason(e.toString());
         }finally {
             saveData("看车。pc新建车型，applet看车页车辆列表+1&信息校验");
         }
     }







    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     *
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "xmf";


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
        crm.login(adminname, adminpassword);


    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     *
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }



}
