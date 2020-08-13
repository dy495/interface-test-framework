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
import org.testng.annotations.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description :crm2.0 pc case--xia
 * @date :2020/7/15 20:22
 **/


public class CrmPcTwoSystemCase extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    public String adminname="xx";    //pc登录密码，最好销售总监或总经理权限
    public String adminpassword="e10adc3949ba59abbe56e057f20f883e";
    public String baoshijie="baoshijie";
    public String code="1234567";
    public String lxqgw="lxqgw";     //销售顾问
    public String adminnamexiaoshou = "销售总监";    //pc登录密码，最好销售总监或总经理权限
    public String adminpasswordxiaoshou = "e10adc3949ba59abbe56e057f20f883e";

    public Integer car_type = 1;
    public String car_type_name = "Panamera";
    public Long activity_id =43L;
    public String filePath="src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic";

    public String article_content = "品牌上新，优惠多多，限时4天,文章内容";  //新建文章，文章内容
    public String positionList1="CAR_ACTIVITY";


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
    public Long createActivity(String valid_start, String simulation_num){
        Long article_id=0L;
        try {
            crm.login(adminname,adminpassword);
            String[] customer_types = {"PRE_SALES", "AFTER_SALES"};
            int[] customer_level = {};           //TODO:客户等级
            String[] customer_property = {};
            String positions = "CAR_ACTIVITY"; //投放位置车型推荐 单选
            // String [] positions={"MODEL_RECOMMENDATION","PURCHASE_GUIDE","BRAND_CULTURE","CAR_ACTIVITY"};
//            String valid_start = dt.getHistoryDate(0);
            String valid_end = dt.getHistoryDate(4);
            int[] car_types = {car_type};
            String article_title = "品牌上新，优惠多多，限时4天---" + dt.getHistoryDate(0);
            String article_bg_pic = texFile(filePath);  //base 64
            String article_content = "品牌上新，优惠多多，限时4天,活动内容";
            String article_remarks = "品牌上新，优惠多多，限时4天,备注";

            boolean is_online_activity = true;  //是否线上报名活动
//            String reception_name = manage(13)[0];  //接待人员名
//            String reception_phone = manage(13)[1]; //接待人员电话
            String reception_name = "xx";  //接待人员名
            String reception_phone = "15037286013"; //接待人员电话
            String customer_max = "50";

            String activity_start = dt.getHistoryDate(0);
            String activity_end = dt.getHistoryDate(4);
            Integer role_id = 15;
            Boolean is_create_poster = true;//是否生成海报
            Integer task_customer_numa = crm.groupTotal(customer_types, car_types, customer_level, customer_property).getInteger("total");
            Integer task_customer_num=5;
            //新建文章并返回文章/活动id
            article_id = crm.createArticle(positions, valid_start, valid_end, customer_types, car_types, customer_level, customer_property, article_title, false,article_bg_pic, article_content, article_remarks, is_online_activity, reception_name, reception_phone, customer_max, simulation_num, activity_start, activity_end, role_id, Integer.toString(task_customer_num), is_create_poster).getLong("id");
            activity_id=crm.appartilceDetail(article_id,positions).getLong("activity_id");
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            logger.info("create activity ,activity_id:{}",activity_id);
        }
        return activity_id;
    }
    //pc新建活动方法，返回文章id和文章id
    public Long[] createAArcile_id(String valid_start, String simulation_num){
        Long article_id=0L;
        Long [] aid=new Long[2];
        try {
            crm.login(adminname,adminpassword);
            String[] customer_types = {"PRE_SALES", "AFTER_SALES"};
            int[] customer_level = {};           //TODO:客户等级
            String[] customer_property = {};
            String positions = "CAR_ACTIVITY"; //投放位置看车推荐 单选
            // String [] positions={"MODEL_RECOMMENDATION","PURCHASE_GUIDE","BRAND_CULTURE","CAR_ACTIVITY"};
//            String valid_start = dt.getHistoryDate(0);
            String valid_end = dt.getHistoryDate(4);
            int[] car_types = {car_type};
            String article_title = "品牌优惠，限时4天-" + dt.getHistoryDate(0);
            String article_bg_pic = texFile(filePath);  //base 64
            String article_content = "品牌上新，优惠多多，限时4天,活动内容";
            String article_remarks = "品牌上新，优惠多多，限时4天,备注";

            boolean is_online_activity = true;  //是否线上报名活动
//            String reception_name = manage(13)[0];  //接待人员名
//            String reception_phone = manage(13)[1]; //接待人员电话
            String reception_name = "xx";  //接待人员名
            String reception_phone = "15037286013"; //接待人员电话
            String customer_max = "50";                    //人数上限

            String activity_start = dt.getHistoryDate(1);
            String activity_end = dt.getHistoryDate(4);
            Integer role_id = 16;
            Boolean is_create_poster = true;//是否生成海报
            Integer task_customer_numa = crm.groupTotal(customer_types, car_types, customer_level, customer_property).getInteger("total");
            Integer task_customer_num=5;
            //新建文章并返回文章/活动id
            article_id = crm.createArticle(positions, valid_start, valid_end, customer_types, car_types, customer_level, customer_property, article_title,false, article_bg_pic, article_content, article_remarks, is_online_activity, reception_name, reception_phone, customer_max, simulation_num, activity_start, activity_end, role_id, Integer.toString(task_customer_num), is_create_poster).getLong("id");
            activity_id=crm.appartilceDetail(article_id,positions).getLong("activity_id");
            aid[0]=article_id;  //文章id
            aid[1]=activity_id;  //活动id
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            logger.info("create activity ,activity_id:{}",activity_id);
        }
        return aid;
    }
    //创建文章返回文章id
    public Long createArcile(String positionsA,String article_title)throws Exception{
        String[] customer_types = {"PRE_SALES", "AFTER_SALES"};
        int[] customer_level = {};                                //TODO:客户等级
        String[] customer_property = {};
        String positions = positionsA;         //投放位置车型推荐 单选
        // String [] positions={"MODEL_RECOMMENDATION","PURCHASE_GUIDE","BRAND_CULTURE","CAR_ACTIVITY"};
        String valid_start = dt.getHistoryDate(0);
        String valid_end = dt.getHistoryDate(4);
        int[] car_types = {};
        //String article_title = "品牌上新，优惠多多，限时4天---" + dt.getHistoryDate(0);
        String article_bg_pic = texFile(filePath);  //base 64
        String article_remarks = "品牌上新，优惠多多，限时4天,备注";
        //新建文章，获取id
        Long actriclereal_id=crm.createArticleReal(positions,valid_start,valid_end,customer_types,car_types,customer_level,customer_property,article_title,false,article_bg_pic,article_content,article_remarks,false).getLong("id");
        return actriclereal_id;
    }

    //创建车辆
    public void createCar(String car_type_name) throws Exception{

        double lowest_price=88.99;
        double highest_price=888.99;
        String car_discount="跑车多数人知道，少数人了解";
        String car_introduce="保时捷Boxster是保时捷公司的一款双门双座敞篷跑车，引擎采中置后驱设计，最早以概念车形式亮相于北美车展展出。";
        String car_pic=texFile(filePath);  //base 64
        String big_pic=texFile(filePath);  //base 64
        String interior_pic=texFile(filePath);  //base 64
        String space_pic=texFile(filePath);  //base 64
        String appearance_pic=texFile(filePath);  //base 64
        crm.addCarPc(car_type_name,lowest_price,highest_price,car_discount,car_introduce,car_pic,big_pic,interior_pic,space_pic,appearance_pic);
    }

    //创建车辆
    public Long createCarcode(String car_type_name) throws Exception{

        double lowest_price=88.99;
        double highest_price=8888.99;
        String car_discount="跑车多数人知道，少数人了解";
        String car_introduce="保时捷Boxster是保时捷公司的一款双门双座敞篷跑车，引擎采中置后驱设计，最早以概念车形式亮相于北美车展展出。";
        String car_pic=texFile(filePath);  //base 64
        String big_pic=texFile(filePath);  //base 64
        String interior_pic=texFile(filePath);  //base 64
        String space_pic=texFile(filePath);  //base 64
        String appearance_pic=texFile(filePath);  //base 64
        Long code=crm.addCarPccode(car_type_name,lowest_price,highest_price,car_discount,car_introduce,car_pic,big_pic,interior_pic,space_pic,appearance_pic);
        return code;
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
    /**
     * @description :pc新建文章，数据一致，pc文章列表+1，列表信息校验，新建文章时投放人数等于该文章发送短信页总人数；查看文章详情信息校验
     * @date :2020/7/14 10:23
     **/
    @Test(priority = 1)
    public void articleManage(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //坑位列表
            JSONArray positionList=crm.positionList().getJSONArray("list");
            Long totalB=crm.articlePage(1,10,positionList1).getLong("total");
            String []customer_types={"PRE_SALES","AFTER_SALES"};
            int []customer_level={};                          //客户等级
            String []customer_property={};
            String positions="CAR_ACTIVITY"; //投放位置购买指南单选
            // String [] positions={"MODEL_RECOMMENDATION","PURCHASE_GUIDE","BRAND_CULTURE","CAR_ACTIVITY"};
            String valid_start=dt.getHistoryDate(0);
            String valid_end=dt.getHistoryDate(4);
            int []car_types={car_type};
            String article_title="购买指南，品牌上新，优惠多多，限时4天---"+dt.getHistoryDate(0);
            String article_bg_pic=texFile(filePath);  //base 64
            String article_content="购买指南，品牌上新，优惠多多，限时4天,活动内容";
            String article_remarks="购买指南，品牌上新，优惠多多，限时4天,备注";

            boolean is_online_activity=true;  //是否线上报名活动
//            String reception_name=manage(13)[0];  //接待人员名
//            String reception_phone=manage(13)[1]; //接待人员电话
            String reception_name="xia1";  //接待人员名
            String reception_phone="15037286013"; //接待人员电话
            String customer_max="20";                    //人数上限
            String simulation_num="10";                   //假定基数
            String activity_start=dt.getHistoryDate(0);
            String activity_end=dt.getHistoryDate(4);
            Integer role_id=15;
            Boolean is_create_poster=true;//是否生成海报
            Integer task_customer_numa=crm.groupTotal(customer_types,car_types,customer_level,customer_property).getInteger("total");
            Integer task_customer_num=5;
            //新建文章并返回文章id
            Long article_id=crm.createArticle(positions,valid_start,valid_end,customer_types,car_types,customer_level,customer_property,article_title,false,article_bg_pic,article_content,article_remarks,is_online_activity,reception_name,reception_phone,customer_max,simulation_num,activity_start,activity_end,role_id,Integer.toString(task_customer_num),is_create_poster).getLong("id");
            //activity_id = article_id;

            JSONObject pcarctile=crm.artilceDetailpc(article_id);  //pc活动详情
            String detail_customer_max=pcarctile.getString("customer_max");
            String detail_registered_num=pcarctile.getString("registered_num");  //已经报名人数
            String detail_article_title=pcarctile.getString("article_title");
            String detail_article_content=pcarctile.getString("article_content");


            //新增活动，活动列表+1
            JSONObject data=crm.articlePage(1,10,positionList1);
            Long totalA=data.getLong("total");
            JSONArray list=data.getJSONArray("list");
            String customer_typesA=list.getJSONObject(0).getString("customer_types");
            String positionsA=list.getJSONObject(0).getString("positions");
            String valid_startA=list.getJSONObject(0).getString("valid_start");
            String valid_endA=list.getJSONObject(0).getString("valid_end");
            String statusA=list.getJSONObject(0).getString("status");


            Preconditions.checkArgument((totalA-totalB)==1,"新建活动，活动详情列表总数没+1");
            Integer activityTotal=crm.activityPeople(article_id).getInteger("total");
            Preconditions.checkArgument(activityTotal==task_customer_numa,"新建活动时投放人数不等于，该活动发送短信页总人数");
            //活动列表信息校验
            Preconditions.checkArgument(customer_typesA.equals("售前、售后"),"新建活动列表信息，投放人群展示错误");
            Preconditions.checkArgument(positionsA.equals("首页-看车页"),"新建活动列表信息，投放位置展示错误");
            Preconditions.checkArgument(valid_endA.equals(valid_end),"新建活动列表信息，失效时间展示错误");
            Preconditions.checkArgument(valid_startA.equals(valid_start),"新建活动列表信息，生效时间展示错误");
            Preconditions.checkArgument(statusA.equals("SHOW"),"新建活动列表信息，状态展示错误");

            //查看详情信息校验
            Preconditions.checkArgument(detail_customer_max.equals(customer_max),"查看文章详情，报名总额错误");
            Preconditions.checkArgument(detail_article_title.equals(article_title),"查看文章详情，文章内容错误");
            Preconditions.checkArgument(detail_article_content.equals(article_content),"查看文章详情，文章标题错误");

            crm.articleStatusChange(article_id);
            crm.articleDelete(article_id);

            crm.articleDelete(article_id);
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("pc新建活动，数据一致，活动列表+1，新建活动时投放人数等于该活动发送短信页总人数,文章列表数据校验，查看文章详情数据校验");
        }
    }

    /**
     * @description :pc新建活动，报名客户和任务客户活动列表+1,删除活动-1
     * @date :2020/8/2 13:08
     **/
    @Test
    public void appointTaskList(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray list=crm.activityShowList().getJSONArray("list");
            int total;
            if(list==null||list.size()==0){
                total=0;
            }else{
                total=list.size();
            }
            //创建活动
            Long [] aid=createAArcile_id(dt.getHistoryDate(0),"10");
            Long activity_id=aid[1];
            Long id=aid[0];
            if(activity_id==null){
                throw new Exception("创建活动失败");
            }

            JSONArray listA=crm.activityShowList().getJSONArray("list");
            int totalA=listA.size();

            //删除活动
            crm.articleStatusChange(id);
            crm.articleDelete(id);
            JSONArray listB=crm.activityShowList().getJSONArray("list");
            int totalB=listB.size();
            Preconditions.checkArgument(totalA-total==1,"创建活动，报名管理-活动列表没+1");
            Preconditions.checkArgument(totalB==total,"删除活动，报名管理-活动列表没-1");

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("pc新建活动，报名客户和任务客户活动列表+1，删除-1");
        }
    }

    /**
     * @description :活动详情报名人数==报名管理-活动报名列表数（除已取消，已拒绝）
     * @date :2020/8/2 13:31
     **/
    @Test
    public void activityDetailAndList(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray list=crm.activityShowList().getJSONArray("list");
            if(list==null||list.size()==0){
                return;
            }
            for(int i=0;i<list.size();i++){
                Long avtivity_id=list.getJSONObject(i).getLong("id");
                String avtivity_name=list.getJSONObject(i).getString("name");
                //活动详情，已报名人数  TODO:无法由活动id获取文章id,故无法获取pc文章详情中已报名人数

                JSONObject dataActivity=crm.activityList(1,10,avtivity_id);
                long listActivityTotal=dataActivity.getLong("total");
                JSONArray listActivity=dataActivity.getJSONArray("list");
                for(int j=0;j<listActivity.size();j++){
                    String service_status=listActivity.getJSONObject(j).getString("service_status_name"); //客户状态，已取消、报名成功/失败
                    int audit_status=listActivity.getJSONObject(j).getInteger("audit_status");  //审核状态，2拒绝
                    if(service_status.equals("已取消")||(service_status.equals("报名失败")&&audit_status==2))
                        listActivityTotal=listActivityTotal-1;
                }
                logger.info("[{}]报名客户列表中，已报名客户数量：{}",avtivity_name,listActivityTotal);

            }
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("活动详情报名人数==报名管理-活动报名列表数（除已取消，已拒绝）");
        }
    }


    /**
     * @description :pc内容运营，状态，删除文章；展示中的活动不应该能被删除，下架的活动能被删除且活动列表-1    新建了活动 ok
     * @date :2020/7/15 16:18
     **/
    @Test(priority = 1)
    public void activityState(){
        logger.logCaseStart(caseResult.getCaseName());
        try{

            String num="6"; //假定基数
            String valid_start = dt.getHistoryDate(0);
            Long [] aid=createAArcile_id(valid_start,num);
            Long activity_id=aid[1];
            Long id=aid[0];   //文章id
            String total=crm.articlePage(1,10,positionList1).getString("total");
            //删除正在运行的活动失败
            Long code=crm.articleDelete(id).getLong("code");
            crm.articleStatusChange(id);       //改变活动状态
            //删除下架活动成功
            Long codeA=crm.articleDelete(id).getLong("code");

            //删除活动列表-1
            String totalA=crm.articlePage(1,10,positionList1).getString("total");
            Preconditions.checkArgument(code==1001,"展示中的活动不应该能被删除");
            Preconditions.checkArgument(codeA==1000,"下架的活动不应该不能被删除");
            logger.info("删除前 文章列表总数：{}",total);
            logger.info("删除后 文章列表总数：{}",totalA);
            Preconditions.checkArgument((Integer.parseInt(total)-Integer.parseInt(totalA)==1),"删除活动，活动列表未-1");
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("展示中的活动不应该能被删除，下架的活动能被删除且活动列表-1");
        }
    }

    /**
     * @description :pc内容运营，状态，删除文章排期中的活动 ok
     * @date :2020/7/15 17:13
     **/
    @Test(priority = 1)
    public void deleteRunningActivity(){
        logger.logCaseStart(caseResult.getCaseName());
        try{

            String num="6"; //假定基数
            String valid_start = dt.getHistoryDate(1);
            Long [] aid=createAArcile_id(valid_start,num);
            Long activity_id=aid[1];
            Long id=aid[0];
            if(activity_id==null||id==null){
                throw new Exception("创建文章获取id失败");
            }
            String total=crm.articlePage(1,10,positionList1).getString("total");
            //删除排期活动成功 列表-1
            Long code=crm.articleDelete(id).getLong("code");
            //删除活动列表-1
            String totalA=crm.articlePage(1,10,positionList1).getString("total");
            Preconditions.checkArgument(code==1000,"删除排期活动不成功");
            Preconditions.checkArgument((Integer.parseInt(total)-Integer.parseInt(totalA)==1),"删除排期活动，活动列表未-1");
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("pc删除排期中活动，活动列表-1");
        }
    }

    /**
     * @description :banner 新建文章，管理文章列表+1,删除文章，列表-1  ok
     * @date :2020/7/15 17:34
     **/
    @Test
    public void bannerArticleList(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray list=crm.articleShowList().getJSONArray("list");
            int total;
            if(list==null||list.size()==0){
                total=0;
            }else {
                total=list.size();
            }
            String positions="MODEL_RECOMMENDATION";
            String article_title = "品牌上新，优惠多多，限时4天---" + dt.getHistoryDate(0);
            Long id=createArcile(positions,article_title);
            int totalA=crm.articleShowList().getJSONArray("list").size();
            Preconditions.checkArgument((totalA-total)==1,"banner 新建文章，管理文章列表没+1");
            //删除文章，列表-1
            crm.articleStatusChange(id);       //改变活动状态
            crm.articleDelete(id);
            int totalB=crm.articleShowList().getJSONArray("list").size();
            Preconditions.checkArgument((totalA-totalB)==1,"banner 删除文章，管理文章列表没-1");
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("banner 新建文章，管理文章列表+1,删除文章，列表-1");
        }
    }

    /**
     * @description :banner 下拉菜单中数==内容运营文章数（除已下架、已过期）  //TODO:四个位置之和
     * @date :2020/8/2 15:49
     **/
    @Test
    public void bannerAndArticle(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray list=crm.articlePage(1,100,positionList1).getJSONArray("list");
            if(list==null||list.size()==0){
                return;
            }
            int total=list.size();

            for(int i=0;i<list.size();i++){
                String status_name=list.getJSONObject(i).getString("status_name");
                if((status_name.equals("已下架"))||(status_name.equals("已过期"))){
                    total = total - 1;
                }
            }
            //banner 下拉列表数
            int totalB=crm.articleShowList().getJSONArray("list").size();
            Preconditions.checkArgument(totalB==total,"下拉菜单中数!=内容运营文章数（除已下架、已过期）");

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("下拉菜单中数==内容运营文章数（除已下架、已过期）");
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
            crm.appletLogin(code);
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
     * @description :新建文章，小程序显示文章内容校验(车型推荐、购买指南、、、、)
     * @date :2020/7/14 20:16
     **/
    @Test(dataProvider = "POSITIONS",dataProviderClass = CrmScenarioUtil.class)
    public void articleTitleCompare(String positions){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String article_title = "品牌上新，优惠多多，限时4天---" + dt.getHistoryDate(0);
//            String article_content = "品牌上新，优惠多多，限时4天,文章内容";
            String article_remarks = "品牌上新，优惠多多，限时4天,备注";
            //新建文章，获取id
            Long actriclereal_id=createArcile(positions,article_title);
            //小程序查看文章内容
            crm.appletLogin(code);
            JSONObject detail=crm.appartilceDetail(actriclereal_id,positions);
            String article_titlA=detail.getString("article_title");
            String article_contentA=detail.getString("article_content");
            //String article_remarksA=detail.getString("article_remarks");  //需求变更文章，不填写备注
            crm.login(adminname,adminpassword);
            crm.articleStatusChange(actriclereal_id);
            crm.articleDelete(actriclereal_id);
            Preconditions.checkArgument(article_titlA.equals(article_title),"小程序文章标题错误");
            Preconditions.checkArgument(article_contentA.equals(article_content),"小程序文章内容错误");
            //Preconditions.checkArgument(article_remarksA.equals(article_remarks),"小程序文章备注错误");



        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("新建文章，小程序显示文章内容校验(车型推荐、购买指南、、、、)");
        }
    }

    /**
     * @description :pc 内容管理看车；pc创建车辆后，pc车辆列表数+1 ok
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
            if(total==50){
                throw new Exception("商品数量已达上限，无法添加");
            }
            //pc 新建车辆
            String car_type_name="911-"+dt.getHHmm(0);
            createCar(car_type_name);
            JSONArray listA=crm.carList().getJSONArray("list");
            int totalA=0;
            if(listA==null||listA.size()==0){
                totalA=0;
            }else{ totalA=listA.size(); }
            String car_id=listA.getJSONObject(listA.size()-1).getString("id");  //新建车型id
            crm.carDelete(Integer.parseInt(car_id));   //删除车辆
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
     * @description :商品管理。pc删除车型，车辆列表-1 ok
     * @date :2020/7/15 18:34
     **/
    @Test
    public void CardeleteConsistency(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //创建车
            String car_type_name="Macan"+dt.getHHmm(0);
            createCar(car_type_name);
            JSONArray list=crm.carList().getJSONArray("list");
            int total=list.size();
            String car_id=list.getJSONObject(list.size()-1).getString("id");  //新建车型id
            //删除车辆
            crm.carDelete(Integer.parseInt(car_id));
            JSONArray listA=crm.carList().getJSONArray("list");

            int totalA;
            if(listA==null||listA.size()==0){
                totalA=0;
            }else{
                totalA=listA.size();
            }
            Preconditions.checkArgument((total-totalA)==1,"pc删除车型，pc车辆列表没-1");

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("pc删除车型，车辆列表-1");
        }
    }
    /**
     * @description :删除商品管理车
     * @date :2020/7/21 17:42
     **/
//    @Test
    public void deletegoodsManage(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray list=crm.carList().getJSONArray("list");
            int total=0;
            if(list==null||list.size()==0){
                total=0;
            }else{ total=list.size(); }
            for(int i=0;i<45;i++){
                Integer id=list.getJSONObject(i).getInteger("id");
                crm.carDelete(id);
            }
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("pc批量删除车辆");
        }
    }
    /**
     * @description :创建商品车辆 50辆边界
     * @date :2020/7/21 17:43
     **/
    @Test
    public void createGoodsManage(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray list=crm.carList().getJSONArray("list");
            int total=0;
            if(list==null||list.size()==0){
                total=0;
            }else{ total=list.size(); }
            //pc 新建车辆
            int limit=50-total;
            for(int i=0;i<limit;i++){
                String car_type_name="Cayman2"+Integer.toString(i);
                createCar(car_type_name);
            }
            //创建第51辆车
            String car_type_name="Cayman51";
            Long code=createCarcode(car_type_name);
            Preconditions.checkArgument(code==1001,"商品车辆边界50，添加51辆应该失败");
            //删除创建的商品车辆
            if(limit==0){
                return;
            }else {
                JSONArray listA = crm.carList().getJSONArray("list");
                for (int j = total; j < limit+total; j++) {
                    String car_id = listA.getJSONObject(j).getString("id");  //新建车型id
                    crm.carDelete(Integer.parseInt(car_id));
                }
            }

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("pc创建商品车辆 50辆边界");
        }
    }


    /**
     * @description :看车。pc新建车型，applet看车页车辆列表+1&信息校验（车辆详情于pc配置的一致） ok
     * @date :2020/7/14 18:34
     **/
    @Test
    public void watchCarConsistency(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            crm.appletLogin(code);
            JSONArray list=crm.appletwatchCarList().getJSONArray("list");
            int total=0;
            if(list==null||list.size()==0){
                total=0;
            }else{
                total=list.size();
            }
            //pc创建车辆
            crm.login(adminname,adminpassword);
            String car_type_name="Cayman"+dt.getHHmm(0);
            createCar(car_type_name);
            //applet 看车列&详情
            crm.appletLogin(code);
            JSONArray listA=crm.appletwatchCarList().getJSONArray("list");
            int totalA=0;
            if(listA==null||listA.size()==0){
                totalA=0;
            }else{
                totalA=listA.size();
            }
            JSONObject dataA=listA.getJSONObject(listA.size()-1);  //新建车后排在第一位
            String car_type_nameA=dataA.getString("car_type_name");
            String price=dataA.getString("price");
            String car_id=dataA.getString("id");
            crm.login(adminname,adminpassword);
            crm.carDelete(Integer.parseInt(car_id));
            Preconditions.checkArgument((totalA-total)==1,"pc新建车型，applet看车页车辆列表没+1");
//             Preconditions.checkArgument(car_type_name.equals(car_type_nameA),"pc新建车辆，applet未显示");
//             Preconditions.checkArgument(price.equals("88.99-888.99万"),"pc新建车辆，applet售价显示异常");


        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("看车。pc新建车型，applet看车页车辆列表+1&信息校验");
        }
    }

    /**
     * @description :商品管理。pc新建车型，车辆列表+1；删除车辆-1,信息校验
     * @date :2020/7/14 18:34
     **/
    @Test
    public void CarlistConsistency(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray list=crm.carList().getJSONArray("list");
            int total=0;
            if(list==null||list.size()==0){
                total=0;
            }else{
                total=list.size();
            }
            //pc创建车辆
            String car_type_name="Panamera"+dt.currentDateToTimestamp();

            createCar(car_type_name);
            //pc看车列&详情
            JSONArray listA=crm.carList().getJSONArray("list");
            int totalA=0;
            if(listA==null||listA.size()==0){
                throw new Exception("商品管理，新建车型，车辆数还是0");
            }else{
                totalA=listA.size();
            }
            String car_type_nameA=listA.getJSONObject(listA.size()-1).getString("car_type_name");
            String car_id=listA.getJSONObject(listA.size()-1).getString("id");  //新建车型id
            String price=listA.getJSONObject(listA.size()-1).getString("price");

            Preconditions.checkArgument((totalA-total)==1,"pc新建车型，pc车辆列表没+1");
//            Preconditions.checkArgument(car_type_name.equals(car_type_nameA),"pc列表车型名称显示错误");
//            Preconditions.checkArgument(price.equals("88.99-888.99万"),"pc新建车辆，pc列表售价显示异常");
            //删除车辆
            crm.carDelete(Integer.parseInt(car_id));
            JSONArray listB=crm.carList().getJSONArray("list");
            int totalB=0;
            if(listB==null||listB.size()==0){
                totalB=0;
            }else{
                totalB=listB.size();
            }
            Preconditions.checkArgument((totalA-totalB)==1,"pc删除车型，pc车辆列表没-1");

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("看车。pc新建车型，pc车辆列表+1&信息校验");
        }
    }



    /**
     * @description :报名列表加入黑名单，黑名单增+1；释放-1&列表信息校验 ok
     * @date :2020/7/15 11:13
     **/
    @Test(priority = 2)   //TODO:
    public void pcblackList(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Long [] aid=createAArcile_id(dt.getHistoryDate(0),"8");
            Long activity_id=aid[1];
            Long id=aid[0];
            //活动报名
            crm.appletLogin(code);
            String other_brand="奥迪pc-黑名单报名";
            String customer_num="2";
            //预约使用参数
            String customer_name = "@@@2";
            String customer_phone_number = "15037286013";
            String appointment_date = dt.getHistoryDate(1);  //预约日期取当前天的前一天
            Integer car_type = 1;
            String car_type_name = "";
            JSONObject data1=crm.joinActivity(Long.toString(activity_id),customer_name,customer_phone_number,appointment_date,car_type,other_brand,customer_num);
            String appointment_id=data1.getString("appointment_id");
            //crm.cancle(Long.parseLong(appointment_id));  //取消活动报名
            crm.login(adminname,adminpassword);
            //报名客户 页获取客户id
            JSONArray list=crm.activityList(1,10,activity_id).getJSONArray("list");
            String customer_type=list.getJSONObject(0).getString("customer_type");
            String customer_id=list.getJSONObject(0).getString("customer_id");
            String customer_name1=list.getJSONObject(0).getString("customer_name");
            String customer_phone_number1=list.getJSONObject(0).getString("customer_phone_number");
            String appointment_activity_total=list.getJSONObject(0).getString("appointment_activity_total"); //报名次数，TODO:首次报名，报名次数=1
            //pc黑名单客户列表total
            String blackTotal=crm.blacklist(1,10).getString("total");
            Integer total;
            if(blackTotal==null){
                total=0;
            }else{
                total=Integer.parseInt(blackTotal);
            }
            //加入黑名单
            crm.blackadd(customer_id);
            String time=dt.getHistoryDate(0);
            JSONObject data=crm.blacklist(1,10);
            String blackTotalA=data.getString("total");
            JSONArray listA=data.getJSONArray("list");
            String customer_typeA=listA.getJSONObject(0).getString("customer_type");
            String customer_nameA=listA.getJSONObject(0).getString("customer_name");
            String customer_phone_numberA=listA.getJSONObject(0).getString("customer_phone_number");
            String timeA=listA.getJSONObject(0).getString("time");         //划入时间
            String order_number=listA.getJSONObject(0).getString("order_number"); //报名次数

            Integer totalA;
            if(blackTotal==null){
                throw new Exception("报名客户加入黑名单，黑名单列表仍是空");
            }else{
                totalA=Integer.parseInt(blackTotalA);
            }
            //释放 黑名单
            crm.blackRemove(customer_id);
            String blackTotalB=crm.blacklist(1,10).getString("total");
            Integer totalB=0;
            if(blackTotalB==null){
                totalB=0;
            }else{
                totalB=Integer.parseInt(blackTotalB);
            }

            Preconditions.checkArgument((totalA-total)==1,"报名客户加入黑名单，黑名单列表没+1");
            Preconditions.checkArgument((totalA-totalB)==1,"释放黑名单，黑名单列表没-1");
//             Preconditions.checkArgument(customer_name1.equals(customer_nameA),"黑名单中，顾客姓名错误");
            Preconditions.checkArgument(customer_phone_number1.equals(customer_phone_numberA),"黑名单中，客户电话错误");
//             Preconditions.checkArgument(customer_type.equals(customer_typeA),"黑名单中，客户类型错误");
            Preconditions.checkArgument(appointment_activity_total.equals(order_number),"黑名单中，报名次数错误");
            Preconditions.checkArgument(time.equals(timeA),"黑名单中，划入时间错误");
            crm.articleStatusChange(id);
            crm.articleDelete(id);


        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("报名列表加入黑名单，黑名单增+1；释放-1,&黑名单信息校验");
        }
    }

    /**
     * @description :人员管理
     * @date :2020/7/19 19:06
     **/
    @Test(dataProvider = "ROLE_IDS",dataProviderClass = CrmScenarioUtil.class)
    public void peopelmange(Integer role_ids){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //删除排版销售前，排班人数
            JSONArray list=crm.ManageList(role_ids).getJSONArray("list");
            int total;
            if(list==null||list.size()==0){
                total=0;
            }else {total=list.size();}
            JSONArray listN=crm.ManageListNoSelect(role_ids).getJSONArray("list");
            if(listN==null||listN.size()==0){
                throw new Exception("未创建销售顾问，无法排班");
            }
            //增加排班
            String uid=listN.getJSONObject(0).getString("uid");
            crm.ManageAdd(role_ids,uid);
            //增加排班人数后，人数
            JSONArray listA=crm.ManageList(role_ids).getJSONArray("list");
            int totalA;
            if(list==null||listA.size()==0){
                totalA=0;
            }else {totalA=listA.size();}
            Preconditions.checkArgument((totalA-total)==1,"增加销售排版");
            //删除增加的排班
            JSONArray listB=crm.ManageList(role_ids).getJSONArray("list");
            int index=listB.size()-1;
            Integer id=crm.ManageList(role_ids).getJSONArray("list").getJSONObject(index).getInteger("id");
            crm.ManageDelete(id);

            JSONArray listD=crm.ManageList(role_ids).getJSONArray("list");
            int totalD;
            if(listD==null||listD.size()==0){
                totalD=0;
            }else {totalD=listD.size();}
            Preconditions.checkArgument(totalD==total,"删除排班销售，排版数-1");

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("【人员管理】增删销售排班，列表+-1");
        }
    }

    /**
     * @description :人员管理，删除大池子销售/维修/保养顾问，小池子-1
     * @date :2020/7/28 19:44
     **/
    @Test
    public void deletaGuwen(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //主账号登录
            crm.login(baoshijie,adminpassword);
            //创建销售/顾问
            String userName = ""+ System.currentTimeMillis();
            int roleId=13; //销售顾问
            String passwd="123456";

            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }
            crm.addUser(userName,userName,phone,passwd,roleId);
            JSONObject data=crm.userPage(1,100);
            int total=data.getInteger("total");
            JSONArray list=new JSONArray();
            if(total==200){
                logger.info("用户数量已达上限");
                return;
            }
            else if(total<100){
                list = data.getJSONArray("list");
            }else{
                list=crm.userPage(2,100).getJSONArray("list");
            }
            String userid = list.getJSONObject(list.size()-1).getString("user_id"); //获取用户id
            //加人到小池子
            crm.login(adminname,adminpassword);
            crm.ManageAdd(roleId,userid);
            JSONArray listA=crm.ManageList(roleId).getJSONArray("list");
            int totalA=listA.size();   //删除前小池子数量
            //删除大池子
            crm.login(baoshijie,adminpassword);
            crm.userDel(userid);

            crm.login(adminname,adminpassword);
            JSONArray listB=crm.ManageList(roleId).getJSONArray("list");
            int totalB=listB.size();   //删除后小池子数量
            Preconditions.checkArgument((totalA-totalB)==1,"人员管理，删除大池子销售/维修/保养顾问，小池子没-1");

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("人员管理，删除大池子销售/维修/保养顾问，小池子-1");
        }
    }

    /**
     * @description :【人员管理】crm中销售数量{}=新增下拉框销售数量{}+当前列表数量{}
     * @date :2020/7/31 16:41
     **/
    @Test(dataProvider = "ROLE_IDS",dataProviderClass = CrmScenarioUtil.class)
    public void roleListCrm(Integer role_ids){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray list=crm.ManageList(role_ids).getJSONArray("list");
            int total;
            if(list==null||list.size()==0){
                total=0;
            }else {total=list.size();}
            JSONArray listN=crm.ManageListNoSelect(role_ids).getJSONArray("list");
            int totalNoSelect;
            if(listN==null){
                totalNoSelect=0;
            }else{
                totalNoSelect=listN.size();
            }
            //crm中校色数统计
            int crmRoleTotal=0;
            //100以内
            crm.login("baoshijie",adminpassword);
            JSONObject data=crm.userPage(1,100);
            int crmTotal=data.getInteger("total");
            JSONArray listC=data.getJSONArray("list");
            for(int i=0;i<listC.size();i++){
                int role_idC=listC.getJSONObject(i).getInteger("role_id");
                String user_id=listC.getJSONObject(i).getString("user_id");
                logger.info("------user_id:{}------",user_id);
                if(role_idC==role_ids){
                    crmRoleTotal=crmRoleTotal+1;
                }
            }
            //若超过100
            if(crmTotal>100){
                JSONObject data2=crm.userPage(2,100);
                JSONArray listC2=data2.getJSONArray("list");
                for(int i=0;i<listC2.size();i++){
                    int role_idC2=listC2.getJSONObject(i).getInteger("role_id");
                    if(role_idC2==role_ids){
                        crmRoleTotal=crmRoleTotal+1;
                    }
                }
            }
            crm.login(adminname,adminpassword);
            logger.info("crm中销售数量{}=新增下拉框销售数量{}+当前列表数量{}",crmRoleTotal,totalNoSelect,total);
            Preconditions.checkArgument(crmRoleTotal==total+totalNoSelect,"crm中销售数量{}=新增下拉框销售数量{}+当前列表数量{}",crmRoleTotal,totalNoSelect,total);

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("【人员管理】crm中销售数量{}=新增下拉框销售数量{}+当前列表数量{}");
        }
    }


    /**
     * @description :app活动报名，报名信息上限50
     * @date :2020/7/30 19:45
     **/
    @Test
    public void appactivity(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //创建活动，获取活动id
            Long [] aid=createAArcile_id(dt.getHistoryDate(0),"8");
            Long activity_id=aid[1];
            Long id=aid[0];
            crm.login("baoyangr",adminpassword);
            JSONObject response = crm.activityTaskPageX();
            JSONObject json = response.getJSONObject("data").getJSONArray("list").getJSONObject(0);
            int activityTaskId = json.getInteger("activity_task_id");
            int before_total=0;
            String userLoginName = ""+ System.currentTimeMillis();

            while (before_total<50){
                String newloginname = userLoginName+before_total;
                String phone = "1";
                for (int i = 0; i < 10;i++){
                    String a = Integer.toString((int)(Math.random()*10));
                    phone = phone + a;
                }
                crm.registeredCustomer((long) activityTaskId, "夏", phone);
                before_total = before_total +1;
            }
            //添加第51个
            Long code=crm.registeredCustomerCode((long) activityTaskId, "夏蝈蝈", "15037286612");
            Preconditions.checkArgument(code==1001,"app添加报名人信息上限50条");
            crm.login(adminname,adminpassword);
            crm.articleStatusChange(id);
            crm.articleDelete(id);
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("app活动报名，报名信息上限50");
        }
    }
    /**
     * @description :app活动报名，添加报过名的电话，失败
     * @date :2020/8/3 16:21
     **/
    @Test
    public void appactivitySamePhone(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //创建活动，获取活动id
            Long [] aid=createAArcile_id(dt.getHistoryDate(0),"8");
            Long activity_id=aid[1];
            Long id=aid[0];
            crm.login(lxqgw,adminpassword);
            JSONObject response = crm.activityTaskPageX();
            JSONObject json = response.getJSONObject("data").getJSONArray("list").getJSONObject(0);
            int activityTaskId = json.getInteger("activity_task_id");
            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }
            crm.registeredCustomer((long) activityTaskId, "夏", phone);
            //添加第51个
            Long code=crm.registeredCustomerCode((long) activityTaskId, "夏蝈蝈", phone);
            Preconditions.checkArgument(code==1001,"app添加报过名的电话，应该失败");
            crm.login(adminname,adminpassword);
            crm.articleStatusChange(id);
            crm.articleDelete(id);
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("活动报名，添加报过名的电话，失败");
        }
    }



    /**
     * @description :app活动报名，pc任务客户+1&信息校验
     * @date :2020/7/30 19:45
     **/
    @Test
    public void taskactivity(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //创建活动，获取活动id
            Long [] aid=createAArcile_id(dt.getHistoryDate(0),"8");
            Long activity_id=aid[1];
            Long id=aid[0];
            JSONObject data=crm.customerTaskPageX(10,1,activity_id).getJSONObject("data");
            int total=data.getInteger("total");
            crm.login(lxqgw,adminpassword);
            JSONObject response = crm.activityTaskPageX();
            JSONObject json = response.getJSONObject("data").getJSONArray("list").getJSONObject(0);
            int activityTaskId = json.getInteger("activity_task_id");
            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }
            crm.registeredCustomer((long) activityTaskId, "夏", phone);
            crm.login(adminname,adminpassword);
            JSONObject dataA=crm.customerTaskPageX(10,1,activity_id).getJSONObject("data");
            int totalA=dataA.getInteger("total");
            JSONArray list1=dataA.getJSONArray("list");

            JSONObject list=list1.getJSONObject(0);
            String customer_type=list.getString("customer_type");
            String customer_name=list.getString("customer_name");
            String customer_phone_number=list.getString("customer_phone_number");
            String sale_name=list.getString("sale_name");
            //获取报名字段，校验
            Preconditions.checkArgument(totalA-total==1,"app报名活动，该任务任务客户+1");
            Preconditions.checkArgument(customer_name.equals("夏"),"任务客户报名人名错误");
            Preconditions.checkArgument(customer_type.equals("售前/售后"),"活动类型错误错误");
            Preconditions.checkArgument(customer_phone_number.equals(phone),"任务客户报名人电话错误");
            Preconditions.checkArgument(sale_name.equals(lxqgw),"任务客户报名销售名错误");


            crm.login(adminname,adminpassword);
            crm.articleStatusChange(id);
            crm.articleDelete(id);
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("app活动报名，pc任务客户+1&信息校验");
        }
    }

    /**
     * @description :app活动报名，删除报名客户，pc任务客户-1
     * @date :2020/8/3 18:38
     **/
    @Test
    public void taskactivityDelet(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //创建活动，获取活动id
            Long [] aid=createAArcile_id(dt.getHistoryDate(0),"8");
            Long activity_id=aid[1];
            Long id=aid[0];
            crm.login(lxqgw,adminpassword);
            JSONObject response = crm.activityTaskPageX();
            JSONObject json = response.getJSONObject("data").getJSONArray("list").getJSONObject(0);
            int activityTaskId = json.getInteger("activity_task_id");
            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }
            crm.registeredCustomer((long) activityTaskId, "夏", phone);
            JSONObject responseapp = crm.activityTaskPageX();
            JSONObject jsonapp = responseapp.getJSONObject("data").getJSONArray("list").getJSONObject(0);
            Long customer_id = jsonapp.getJSONArray("customer_list").getJSONObject(0).getLong("customer_id");
            //报名后，任务人数
            crm.login(adminname,adminpassword);
            JSONObject data=crm.customerTaskPageX(10,1,activity_id).getJSONObject("data");
            int total=data.getInteger("total");

            //删除报名客户
            crm.login(lxqgw,adminpassword);
            crm.deleteCustomerX(Integer.toString(activityTaskId),Long.toString(customer_id) );
            //删除后，任务人数
            crm.login(adminname,adminpassword);
            JSONObject dataA=crm.customerTaskPageX(10,1,activity_id).getJSONObject("data");
            int totalA=dataA.getInteger("total");

            Preconditions.checkArgument(total-totalA==1,"app报名活动，删除报名客户，pc任务客户没-1");

            crm.articleStatusChange(id);
            crm.articleDelete(id);
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("app活动报名，删除报名客户，pc任务客户-1");
        }
    }

    /**
     * @description :小程序车主风采<=pc今日交车数 TODO:  车主风采中需要是今日才成立，若今日无交车，则此case 不成立
     * @date :2020/8/2 15:41
     **/
    //@Test
    public void carOwer(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //pc今日交车数
            crm.login(adminname,adminpassword);
            long totalDeliverCar=crm.deliverCarList(1,10).getLong("total");
            if(totalDeliverCar==0){
                return;
            }
            crm.appletLogin("123456");
            JSONArray list=crm.carOwner().getJSONArray("list");
            int total;
            if(list==null||list.size()==0){
                total=0;
            }else{
                total=list.size();
            }
            crm.login(adminname,adminpassword);
            Preconditions.checkArgument(total<=totalDeliverCar,"小程序车主风采>pc今日交车数");

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("小程序车主风采<=pc今日交车数 ");
        }
    }


    // @Test
    public void deleteuser(){
        try {
            crm.login("baoshijie", adminpassword);
//              for(int j=0;j<1;j++) {
            JSONArray list = crm.userPage(6, 10).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String userid = list.getJSONObject(i).getString("user_id"); //获取用户id
                crm.userDel(userid);
            }
//              }

        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("清多余用户数据");
        }
    }

    /**
     * @description :预约记录查询验证预约记录查询验证，今日数=列表去重数,数字统计按创建日期，页面无创建日期，故此case不通 TODO:可用于app试驾、交车
     * @date :2020/7/31 13:55
     **/
    //@Test
    public void appointmentRecoed(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject data=crm.appointmentpage(dt.getHistoryDate(0),dt.getHistoryDate(0),1,100);
            int today_number=data.getInteger("today_number");
            JSONArray list=data.getJSONArray("list");
            List<String> numList = new ArrayList<String>();
            if(list==null||list.size()==0){
                logger.info("今日无预约记录");
                return;
            }
            for(int i=0;i<list.size();i++){
                String phone=list.getJSONObject(i).getString("customer_phone_number");
                String service_status_name=list.getJSONObject(i).getString("service_status_name");
                if(!service_status_name.equals("已取消")){
                    numList.add(phone);
                }
            }
            Set<String> numSet = new HashSet<String>();
            numSet.addAll(numList);
            int similar=numSet.size();
            Preconditions.checkArgument(similar==today_number,"今日预约数！=今日列表电话号码去重数");

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("预约记录查询验证预约记录查询验证，今日数=列表去重数");
        }
    }

    /**
     * @description :预约试驾记录查询，按日期查询及结果验证
     * @date :2020/7/31 14:37
     **/
    @Test(dataProvider = "SELECT_DATE",dataProviderClass = CrmScenarioUtil.class)
    public void driverRecoedSelect(String select_date){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject data=crm.appointmentpage(select_date,select_date,1,50);
            JSONArray list=data.getJSONArray("list");
            if(list==null||list.size()==0){
                logger.info("当日无预约记录");
            }
            for(int i=0;i<list.size();i++){
                String order_date=list.getJSONObject(i).getString("order_date");
                Preconditions.checkArgument(order_date.equals(select_date),"预约记录按日期查询，查询到记录日期{}",order_date);
            }
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("预约试驾记录查询");
        }
    }

    /**
     * @description :预约保养记录查询，按日期查询及结果验证
     * @date :2020/7/31 14:37
     **/
    @Test(dataProvider = "SELECT_DATE",dataProviderClass = CrmScenarioUtil.class)
    public void maintainRecoedSelect(String select_date){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject data=crm.mainAppointmentpage(select_date,select_date,1,50);
            JSONArray list=data.getJSONArray("list");
            if(list==null||list.size()==0){
                logger.info("当日无预约记录");
            }
            for(int i=0;i<list.size();i++){
                String order_date=list.getJSONObject(i).getString("order_date");
                Preconditions.checkArgument(order_date.equals(select_date),"预约记录按日期查询，查询到记录日期{}",order_date);
            }
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("预约保养记录查询");
        }
    }

    /**
     * @description :预约维修记录查询，按日期查询及结果验证
     * @date :2020/7/31 14:37
     **/
    @Test(dataProvider = "SELECT_DATE",dataProviderClass = CrmScenarioUtil.class)
    public void repairRecoedSelect(String select_date){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject data=crm.repairAppointmentpage(select_date,select_date,1,50);
            JSONArray list=data.getJSONArray("list");
            if(list==null||list.size()==0){
                logger.info("当日无预约记录");
            }
            for(int i=0;i<list.size();i++){
                String order_date=list.getJSONObject(i).getString("order_date");
                Preconditions.checkArgument(order_date.equals(select_date),"预约记录按日期查询，查询到记录日期{}",order_date);
            }
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("预约维修记录查询");
        }
    }

    /**
     * @description :黑名单记录查询，按日期查询及结果验证   ok
     * @date :2020/7/31 14:37
     **/
    @Test(dataProvider = "SELECT_DATE",dataProviderClass = CrmScenarioUtil.class)
    public void blackList(String select_date){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject data=crm.blacklist(select_date,select_date,1,50);
            JSONArray list=data.getJSONArray("list");
            if(list==null||list.size()==0){
                logger.info("当日无拉黑名单");
            }
            for(int i=0;i<list.size();i++){
                String order_date=list.getJSONObject(i).getString("order_date");
                Preconditions.checkArgument(order_date.equals(select_date),"黑名单按日期查询，查询到记录日期{}",order_date);
            }
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("黑名单记录查询");
        }
    }

    /**
     * @description :今日试驾共计<本月共计
     * @date :2020/7/31 16:02
     **/
    @Test
    public void todayLower(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject data=crm.appointmentpage(1,100);
            int total_number=data.getInteger("total_number");
            int month_number=data.getInteger("month_number");
            int today_number=data.getInteger("today_number");
            int total=data.getInteger("total");
            Preconditions.checkArgument(today_number<=month_number,"今日试驾总数>本月共计");
            Preconditions.checkArgument(month_number<=total_number,"本月共计>累计");
            Preconditions.checkArgument(total_number<=total,"今日累计>列表总数");
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("今日试驾共计<本月共计<累计<列表总数");
        }
    }
    /**
     * @description :今日保养共计<本月共计
     * @date :2020/7/31 16:02
     **/
    @Test
    public void repairtodayLower(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject data=crm.mainAppointmentpage(1,100);
            int total_number=data.getInteger("total_number");
            int month_number=data.getInteger("month_number");
            int today_number=data.getInteger("today_number");
            int total=data.getInteger("total");
            Preconditions.checkArgument(today_number<=month_number,"今日保养总数>本月共计");
            Preconditions.checkArgument(month_number<=total_number,"本月共计>累计");
            Preconditions.checkArgument(total_number<=total,"今日累计>列表总数");
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("今日保养共计<本月共计<累计<列表总数");
        }
    }

    /**
     * @description :今日维修共计<本月共计
     * @date :2020/7/31 16:02
     **/
    @Test
    public void mainTaintodayLower(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject data=crm.repairAppointmentpage(1,100);
            int total_number=data.getInteger("total_number");
            int month_number=data.getInteger("month_number");
            int today_number=data.getInteger("today_number");
            int total=data.getInteger("total");
            Preconditions.checkArgument(today_number<=month_number,"今日维修总数>本月共计");
            Preconditions.checkArgument(month_number<=total_number,"本月共计>累计");
            Preconditions.checkArgument(total_number<=total,"今日累计>列表总数");
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("今日维修共计<本月共计<累计<列表总数");
        }
    }

    /**
     * @description :车型推荐车数量==商品管理数量  TODO:
     * @date :2020/8/12 17:22
     **/
     @Test
    public void carCommend(){
         logger.logCaseStart(caseResult.getCaseName());
         try{
             String totalRecomend=crm.carReCommendList(1,10).getString("total");
             JSONArray list=crm.carList().getJSONArray("list");
             int totalCar=0;
             if(list==null||list.size()==0){
                 totalCar=0;
             }else{
                 totalCar=list.size();
             }
             Preconditions.checkArgument(totalCar==Integer.valueOf(totalRecomend),"车型推荐车数量！=商品管理数量");

         }catch (AssertionError e){
             appendFailreason(e.toString());
         }catch (Exception e){
             appendFailreason(e.toString());
         }finally {
             saveData("车型推荐车数量==商品管理数量");
         }
     }

     /**
      * @description :站内消息与小程序收到的消息一致性校验 TODO；
      * @date :2020/8/12 17:51
      **/
     @Test(dataProvider = "APPOINTMENT_TYPE",dataProviderClass = CrmScenarioUtil.class)
     public void messageInter(String appointment_type){
         logger.logCaseStart(caseResult.getCaseName());
         try{
//             String appointment_type = "TEST_DRIVER";
             //pc创建站内消息
             String[] customer_types = {"PRE_SALES", "AFTER_SALES"};
             int[] customer_level = {};           //TODO:客户等级
             String[] customer_property = {};
             String sendTime = dt.getHistoryDate(4);
             int[] car_types = {};
             String messageTitile = "暑期特惠" + dt.getHistoryDate(0);
             String messageContent = "站内自动消息内容";

             //有效活动列表
            JSONArray list= crm.activityVaild().getJSONArray("list");
            if(list==null||list.size()==0){
                crm.createMessage(customer_types,car_types,customer_level,customer_property,sendTime,messageTitile,messageContent);
            }else{
                Long activityId=list.getJSONObject(0).getLong("id");
                crm.createMessage(customer_types,car_types,customer_level,customer_property,sendTime,messageTitile,messageContent,appointment_type,activityId);

            }
            //我的消息页
             JSONArray messagePage=crm.messageList(10).getJSONArray("list");
            Long id=messagePage.getJSONObject(0).getLong("id");
            JSONObject dataM=crm.messageDetail(id);

            String titleM=dataM.getString("title");
            String contentM=dataM.getString("content");
            String appointment_typeM=dataM.getString("appointment_type");
            String activity_id=dataM.getString("activity_id");

            Preconditions.checkArgument(titleM.equals(messageTitile),"站内消息标题显示错误");
            Preconditions.checkArgument(contentM.equals(messageContent),"站内消息内容显示错误");





         }catch (AssertionError e){
             appendFailreason(e.toString());
         }catch (Exception e){
             appendFailreason(e.toString());
         }finally {
             saveData("车型推荐车数量==商品管理数量");
         }
     }









}
