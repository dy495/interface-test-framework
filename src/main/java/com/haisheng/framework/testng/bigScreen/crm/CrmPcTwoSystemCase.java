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
/**
 * @description :crm2.0 pc case--xia
 * @date :2020/7/15 20:22
 **/


public class CrmPcTwoSystemCase extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    public String adminname="xx";    //pc登录密码，最好销售总监或总经理权限
    public String adminpassword="e10adc3949ba59abbe56e057f20f883e";
    public String code="1234567";

    public Integer car_type = 1;
    public String car_type_name = "Panamera";
    public Long activity_id =43L;

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
            String[] positions = {"CAR_ACTIVITY"}; //投放位置车型推荐 单选
            // String [] positions={"MODEL_RECOMMENDATION","PURCHASE_GUIDE","BRAND_CULTURE","CAR_ACTIVITY"};
//            String valid_start = dt.getHistoryDate(0);
            String valid_end = dt.getHistoryDate(4);
            int[] car_types = {car_type};
            String article_title = "品牌上新，优惠多多，限时4天---" + dt.getHistoryDate(0);
            String article_bg_pic = texFile("src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic");  //base 64
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
            article_id = crm.createArticle(positions, valid_start, valid_end, customer_types, car_types, customer_level, customer_property, article_title, article_bg_pic, article_content, article_remarks, is_online_activity, reception_name, reception_phone, customer_max, simulation_num, activity_start, activity_end, role_id, Integer.toString(task_customer_num), is_create_poster).getLong("id");
            activity_id=crm.appartilceDetail(article_id).getLong("activity_id");
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
            String[] positions = {"CAR_ACTIVITY"}; //投放位置车型推荐 单选
            // String [] positions={"MODEL_RECOMMENDATION","PURCHASE_GUIDE","BRAND_CULTURE","CAR_ACTIVITY"};
//            String valid_start = dt.getHistoryDate(0);
            String valid_end = dt.getHistoryDate(4);
            int[] car_types = {car_type};
            String article_title = "品牌上新，优惠多多，限时4天---" + dt.getHistoryDate(0);
            String article_bg_pic = texFile("src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic");  //base 64
            String article_content = "品牌上新，优惠多多，限时4天,活动内容";
            String article_remarks = "品牌上新，优惠多多，限时4天,备注";

            boolean is_online_activity = true;  //是否线上报名活动
//            String reception_name = manage(13)[0];  //接待人员名
//            String reception_phone = manage(13)[1]; //接待人员电话
            String reception_name = "xx";  //接待人员名
            String reception_phone = "15037286013"; //接待人员电话
            String customer_max = "50";                    //人数上限

            String activity_start = dt.getHistoryDate(0);
            String activity_end = dt.getHistoryDate(4);
            Integer role_id = 13;
            Boolean is_create_poster = true;//是否生成海报
            Integer task_customer_numa = crm.groupTotal(customer_types, car_types, customer_level, customer_property).getInteger("total");
            Integer task_customer_num=5;
            //新建文章并返回文章/活动id
            article_id = crm.createArticle(positions, valid_start, valid_end, customer_types, car_types, customer_level, customer_property, article_title, article_bg_pic, article_content, article_remarks, is_online_activity, reception_name, reception_phone, customer_max, simulation_num, activity_start, activity_end, role_id, Integer.toString(task_customer_num), is_create_poster).getLong("id");
            activity_id = article_id;
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
        String[] positions = {positionsA};         //投放位置车型推荐 单选
        // String [] positions={"MODEL_RECOMMENDATION","PURCHASE_GUIDE","BRAND_CULTURE","CAR_ACTIVITY"};
        String valid_start = dt.getHistoryDate(0);
        String valid_end = dt.getHistoryDate(4);
        int[] car_types = {car_type};
        //String article_title = "品牌上新，优惠多多，限时4天---" + dt.getHistoryDate(0);
        String article_bg_pic = texFile("src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic");  //base 64
        String article_content = "品牌上新，优惠多多，限时4天,文章内容";
        String article_remarks = "品牌上新，优惠多多，限时4天,备注";
        //新建文章，获取id
        Long actriclereal_id=crm.createArticleReal(positions,valid_start,valid_end,customer_types,car_types,customer_level,customer_property,article_title,article_bg_pic,article_content,article_remarks,false).getLong("id");
        return actriclereal_id;
    }

    //创建车辆
    public void createCar(String car_type_name) throws Exception{

        double lowest_price=899999.99;
        double highest_price=1899999.99;
        String car_discount="限时优惠";
        String car_introduce="车型介绍，超大空间";
        String car_pic=texFile("src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic");  //base 64
        String big_pic=texFile("src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic");  //base 64
        String interior_pic=texFile("src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic");  //base 64
        String space_pic=texFile("src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic");  //base 64
        String appearance_pic=texFile("src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic");  //base 64
        crm.addCarPc(car_type_name,lowest_price,highest_price,car_discount,car_introduce,car_pic,big_pic,interior_pic,space_pic,appearance_pic);

    }

   /**
    * @description :pc新建活动，数据一致，pc活动列表+1，列表信息校验，新建活动时投放人数等于该活动发送短信页总人数；查看文章详情信息校验
    * @date :2020/7/14 10:23
    **/
    @Test(priority = 1)
    public void articleManage(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Long totalB=crm.articlePage(1,10).getLong("total");
            String []customer_types={"PRE_SALES","AFTER_SALES"};
            int []customer_level={};                          //客户等级
            String []customer_property={};
            String [] positions={"CAR_ACTIVITY"}; //投放位置购买指南单选
            // String [] positions={"MODEL_RECOMMENDATION","PURCHASE_GUIDE","BRAND_CULTURE","CAR_ACTIVITY"};
            String valid_start=dt.getHistoryDate(0);
            String valid_end=dt.getHistoryDate(4);
            int []car_types={car_type};
            String article_title="购买指南，品牌上新，优惠多多，限时4天---"+dt.getHistoryDate(0);
            String article_bg_pic=texFile("src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic");  //base 64
            String article_content="购买指南，品牌上新，优惠多多，限时4天,活动内容";
            String article_remarks="购买指南，品牌上新，优惠多多，限时4天,备注";

            boolean is_online_activity=true;  //是否线上报名活动
//            String reception_name=manage(13)[0];  //接待人员名
//            String reception_phone=manage(13)[1]; //接待人员电话
            String reception_name="xia1";  //接待人员名
            String reception_phone="12222222229"; //接待人员电话
            String customer_max="20";                    //人数上限
            String simulation_num="10";                   //假定基数
            String activity_start=dt.getHistoryDate(0);
            String activity_end=dt.getHistoryDate(4);
            Integer role_id=15;
            Boolean is_create_poster=true;//是否生成海报
            Integer task_customer_numa=crm.groupTotal(customer_types,car_types,customer_level,customer_property).getInteger("total");
            Integer task_customer_num=5;
            //新建活动并返回活动id
            Long article_id=crm.createArticle(positions,valid_start,valid_end,customer_types,car_types,customer_level,customer_property,article_title,article_bg_pic,article_content,article_remarks,is_online_activity,reception_name,reception_phone,customer_max,simulation_num,activity_start,activity_end,role_id,Integer.toString(task_customer_num),is_create_poster).getLong("id");
            //activity_id = article_id;

            JSONObject pcarctile=crm.artilceDetailpc(article_id);  //pc活动详情
            String detail_customer_max=pcarctile.getString("customer_max");
            String detail_registered_num=pcarctile.getString("registered_num");  //已经报名人数
            String detail_article_title=pcarctile.getString("article_title");
            String detail_article_content=pcarctile.getString("article_content");


            //新增活动，活动列表+1
            JSONObject data=crm.articlePage(1,10);
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
            Preconditions.checkArgument(positionsA.equals("看车页"),"新建活动列表信息，投放位置展示错误");
            Preconditions.checkArgument(valid_endA.equals(valid_end),"新建活动列表信息，失效时间展示错误");
            Preconditions.checkArgument(valid_startA.equals(valid_start),"新建活动列表信息，生效时间展示错误");
            Preconditions.checkArgument(statusA.equals("SHOW"),"新建活动列表信息，状态展示错误");

            //查看详情信息校验
            Preconditions.checkArgument(detail_customer_max.equals(customer_max),"查看文章详情，报名总额错误");
            Preconditions.checkArgument(detail_article_title.equals(article_title),"查看文章详情，文章内容错误");
            Preconditions.checkArgument(detail_article_content.equals(article_content),"查看文章详情，文章标题错误");
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
     * @description :pc内容运营，状态，删除文章；展示中的活动不应该能被删除，下架的活动能被删除且活动列表-1    新建了活动
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
            String total=crm.articlePage(1,10).getString("total");
            //删除正在运行的活动失败
            Long code=crm.articleDelete(id).getLong("code");
            crm.articleStatusChange(id);       //改变活动状态
            //删除下架活动成功
            Long codeA=crm.articleDelete(id).getLong("code");

            //删除活动列表-1
            String totalA=crm.articlePage(1,10).getString("total");
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
            String total=crm.articlePage(1,10).getString("total");
            String num="6"; //假定基数
            String valid_start = dt.getHistoryDate(1);
            Long [] aid=createAArcile_id(valid_start,num);
            Long activity_id=aid[1];
            Long id=aid[0];

            //删除排期活动成功 列表-1
            Long code=crm.articleDelete(id).getLong("code");
            //删除活动列表-1
            String totalA=crm.articlePage(1,10).getString("total");
            Preconditions.checkArgument(code==1000,"删除排期活动成功");
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
      * @description :新建文章，小程序显示文章内容校验(车型推荐、购买指南、、、、)
      * @date :2020/7/14 20:16
      **/
     @Test(dataProvider = "POSITIONS",dataProviderClass = CrmScenarioUtil.class)
     public void articleTitleCompare(String positionsA){
         logger.logCaseStart(caseResult.getCaseName());
         try{
             crm.login(adminname,adminpassword);
             String article_title = "品牌上新，优惠多多，限时4天---" + dt.getHistoryDate(0);
             String article_content = "品牌上新，优惠多多，限时4天,文章内容";
             String article_remarks = "品牌上新，优惠多多，限时4天,备注";
             //新建文章，获取id
             Long actriclereal_id=createArcile(positionsA,article_title);
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
      * @description :看车。pc新建车型，applet看车页车辆列表+1&信息校验（车辆详情于pc配置的一致）
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
             //applet 看车列&详情
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
             Preconditions.checkArgument(totalA==total,"pc新建车型，applet看车页车辆列表没+1");
             Preconditions.checkArgument(car_type_name.equals(car_type_nameA),"pc新建车辆，applet未显示");
             //TODO:价格校验
//             Preconditions.checkArgument(price.equals(""),"pc新建车辆，applet售价显示异常");


         }catch (AssertionError e){
             appendFailreason(e.toString());
         }catch (Exception e){
             appendFailreason(e.toString());
         }finally {
             saveData("看车。pc新建车型，applet看车页车辆列表+1&信息校验");
         }
     }

    /**
     * @description :商品管理。pc新建车型，车辆列表+1；删除车辆-1--- TODO：不牌位的情况下，新建总在第一个
     * @date :2020/7/14 18:34
     **/
    @Test
    public void CarlistConsistency(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            crm.login(adminname,adminpassword);
            JSONArray list=crm.carList().getJSONArray("list");
            int total=0;
            if(list==null||list.size()==0){
                total=0;
            }else{
                total=list.size();
            }
            //pc创建车辆
            String car_type_name="bsj自动化车型"+dt.currentDateToTimestamp();

            createCar(car_type_name);
            //pc看车列&详情
            JSONArray listA=crm.carList().getJSONArray("list");
            int totalA=0;
            if(listA==null||listA.size()==0){
                throw new Exception("商品管理，新建车型，车辆数还是0");
            }else{
                totalA=listA.size();
            }
            String car_type_nameA=listA.getJSONObject(0).getString("car_type_name");
            String car_id=listA.getJSONObject(0).getString("id");  //新建车型id
            String price=listA.getJSONObject(0).getString("price");

            Preconditions.checkArgument((totalA-total)==1,"pc新建车型，pc车辆列表没+1");
            Preconditions.checkArgument(car_type_name.equals(car_type_nameA),"pc新建车辆，applet未显示");
            //TODO:价格校验
            //Preconditions.checkArgument(price.equals(""),"pc新建车辆，applet售价显示异常");
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
     * @description :商品管理。pc删除车型，车辆列表-1
     * @date :2020/7/15 18:34
     **/
    @Test
    public void CardeleteConsistency(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //创建车
            String car_type_name="bsj自动化车型"+dt.currentDateToTimestamp();
            createCar(car_type_name);
            JSONArray list=crm.carList().getJSONArray("list");
            int total=list.size();

            String car_id=list.getJSONObject(0).getString("id");  //新建车型id
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
            saveData("看车。pc新建车型，applet看车页车辆列表+1&信息校验");
        }
    }

    /**
      * @description :报名列表加入黑名单，黑名单增+1；释放-1&列表信息校验 TODO:
      * @date :2020/7/15 11:13
      **/
     @Test(priority = 2)
     public void pcblackList(){
         logger.logCaseStart(caseResult.getCaseName());
         try{
             Long [] aid=createAArcile_id(dt.getHistoryDate(0),"8");
             Long activity_id=aid[1];
             Long id=aid[0];
             //活动报名
             crm.appletlogin(code);
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
//             Preconditions.checkArgument(appointment_activity_total.equals(order_number),"黑名单中，报名次数错误");
//             Preconditions.checkArgument(time.equals(timeA),"黑名单中，划入时间错误");


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
      @Test
      public void peopelmange(){
          logger.logCaseStart(caseResult.getCaseName());
          try{

          }catch (AssertionError e){
              appendFailreason(e.toString());
          }catch (Exception e){
              appendFailreason(e.toString());
          }finally {
              saveData("");
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
