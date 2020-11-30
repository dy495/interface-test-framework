package com.haisheng.framework.testng.bigScreen.jiaochen.gly;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.Variable.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.SelectReception;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;
import java.lang.reflect.Method;

public class FilterColumnSystem extends TestCaseCommon implements TestCaseStd {
    ScenarioUtil jc = new ScenarioUtil();
//    JsonPathUtil jpu = new JsonPathUtil();
    public String shopId="";

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //checklist相关配置
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_JIAOCHEN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "gly";
        //replace backend gateway url
        //commonConfig.gateway = "";
        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "jc-daily-test");
        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "轿辰 日常 gly");
        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};
        //set shop id
        commonConfig.shopId = getProscheShop();
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);

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
     * @description :接待管理查询-筛选栏参数单项插查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_ReceptionManageFilter", dataProviderClass = Constant.class)
    public void selectAppointmentRecodeOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
       try{
           JSONObject respon=jc.receptionManage(shopId,"1","10","","");
           int pages=respon.getInteger("pages");
           String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
           for(int page=1;page<=pages;page++){
               JSONArray list=jc.receptionManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
               for(int i=0;i<10;i++){
                   String Flag=list.getJSONObject(i).getString(output);
                   Preconditions.checkArgument(Flag.contains(result), "接待管理按"+result+"查询，结果错误"+Flag);
               }
           }
       }catch(AssertionError | Exception e){
           appendFailReason(e.toString());
       }finally{
           saveData("接待管理查询单项查询，结果校验");
       }
    }

    /**
     * @description :接待管路-筛选栏参数全填查询
     * @date :2020/11/28
     **/
    @Test()
    public void selectAppointmentRecodeAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] ss = Constant.receptionManageFilter_pram();
            SelectReception sr=new SelectReception();
            JSONArray res=jc.receptionManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            sr.plate_number=data.getString(ss[0][1].toString());
            sr.reception_sale_id=data.getString(ss[1][1].toString());
            sr.reception_date=data.getString(ss[2][1].toString());
            sr.customer_name=data.getString(ss[3][1].toString());
            sr.reception_status =data.getString(ss[4][1].toString());
            sr.finish_date=data.getString(ss[5][1].toString());
            sr.customer_phone=data.getString(ss[6][1].toString());
            sr.reception_type =data.getString(ss[7][1].toString());
            sr.shop_id=data.getString(ss[8][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.receptionManageC(sr).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(ss[0][1].toString()).contains(sr.plate_number),"参数全部输入的查询的"+sr.plate_number+"与列表信息的第一行的"+result.getString(ss[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(ss[1][1].toString()).contains(sr.reception_sale_id ),"参数全部输入的查询的"+result.getString(ss[1][1].toString()+"与列表信息的第一行的"+sr.reception_sale_id+"不一致"));
            Preconditions.checkArgument(result.getString(ss[2][1].toString()).contains(sr.reception_date),"参数全部输入的查询的"+sr.reception_date+"与列表信息的第一行的"+result.getString(ss[2][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(ss[3][1])).contains(sr.customer_name),"参数全部输入的查询的"+sr.customer_name+"与列表信息的第一行的"+result.getString(ss[3][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(ss[4][1].toString()).contains(sr.reception_status),"参数全部输入的查询的"+sr.reception_status+"与列表信息的第一行的"+result.getString(ss[4][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(ss[0][1])).contains(sr.finish_date),"参数全部输入的查询的"+sr.finish_date+"与列表信息的第一行的"+result.getString(String.valueOf(ss[0][1])+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(ss[6][1])).contains(sr.customer_phone),"参数全部输入的查询的"+sr.customer_phone+"与列表信息的第一行的"+result.getString(ss[6][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(ss[7][1].toString()).contains(sr.reception_type),"参数全部输入的查询的"+sr.reception_type+"与列表信息的第一行"+result.getString(ss[7][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(ss[8][1].toString()).contains(sr.shop_id),"参数全部输入的查询的"+sr.shop_id+"与列表信息的第一行的"+result.getString(ss[8][1].toString()+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("接待管理列表查询全填，结果校验");
        }
    }


    /**
     * @description :接待管路-筛选栏参数多项查询
     * @date :2020/11/28
     **/
    @Test()
    public void selectAppointmentRecodeSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] ss = Constant.receptionManageFilter_pram();
            SelectReception sr=new SelectReception();
            JSONArray res=jc.receptionManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            sr.plate_number=data.getString(ss[0][1].toString());
            sr.reception_sale_id=data.getString(ss[1][1].toString());
            sr.reception_date=data.getString(ss[2][1].toString());
            sr.customer_name=data.getString(ss[3][1].toString());
            sr.reception_status =data.getString(ss[4][1].toString());
//            sr.finish_date=data.getString(ss[5][1].toString());
//            sr.customer_phone=data.getString(ss[6][1].toString());
//            sr.reception_type =data.getString(ss[7][1].toString());
//            sr.shop_id=data.getString(ss[8][1].toString());

            //全部筛选之后的结果
            JSONArray result=jc.receptionManageC(sr).getJSONArray("list");
            for(int i=0;i<result.size();i++){

                Preconditions.checkArgument(result.getJSONObject(i).getString(ss[0][1].toString()).contains(sr.plate_number),"参数全部输入的查询的"+sr.plate_number+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(ss[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(ss[1][1].toString()).contains(sr.reception_sale_id ),"参数全部输入的查询的"+sr.reception_sale_id+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(ss[1][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(ss[2][1].toString()).contains(sr.reception_date),"参数全部输入的查询的"+sr.reception_date+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(ss[2][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(ss[3][1])).contains(sr.customer_name),"参数全部输入的查询的"+sr.customer_name+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(ss[3][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(ss[0][1])).contains(sr.finish_date),"参数全部输入的查询的"+sr.finish_date+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(String.valueOf(ss[0][1])+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(ss[4][1].toString()).contains(sr.reception_status),"参数全部输入的查询的"+sr.reception_status+"与列表信息的第一行的"+result.getJSONObject(i).getString(ss[4][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(ss[6][1])).contains(sr.customer_phone),"参数全部输入的查询的"+sr.customer_phone+"与列表信息的第一行的"+result.getJSONObject(i).getString(ss[6][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(ss[7][1].toString()).contains(sr.reception_type),"参数全部输入的查询的"+sr.reception_type+"与列表信息的第一行"+result.getJSONObject(i).getString(ss[7][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(ss[8][1].toString()).contains(sr.shop_id),"参数全部输入的查询的"+sr.shop_id+"与列表信息的第一行的"+result.getJSONObject(i).getString(ss[8][1].toString()+"不一致"));

            }

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("接待管理列表多项查询，结果校验");
        }
    }

    /**
     * @description :接待管路-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void selectAppointmentRecodeEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.receptionManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("接待管理列表查询参数不填写，结果校验");
        }
    }

//    @Test
//    public void Jc_erCode() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONObject data = jc.apperCOde();
//            String jsonpath = "$.er_code_url1";
//            jpu.spiltString(data.toJSONString(), jsonpath);
//
//        } catch (AssertionError | Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("轿辰-app个人中心，小程序码返回结果不为空");
//        }
//    }


    /**
     * @description :销售客户查询-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_PreSleCustomerManageFilter",dataProviderClass = Constant.class)
    public void preSleCustomerManageOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
     try{
         JSONObject respon=jc.preSleCustomerManage(shopId,"1","10","","");
         int pages=respon.getInteger("pages");
         String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
         for(int page=1;page<=pages;page++){
             JSONArray list=jc.preSleCustomerManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
             for(int i=0;i<10;i++){
                 String Flag=list.getJSONObject(i).getString(output);
                 Preconditions.checkArgument(Flag.contains(result), "销售管理按"+result+"查询，结果错误"+Flag);
             }
         }
     }catch(AssertionError | Exception e){
         appendFailReason(e.toString());
     }finally{
         saveData("销售客户查询单项查询，结果校验");
     }
    }

    /**
     * @description :销售客户查询-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void preSleCustomerManageALLFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.preSleCustomerManage_pram();
            PreSleCustomerVariable variable=new PreSleCustomerVariable();
            JSONArray res=jc.preSleCustomerManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.customer_name=data.getString(flag[0][1].toString());
            variable.customer_phone=data.getString(flag[1][1].toString());
            variable.create_date=data.getString(flag[2][1].toString());
            variable.sale_name=data.getString(flag[3][1].toString());
            variable.customer_type=data.getString(flag[4][1].toString());
            //全部筛选之后的结果
            JSONObject result=jc.preSleCustomerManage(variable).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(flag[0][1].toString()).contains(variable.customer_name),"参数全部输入的查询的"+variable.customer_name+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(flag[1][1].toString()).contains(variable.customer_phone),"参数全部输入的查询的"+variable.customer_phone+"与列表信息的第一行的"+result.getString(flag[1][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(flag[2][1].toString()).contains(variable.create_date),"参数全部输入的查询的"+variable.create_date+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(flag[3][1].toString()).contains(variable.sale_name),"参数全部输入的查询的"+variable.sale_name+"与列表信息的第一行的"+result.getString(flag[3][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(flag[4][1].toString()).contains(variable.customer_type),"参数全部输入的查询的"+variable.customer_type+"与列表信息的第一行的"+result.getString(flag[4][1].toString()+"不一致"));



        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("销售客户查询填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :销售客户查询-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void preSleCustomerManageSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.preSleCustomerManage_pram();
            PreSleCustomerVariable variable=new PreSleCustomerVariable();
            JSONArray res=jc.preSleCustomerManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.customer_name=data.getString(flag[0][1].toString());
            variable.customer_phone=data.getString(flag[1][1].toString());
            variable.create_date=data.getString(flag[2][1].toString());
//            variable.sale_name=data.getString(flag[3][1].toString());
//            variable.customer_type=data.getString(flag[4][1].toString());
            //全部筛选之后的结果

            JSONArray result=jc.preSleCustomerManage(variable).getJSONArray("list");
            for(int i=0;i<result.size();i++){
                Preconditions.checkArgument(result.getJSONObject(i).getString(flag[0][1].toString()).contains(variable.customer_name),"参数全部输入的查询的"+variable.customer_name+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(flag[1][1].toString()).contains(variable.customer_phone),"参数全部输入的查询的"+variable.customer_phone+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[1][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(flag[2][1].toString()).contains(variable.create_date),"参数全部输入的查询的"+variable.create_date+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[2][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(flag[3][1].toString()).contains(variable.sale_name),"参数全部输入的查询的"+variable.sale_name+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[3][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(flag[4][1].toString()).contains(variable.customer_type),"参数全部输入的查询的"+variable.customer_type+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[4][1].toString()+"不一致"));

            }

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("销售客户查询填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :销售客户查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void preSleCustomerManageEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
           jc.preSleCustomerManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("销售客户列表参数不填写，结果校验");
        }
    }


    /**
     * @description :售后客户查询-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_AfterSleCustomerManageFilter",dataProviderClass = Constant.class)
    public void selectAfterSleCustomerManageOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.afterSleCustomerManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.afterSleCustomerManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "售后客户管理按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("售后客户客户查询单项搜索，结果校验");
        }
    }

    /**
     * @description :售后客户查询-筛选栏填写全部参数查询
     * @date :2020/11/25
     **/
    @Test()
    public void selectAfterSleCustomerManageAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.afterSleCustomerManage_pram();
            AfterSleCustomerVariable variable=new AfterSleCustomerVariable();
            JSONArray res=jc.afterSleCustomerManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.vehicle_chassis_code=data.getString(flag[0][1].toString());
            variable.create_date=data.getString(flag[1][1].toString());
            variable.customer_name=data.getString(flag[2][1].toString());
            variable.customer_phone=data.getString(flag[3][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.afterSleCustomerManage(variable).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.vehicle_chassis_code),"参数全部输入的查询的"+variable.vehicle_chassis_code+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(flag[1][1].toString()).contains(variable.create_date),"参数全部输入的查询的"+variable.create_date+"与列表信息的第一行的"+result.getString(flag[1][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(flag[2][1].toString()).contains(variable.customer_name),"参数全部输入的查询的"+variable.customer_name+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.customer_phone),"参数全部输入的查询的"+variable.customer_phone+"与列表信息的第一行的"+result.getString(flag[3][1].toString()+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("售后客户客户查询填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :售后客户查询-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void selectAfterSleCustomerManageFSomeilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.afterSleCustomerManage_pram();
            AfterSleCustomerVariable variable=new AfterSleCustomerVariable();
            JSONArray res=jc.afterSleCustomerManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.vehicle_chassis_code=data.getString(flag[0][1].toString());
            variable.create_date=data.getString(flag[1][1].toString());
//            variable.customer_name=data.getString(flag[2][1].toString());
//            variable.customer_phone=data.getString(flag[3][1].toString());

            //全部筛选之后的结果
            JSONArray result=jc.afterSleCustomerManage(variable).getJSONArray("list");
            for(int i=0;i<result.size();i++){
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.vehicle_chassis_code),"参数全部输入的查询的"+variable.vehicle_chassis_code+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(flag[1][1].toString()).contains(variable.create_date),"参数全部输入的查询的"+variable.create_date+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[1][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(flag[2][1].toString()).contains(variable.customer_name),"参数全部输入的查询的"+variable.customer_name+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[2][1].toString()+"不一致"));
//                Preconditions.checkArgument(.getJSONObject(i).getString(String.valueOf(flag[3][1])).contains(variable.customer_phone),"参数全部输入的查询的"+variable.customer_phone+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[3][1].toString()+"不一致"));

            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("售后客户客户查询填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :售后客户查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void selectAfterSleCustomerManageEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.afterSleCustomerManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("售后客户列表参数不填写，结果校验");
        }
    }

    /**
     * @description :小程序客户查询-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_weChatSleCustomerManageFilter",dataProviderClass = Constant.class)
    public void weChatSleCustomerManageOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.weChatSleCustomerManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.weChatSleCustomerManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "小程序客户管理按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("客户小程序客户单项查询，结果校验");
        }
    }

    /**
     * @description :小程序客户查询-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void weChatSleCustomerManageAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.weChatSleCustomerManage_pram();
            weChatSleCustomerVariable variable=new weChatSleCustomerVariable();
            JSONArray res=jc.afterSleCustomerManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.create_date=data.getString(flag[0][1].toString());
            variable.active_type=data.getString(flag[1][1].toString());
            variable.customer_phone=data.getString(flag[2][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.weChatSleCustomerManage(variable).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.create_date),"参数全部输入的查询的"+variable.create_date+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(flag[1][1].toString()).contains(variable.active_type),"参数全部输入的查询的"+variable.active_type+"与列表信息的第一行的"+result.getString(flag[1][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(flag[2][1].toString()).contains(variable.customer_phone),"参数全部输入的查询的"+variable.customer_phone+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("客户小程序客户填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :小程序客户查询-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void weChatSleCustomerManageSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.weChatSleCustomerManage_pram();
            weChatSleCustomerVariable variable=new weChatSleCustomerVariable();
            JSONArray res=jc.afterSleCustomerManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.create_date=data.getString(flag[0][1].toString());
            variable.active_type=data.getString(flag[1][1].toString());
//            variable.customer_phone=data.getString(flag[2][1].toString());

            //全部筛选之后的结果
            JSONArray result=jc.weChatSleCustomerManage(variable).getJSONArray("list");
            for(int i=0;i<result.size();i++){
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.create_date),"参数全部输入的查询的"+variable.create_date+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(flag[1][1].toString()).contains(variable.active_type),"参数全部输入的查询的"+variable.active_type+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[1][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(flag[2][1].toString()).contains(variable.customer_phone),"参数全部输入的查询的"+variable.customer_phone+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[2][1].toString()+"不一致"));

            }

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("客户小程序客户填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :客户小程序查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void weChatSleCustomerManageEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
           jc.afterSleCustomerManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("客户小程序列表参数不填写，结果校验");
        }
    }

    /**
     * @description :预约记录-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_appointmentRecordFilter",dataProviderClass = Constant.class)
    public void appointmentRecordOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.appointmentRecordManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.appointmentRecordManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "预约记录管理按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("预约记录单项查询，结果校验");
        }
    }
    /**
     * @description :预约记录-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void appointmentRecordAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.appointmentRecordFilter_pram();
            appointmentRecordVariable variable=new appointmentRecordVariable();
            JSONArray res=jc.appointmentRecordManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.plate_number=data.getString(flag[0][1].toString());
            variable.customer_manager=data.getString(flag[1][1].toString());
            variable.shop_id=data.getString(flag[2][1].toString());
            variable.customer_name=data.getString(flag[3][1].toString());
            variable.appointment_status=data.getString(flag[4][1].toString());
            variable.customer_phone=data.getString(flag[5][1].toString());
            variable.is_overtime=data.getString(flag[6][1].toString());
            variable.confirm_time=data.getString(flag[7][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.appointmentRecordManage(variable).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.plate_number),"参数全部输入的查询的"+variable.plate_number+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(flag[1][1].toString()).contains(variable.customer_manager),"参数全部输入的查询的"+variable.customer_manager+"与列表信息的第一行的"+result.getString(flag[1][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(flag[2][1].toString()).contains(variable.shop_id),"参数全部输入的查询的"+variable.shop_id+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(flag[3][1].toString()).contains(variable.customer_name),"参数全部输入的查询的"+variable.customer_name+"与列表信息的第一行的"+result.getString(flag[3][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(flag[4][1].toString()).contains(variable.appointment_status),"参数全部输入的查询的"+variable.appointment_status+"与列表信息的第一行的"+result.getString(flag[4][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(flag[5][1].toString()).contains(variable.customer_phone),"参数全部输入的查询的"+variable.customer_phone+"与列表信息的第一行的"+result.getString(flag[5][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(flag[6][1].toString()).contains(variable.is_overtime),"参数全部输入的查询的"+variable.is_overtime+"与列表信息的第一行的"+result.getString(flag[6][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(flag[7][1].toString()).contains(variable.confirm_time),"参数全部输入的查询的"+variable.confirm_time+"与列表信息的第一行的"+result.getString(flag[7][1].toString()+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("预约记录填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :预约记录-筛选栏填写参数多项查询
     * @date :2020/11/28
     **/
    @Test()
    public void appointmentRecordSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.appointmentRecordFilter_pram();
            appointmentRecordVariable variable=new appointmentRecordVariable();
            JSONArray res=jc.appointmentRecordManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.plate_number=data.getString(flag[0][1].toString());
            variable.customer_manager=data.getString(flag[1][1].toString());
            variable.shop_id=data.getString(flag[2][1].toString());
            variable.customer_name=data.getString(flag[3][1].toString());
            variable.appointment_status=data.getString(flag[4][1].toString());
//            variable.customer_phone=data.getString(flag[5][1].toString());
//            variable.is_overtime=data.getString(flag[6][1].toString());
//            variable.confirm_time=data.getString(flag[7][1].toString());

            //全部筛选之后的结果
            JSONArray result=jc.appointmentRecordManage(variable).getJSONArray("list");
            for(int i=0;i<result.size();i++){
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.plate_number),"参数全部输入的查询的"+variable.plate_number+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(flag[1][1].toString()).contains(variable.customer_manager),"参数全部输入的查询的"+variable.customer_manager+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[1][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(flag[2][1].toString()).contains(variable.shop_id),"参数全部输入的查询的"+variable.shop_id+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[2][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(flag[3][1].toString()).contains(variable.customer_name),"参数全部输入的查询的"+variable.customer_name+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[3][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(flag[4][1].toString()).contains(variable.appointment_status),"参数全部输入的查询的"+variable.appointment_status+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[4][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(flag[5][1].toString()).contains(variable.customer_phone),"参数全部输入的查询的"+variable.customer_phone+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[5][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(flag[6][1].toString()).contains(variable.is_overtime),"参数全部输入的查询的"+variable.is_overtime+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[6][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(flag[7][1].toString()).contains(variable.confirm_time),"参数全部输入的查询的"+variable.confirm_time+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[7][1].toString()+"不一致"));

            }

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("预约记录填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :预约记录查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void appointmentRecordEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.appointmentRecordManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("预约记录列表参数不填写，结果校验");
        }
    }

    /**
     * @description :保养配置-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_maintainFilter",dataProviderClass = Constant.class)
    public void maintainOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.maintainFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.maintainFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "保养配置按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("保养配置单项查询，结果校验");
        }
    }

    /**
     * @description :保养配置-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void maintainALLFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.maintainFilter_pram();
            maintainVariable variable=new maintainVariable();
            JSONArray res=jc.maintainFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.brand_name=data.getString(flag[0][1].toString());
            variable.manufacturer=data.getString(flag[1][1].toString());
            variable.car_model=data.getString(flag[2][1].toString());
            variable.year=data.getString(flag[3][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.maintainFilterManage(variable).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.brand_name),"参数全部输入的查询的"+variable.brand_name+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.manufacturer),"参数全部输入的查询的"+variable.manufacturer+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.car_model),"参数全部输入的查询的"+variable.car_model+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.year),"参数全部输入的查询的"+variable.year+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("保养配置填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :保养配置-筛选栏填写参数多项查询
     * @date :2020/11/28
     **/
    @Test()
    public void maintainSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.maintainFilter_pram();
            maintainVariable variable=new maintainVariable();
            JSONArray res=jc.maintainFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.brand_name=data.getString(flag[0][1].toString());
            variable.manufacturer=data.getString(flag[1][1].toString());
            variable.car_model=data.getString(flag[2][1].toString());
//            variable.year=data.getString(flag[3][1].toString());

            //全部筛选之后的结果
            JSONArray result=jc.maintainFilterManage(variable).getJSONArray("list");
            for(int i=0;i<result.size();i++){
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.brand_name),"参数全部输入的查询的"+variable.brand_name+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(variable.manufacturer),"参数全部输入的查询的"+variable.manufacturer+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[2][1])).contains(variable.car_model),"参数全部输入的查询的"+variable.car_model+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[3][1])).contains(variable.year),"参数全部输入的查询的"+variable.year+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));

            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("保养配置填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :保养配置查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void maintainEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.maintainFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("保养配置列表参数不填写，结果校验");
        }
    }

    /**
     * @description :卡券管理-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_voucherFormFilter",dataProviderClass = Constant.class)
    public void voucherFormOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.voucherFormFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.voucherFormFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "卡券管理管理按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("卡券管理单项查询，结果校验");
        }
    }

    /**
     * @description :卡券管理-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void voucherFormAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.voucherFormFilter_pram();
            voucherFormVariable variable=new voucherFormVariable();
            JSONArray res=jc.voucherFormFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.subject_name=data.getString(flag[0][1].toString());
            variable.voucher_name=data.getString(flag[1][1].toString());
            variable.creator=data.getString(flag[2][1].toString());
            variable.is_diff=data.getString(flag[3][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.voucherFormFilterManage(variable).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.subject_name),"参数全部输入的查询的"+variable.subject_name+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.voucher_name),"参数全部输入的查询的"+variable.voucher_name+"与列表信息的第一行的"+result.getString(flag[1][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.creator),"参数全部输入的查询的"+variable.creator+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.is_diff),"参数全部输入的查询的"+variable.is_diff+"与列表信息的第一行的"+result.getString(flag[3][1].toString()+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("卡券管理填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :卡券管理-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void voucherFormSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.voucherFormFilter_pram();
            voucherFormVariable variable=new voucherFormVariable();
            JSONArray res=jc.voucherFormFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.subject_name=data.getString(flag[0][1].toString());
            variable.voucher_name=data.getString(flag[1][1].toString());
//            variable.creator=data.getString(flag[2][1].toString());
//            variable.is_diff=data.getString(flag[3][1].toString());

            //全部筛选之后的结果
            JSONArray result=jc.voucherFormFilterManage(variable).getJSONArray("list");
            for(int i=0;i<result.size();i++){
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.subject_name),"参数全部输入的查询的"+variable.subject_name+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(variable.voucher_name),"参数全部输入的查询的"+variable.voucher_name+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[1][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[2][1])).contains(variable.creator),"参数全部输入的查询的"+variable.creator+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[3][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[3][1])).contains(variable.is_diff),"参数全部输入的查询的"+variable.is_diff+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[3][1].toString()+"不一致"));

            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("卡券管理填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :卡券管理查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void voucherFormEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
           jc.voucherFormFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("卡券管理列表参数不填写，结果校验");
        }
    }

    /**
     * @description :发卡记录-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_sendRecordFilter",dataProviderClass = Constant.class)
    public void sendRecordOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.sendRecordFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.sendRecordFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "发卡记录管理按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("发卡记录单项查询，结果校验");
        }
    }

    /**
     * @description :发卡记录-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void sendRecordAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.sendRecordFilter_pram();
            sendRecordVariable variable=new sendRecordVariable();
            JSONArray res=jc.sendRecordFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.voucher_name=data.getString(flag[0][1].toString());
            variable.sender=data.getString(flag[1][1].toString());
            variable.start_time=data.getString(flag[2][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.sendRecordFilterManage(variable).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(flag[0][1].toString()).contains(variable.voucher_name),"参数全部输入的查询的"+variable.voucher_name+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(flag[1][1].toString()).contains(variable.sender),"参数全部输入的查询的"+variable.sender+"与列表信息的第一行的"+result.getString(flag[1][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(flag[2][1].toString()).contains(variable.start_time),"参数全部输入的查询的"+variable.start_time+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("发卡记录填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :发卡记录-筛选栏填写多数参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void sendRecordSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.sendRecordFilter_pram();
            sendRecordVariable variable=new sendRecordVariable();
            JSONArray res=jc.sendRecordFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.voucher_name=data.getString(flag[0][1].toString());
            variable.sender=data.getString(flag[1][1].toString());
//            variable.start_time=data.getString(flag[2][1].toString());

            //全部筛选之后的结果
            JSONArray result=jc.sendRecordFilterManage(variable).getJSONArray("list");
            for(int i=0;i<result.size();i++){
                Preconditions.checkArgument(result.getJSONObject(i).getString(flag[0][1].toString()).contains(variable.voucher_name),"参数全部输入的查询的"+variable.voucher_name+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(flag[1][1].toString()).contains(variable.sender),"参数全部输入的查询的"+variable.sender+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[1][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getString(flag[2][1].toString()).contains(variable.start_time),"参数全部输入的查询的"+variable.start_time+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));

            }

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("发卡记录填写多数参数查询，结果校验");
        }
    }

    /**
     * @description :发卡记录查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void sendRecordFilterEmptyEmpty(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
          jc.sendRecordFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("发卡记录列表参数不填写，结果校验");
        }
    }

    /**
     * @description :核销记录-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_verificationRecordFilter",dataProviderClass = Constant.class)
    public void verificationRecordOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.verificationReordFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.verificationReordFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "核销记录管理按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("核销记录单项查询，结果校验");
        }
    }



    /**
     * @description :核销记录-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void verificationRecordAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.verificationRecordFilter_pram();
            verificationRecordVariable variable=new verificationRecordVariable();
            JSONArray res=jc.verificationReordFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.voucher_name=data.getString(flag[0][1].toString());
            variable.sender=data.getString(flag[1][1].toString());
            variable.start_time=data.getString(flag[2][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.verificationReordFilterManage(variable).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.voucher_name),"参数全部输入的查询的"+variable.voucher_name+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.sender),"参数全部输入的查询的"+variable.sender+"与列表信息的第一行的"+result.getString(flag[1][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.start_time),"参数全部输入的查询的"+variable.start_time+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("核销记录填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :核销记录-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void verificationRecordSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.verificationRecordFilter_pram();
            verificationRecordVariable variable=new verificationRecordVariable();
            JSONArray res=jc.verificationReordFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.voucher_name=data.getString(flag[0][1].toString());
            variable.sender=data.getString(flag[1][1].toString());
//            variable.start_time=data.getString(flag[2][1].toString());

            //全部筛选之后的结果
            JSONArray result=jc.verificationReordFilterManage(variable).getJSONArray("list");
            for(int i=0;i<result.size();i++){
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.voucher_name),"参数全部输入的查询的"+variable.voucher_name+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(variable.sender),"参数全部输入的查询的"+variable.sender+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[1][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.start_time),"参数全部输入的查询的"+variable.start_time+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));

            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("核销记录填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :核销记录查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void verificationRecordEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.verificationReordFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("核销记录列表参数不填写，结果校验");
        }
    }

    /**
     * @description :核销人员记录-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_verificationPeopleFilter",dataProviderClass = Constant.class)
    public void verificationPeopleOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.verificationPeopleFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.verificationPeopleFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "核销记录管理按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("核销记录单项查询，结果校验");
        }
    }

    /**
     * @description :核销人员记录-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void verificationPeopleAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.verificationPeopleFilter_pram();
            verificationPeopleVariable variable=new verificationPeopleVariable();
            JSONArray res=jc.verificationPeopleFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.verification_person=data.getString(flag[0][1].toString());
            variable.verification_phone=data.getString(flag[1][1].toString());
            variable.verification_code=data.getString(flag[2][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.verificationPeopleFilterManage(variable).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.verification_person),"参数全部输入的查询的"+variable.verification_person+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.verification_phone),"参数全部输入的查询的"+variable.verification_phone+"与列表信息的第一行的"+result.getString(flag[1][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.verification_code),"参数全部输入的查询的"+variable.verification_code+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("核销人员记录填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :核销人员记录-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void verificationPeopleSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.verificationPeopleFilter_pram();
            verificationPeopleVariable variable=new verificationPeopleVariable();
            JSONArray res=jc.verificationPeopleFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.verification_person=data.getString(flag[0][1].toString());
            variable.verification_phone=data.getString(flag[1][1].toString());
//            variable.verification_code=data.getString(flag[2][1].toString());

            //全部筛选之后的结果
            JSONArray result=jc.verificationPeopleFilterManage(variable).getJSONArray("list");
            for(int i=0;i<result.size();i++){

                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.verification_person),"参数全部输入的查询的"+variable.verification_person+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(variable.verification_phone),"参数全部输入的查询的"+variable.verification_phone+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[1][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[2][1])).contains(variable.verification_code),"参数全部输入的查询的"+variable.verification_code+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[2][1].toString()+"不一致"));

            }

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("核销人员记录填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :核销人员记录查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void verificationPeopleEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.verificationPeopleFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("核销人员记录参数不填写，结果校验");
        }
    }

    /**
     * @description :套餐表单-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_packageFormFilter",dataProviderClass = Constant.class)
    public void packageFormOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.packageFormFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.packageFormFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "套餐表单管理按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("套餐表单单项查询，结果校验");
        }
    }

    /**
     * @description :套餐表单-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void packageFormAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.packageFormFilter_pram();
            packageFormVariable variable=new packageFormVariable();
            JSONArray res=jc.packageFormFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.package_name=data.getString(flag[0][1].toString());
            variable.creator=data.getString(flag[1][1].toString());
            variable.start_time=data.getString(flag[2][1].toString());
            variable.shop_name=data.getString(flag[3][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.packageFormFilterManage(variable).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.package_name),"参数全部输入的查询的"+variable.package_name+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.creator),"参数全部输入的查询的"+variable.creator+"与列表信息的第一行的"+result.getString(flag[1][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.start_time),"参数全部输入的查询的"+variable.start_time+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.shop_name),"参数全部输入的查询的"+variable.shop_name+"与列表信息的第一行的"+result.getString(flag[3][1].toString()+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("套餐表单填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :套餐表单-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void packageFormSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.packageFormFilter_pram();
            packageFormVariable variable=new packageFormVariable();
            JSONArray res=jc.packageFormFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.package_name=data.getString(flag[0][1].toString());
            variable.creator=data.getString(flag[1][1].toString());
            variable.start_time=data.getString(flag[2][1].toString());
//            variable.shop_name=data.getString(flag[3][1].toString());

            //全部筛选之后的结果
            JSONArray result=jc.packageFormFilterManage(variable).getJSONArray("list");
            for(int i=0;i<result.size();i++){
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.package_name),"参数全部输入的查询的"+variable.package_name+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(variable.creator),"参数全部输入的查询的"+variable.creator+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[1][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[2][1])).contains(variable.start_time),"参数全部输入的查询的"+variable.start_time+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[2][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[3][1])).contains(variable.shop_name),"参数全部输入的查询的"+variable.shop_name+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));

            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("套餐表单填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :套餐表单查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void packageFormEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.packageFormFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("套餐表单参数不填写，结果校验");
        }
    }

    /**
     * @description :套餐购买记录-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_buyPackageRecordFilter",dataProviderClass = Constant.class)
    public void buyPackageRecordOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.buyPackageRecordFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.buyPackageRecordFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "套餐购买管理按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("套餐购买记录单项查询，结果校验");
        }
    }

    /**
     * @description :套餐购买记录-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void buyPackageRecordAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.buyPackageRecordFilter_pram();
            buyPackageVariable variable=new buyPackageVariable();
            JSONArray res=jc.buyPackageRecordFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.package_name=data.getString(flag[0][1].toString());
            variable.start_time=data.getString(flag[1][1].toString());
            variable.send_type=data.getString(flag[2][1].toString());
            //全部筛选之后的结果
            JSONObject result=jc.buyPackageRecordFilterManage(variable).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.package_name),"参数全部输入的查询的"+variable.package_name+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.start_time),"参数全部输入的查询的"+variable.start_time+"与列表信息的第一行的"+result.getString(flag[1][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.send_type),"参数全部输入的查询的"+variable.send_type+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("套餐购买记录填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :套餐购买记录-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void buyPackageRecordSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.buyPackageRecordFilter_pram();
            buyPackageVariable variable=new buyPackageVariable();
            JSONArray res=jc.buyPackageRecordFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.package_name=data.getString(flag[0][1].toString());
            variable.start_time=data.getString(flag[1][1].toString());
//            variable.send_type=data.getString(flag[2][1].toString());

            //全部筛选之后的结果
            JSONArray result=jc.buyPackageRecordFilterManage(variable).getJSONArray("list");
            for(int i=0;i<result.size();i++){
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.package_name),"参数全部输入的查询的"+variable.package_name+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(variable.start_time),"参数全部输入的查询的"+variable.start_time+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[1][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[2][1])).contains(variable.send_type),"参数全部输入的查询的"+variable.send_type+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));

            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("套餐购买记录填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :套餐购买记录查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void buyPackageRecordEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.buyPackageRecordFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("套餐购买记录参数不填写，结果校验");
        }
    }


            /**
             * @description :消息表单-筛选栏单项查询
             * @date :2020/11/24
             **/
    @Test(dataProvider = "SELECT_messageFormFilter",dataProviderClass = Constant.class)
    public void messageFormOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.messageFormFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.messageFormFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "消息表单管理按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("消息表单单项查询，结果校验");
        }
    }

    /**
     * @description :消息表单-筛选栏填写全部参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void messageFormAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.buyPackageRecordFilter_pram();
            messageFormVariable variable=new messageFormVariable();
            JSONArray res=jc.messageFormFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.shop_id=data.getString(flag[0][1].toString());
            variable.start_time=data.getString(flag[1][1].toString());
            variable.customer_name=data.getString(flag[2][1].toString());
            variable.message_type=data.getString(flag[3][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.messageFormFilterManage(variable).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.shop_id),"参数全部输入的查询的"+variable.shop_id+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.start_time),"参数全部输入的查询的"+variable.start_time+"与列表信息的第一行的"+result.getString(flag[1][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.customer_name),"参数全部输入的查询的"+variable.customer_name+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.message_type),"参数全部输入的查询的"+variable.message_type+"与列表信息的第一行的"+result.getString(flag[3][1].toString()+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("消息表单填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :消息表单-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void messageFormSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.buyPackageRecordFilter_pram();
            messageFormVariable variable=new messageFormVariable();
            JSONArray res=jc.messageFormFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.shop_id=data.getString(flag[0][1].toString());
            variable.start_time=data.getString(flag[1][1].toString());
//            variable.customer_name=data.getString(flag[2][1].toString());
//            variable.message_type=data.getString(flag[3][1].toString());

            //全部筛选之后的结果
            JSONArray result=jc.messageFormFilterManage(variable).getJSONArray("list");
            for(int i=0;i<result.size();i++){
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.shop_id),"参数全部输入的查询的"+variable.shop_id+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(variable.start_time),"参数全部输入的查询的"+variable.start_time+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[1][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[2][1])).contains(variable.customer_name),"参数全部输入的查询的"+variable.customer_name+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[2][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[3][1])).contains(variable.message_type),"参数全部输入的查询的"+variable.message_type+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[3][1].toString()+"不一致"));

            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("消息表单填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :消息表单查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void messageFormEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.messageFormFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("消息表单参数不填写，结果校验");
        }
    }

    /**
     * @description :内容管理-文章表单-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_articleFilter",dataProviderClass = Constant.class)
    public void articleOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.articleFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.articleFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "文章表单按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("文章表单单项查询，结果校验");
        }
    }

    /**
     * @description :内容管理-文章表单-筛选栏填写全部参数查询-----活动开始时间结束时间，报名开始时间结束时间对不上
     * @date :2020/11/28
     **/
    @Test()
    public void articleAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.articleFilter_pram();
            articleVariable variable=new articleVariable();
            JSONArray res=jc.articleFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.title=data.getString(flag[0][1].toString());


            //全部筛选之后的结果
            JSONObject result=jc.articleFilterManage(variable).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.title),"参数全部输入的查询的"+variable.title+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("文章表单填写全部参数查询，结果校验");
        }
    }


    /**
     * @description :文章表单查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void articleFilterEmpty(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.articleFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("文章表单参数不填写，结果校验");
        }
    }

    /**
     * @description :报名列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_registerListFilter",dataProviderClass = Constant.class)
    public void registerListOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.registerListFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.registerListFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "报名列表按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("报名列表单项查询，结果校验");
        }
    }

    /**
     * @description :报名列表-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void registerListAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.registerListFilter_pram();
            registerListVariable variable=new registerListVariable();
            JSONArray res=jc.registerListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.title=data.getString(flag[0][1].toString());
            variable.start_date=data.getString(flag[1][1].toString());
            variable.end_date=data.getString(flag[2][1].toString());
            variable.register_start_date=data.getString(flag[3][1].toString());
            variable.register_end_date=data.getString(flag[4][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.registerListFilterManage(variable).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.title),"参数全部输入的查询的"+variable.title+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.start_date),"参数全部输入的查询的"+variable.start_date+"与列表信息的第一行的"+result.getString(flag[1][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.end_date),"参数全部输入的查询的"+variable.end_date+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.register_start_date),"参数全部输入的查询的"+variable.register_start_date+"与列表信息的第一行的"+result.getString(flag[3][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[4][1])).contains(variable.register_end_date),"参数全部输入的查询的"+variable.register_end_date+"与列表信息的第一行的"+result.getString(flag[4][1].toString()+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("报名列表填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :报名列表-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void registerListSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.registerListFilter_pram();
            registerListVariable variable=new registerListVariable();
            JSONArray res=jc.registerListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.title=data.getString(flag[0][1].toString());
            variable.start_date=data.getString(flag[1][1].toString());
            variable.end_date=data.getString(flag[2][1].toString());
//            variable.register_start_date=data.getString(flag[3][1].toString());
//            variable.register_end_date=data.getString(flag[4][1].toString());

            //全部筛选之后的结果
            JSONArray result=jc.registerListFilterManage(variable).getJSONArray("list");
            for(int i=0;i<result.size();i++){
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.title),"参数全部输入的查询的"+variable.title+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(variable.start_date),"参数全部输入的查询的"+variable.start_date+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[1][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[2][1])).contains(variable.end_date),"参数全部输入的查询的"+variable.end_date+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[2][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[3][1])).contains(variable.register_start_date),"参数全部输入的查询的"+variable.register_start_date+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[3][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[4][1])).contains(variable.register_end_date),"参数全部输入的查询的"+variable.register_end_date+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[4][1].toString()+"不一致"));

            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("报名列表填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :报名列表查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void registerListFilterSomeEmpty(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
           jc.registerListFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("报名列表参数不填写，结果校验");
        }
    }

    /**
     * @description :报名审批列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_registerListFilter",dataProviderClass = Constant.class)
    public void approvalListOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.approvalListFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.approvalListFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "报名审批按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("报名审批单项查询，结果校验");
        }
    }

    /**
     * @description :报名审批列表-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void approvalListAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.approvalListFilter_pram();
            approvalListVariable variable=new approvalListVariable();
            JSONArray res=jc.approvalListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.customer_name=data.getString(flag[0][1].toString());
            variable.phone=data.getString(flag[1][1].toString());
            variable.status=data.getString(flag[2][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.approvalListFilterManage(variable).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.customer_name),"参数全部输入的查询的"+variable.customer_name+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.phone),"参数全部输入的查询的"+variable.phone+"与列表信息的第一行的"+result.getString(flag[1][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.status),"参数全部输入的查询的"+variable.status+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("报名审批填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :报名审批列表-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void approvalListSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.approvalListFilter_pram();
            approvalListVariable variable=new approvalListVariable();
            JSONArray res=jc.approvalListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.customer_name=data.getString(flag[0][1].toString());
            variable.phone=data.getString(flag[1][1].toString());
//            variable.status=data.getString(flag[2][1].toString());

            //全部筛选之后的结果
            JSONArray result=jc.approvalListFilterManage(variable).getJSONArray("list");
            for(int i=0;i<result.size();i++){
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.customer_name),"参数全部输入的查询的"+variable.customer_name+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(variable.phone),"参数全部输入的查询的"+variable.phone+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[1][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[2][1])).contains(variable.status),"参数全部输入的查询的"+variable.status+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[2][1].toString()+"不一致"));

            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("报名审批填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :报名审批查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void approvalListFilterEmpty(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.approvalListFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("报名审批参数不填写，结果校验");
        }
    }

    /**
     * @description :卡券申请-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_applyListFilter",dataProviderClass = Constant.class)
    public void applyListOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.applyListFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.applyListFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "卡券申请按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("卡券申请单项查询，结果校验");
        }
    }

    /**
     * @description :卡券申请-筛选栏填写全部参数查询
     * @date :2020/11/27
     **/
    @Test()
    public void applyListAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.applyListFilter_pram();
            applyListVariable variable=new applyListVariable();
            JSONArray res=jc.applyListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.name=data.getString(flag[0][1].toString());
            variable.apply_name=data.getString(flag[1][1].toString());
            variable.status=data.getString(flag[2][1].toString());
            variable.apply_time=data.getString(flag[3][1].toString());
            variable.apply_group=data.getString(flag[4][1].toString());
            variable.apply_item=data.getString(flag[5][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.applyListFilterManage(variable).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.name),"参数全部输入的查询的"+variable.name+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.apply_name),"参数全部输入的查询的"+variable.apply_name+"与列表信息的第一行的"+result.getString(flag[1][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.status),"参数全部输入的查询的"+variable.status+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.apply_time),"参数全部输入的查询的"+variable.apply_time+"与列表信息的第一行的"+result.getString(flag[3][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[4][1])).contains(variable.apply_group),"参数全部输入的查询的"+variable.apply_group+"与列表信息的第一行的"+result.getString(flag[4][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[5][1])).contains(variable.apply_item),"参数全部输入的查询的"+variable.apply_item+"与列表信息的第一行的"+result.getString(flag[5][1].toString()+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("卡券申请填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :卡券申请-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void applyListSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.applyListFilter_pram();
            applyListVariable variable=new applyListVariable();
            JSONArray res=jc.applyListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.name=data.getString(flag[0][1].toString());
            variable.apply_name=data.getString(flag[1][1].toString());
            variable.status=data.getString(flag[2][1].toString());
            variable.apply_time=data.getString(flag[3][1].toString());
//            variable.apply_group=data.getString(flag[4][1].toString());
//            variable.apply_item=data.getString(flag[5][1].toString());

            //全部筛选之后的结果
            JSONArray result=jc.applyListFilterManage(variable).getJSONArray("list");
            for(int i=0;i<result.size();i++){
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.name),"参数全部输入的查询的"+variable.name+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(variable.apply_name),"参数全部输入的查询的"+variable.apply_name+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[1][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[2][1])).contains(variable.status),"参数全部输入的查询的"+variable.status+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[2][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[3][1])).contains(variable.apply_time),"参数全部输入的查询的"+variable.apply_time+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[3][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[4][1])).contains(variable.apply_group),"参数全部输入的查询的"+variable.apply_group+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[4][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[5][1])).contains(variable.apply_item),"参数全部输入的查询的"+variable.apply_item+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[5][1].toString()+"不一致"));

            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("卡券申请填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :卡券申请查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void voidapplyListEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.applyListFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("卡券申请参数不填写，结果校验");
        }
    }

    /**
     * @description :门店列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_shopListFilter",dataProviderClass = Constant.class)
    public void shopListOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.shopListFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.shopListFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "门店列表按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("门店列表单项查询，结果校验");
        }
    }

    /**
     * @description :门店列表-筛选栏填写全部参数查询
     * @date :2020/11/27
     **/
    @Test()
    public void shopListAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.shopListFilter_pram();
            JSONArray res=jc.shopListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            String name=data.getString(flag[0][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.shopListFilterManage(shopId,"1","10",name).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(name),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+name+"不一致"));
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("门店列表填写全部参数查询，结果校验");
        }
    }


    /**
     * @description :门店列表查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void shopListEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.shopListFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("门店列表参数不填写，结果校验");
        }
    }

    /**
     * @description :品牌列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_brandListFilter",dataProviderClass = Constant.class)
    public void brandListOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.brandListFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.brandListFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "品牌列表按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("品牌列表单项查询，结果校验");
        }
    }

    /**
     * @description :品牌列表-筛选栏填写全部参数查询
     * @date :2020/11/27
     **/
    @Test()
    public void brandListAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.brandListFilter_pram();
            JSONArray res=jc.brandListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            String name=data.getString(flag[0][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.brandListFilterManage(shopId,"1","10",name).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(name.contains(result.getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+name+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("品牌列表填写全部参数查询，结果校验");
        }
    }



    /**
     * @description :品牌列表查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void brandListEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.brandListFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("品牌列表参数不填写，结果校验");
        }
    }

    /**
     * @description :车系列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_brandListFilter",dataProviderClass = Constant.class)
    public void carStyleListOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.carStyleListFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.carStyleListFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "车系列表按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("车系列表单项查询，结果校验");
        }
    }

    /**
     * @description :车系列表-筛选栏填写全部参数查询
     * @date :2020/11/27
     **/
    @Test()
    public void carStyleListAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.carStyleListFilter_pram();
            JSONArray res=jc.carStyleListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            String name=data.getString(flag[0][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.carStyleListFilterManage(shopId,"1","10",name).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(name.contains(result.getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+name+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("车系列表填写全部参数查询，结果校验");
        }
    }


    /**
     * @description :车系列表查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void carStyleListEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.carStyleListFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("车系列表参数不填写，结果校验");
        }
    }

    /**
     * @description :车型列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_brandListFilter",dataProviderClass = Constant.class)
    public void carModelListOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.carModelListFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.carModelListFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "车型列表按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("车型列表单项查询，结果校验");
        }
    }

    /**
     * @description :车型列表-筛选栏填写全部参数查询
     * @date :2020/11/27
     **/
    @Test()
    public void carModelListAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.carModelListFilter_pram();
            JSONArray res=jc.carModelListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            String name=data.getString(flag[0][1].toString());
            String year=data.getString(flag[1][1].toString());
            String status=data.getString(flag[2][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.carModelListFilterManage(shopId,"1","10",name,year,status).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(name),"参数全部输入的查询的"+name+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(year),"参数全部输入的查询的"+year+"与列表信息的第一行的"+result.getString(flag[1][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(status),"参数全部输入的查询的"+status+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));


        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("车型列表填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :车型列表-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void carModelListSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.carModelListFilter_pram();
            JSONArray res=jc.carModelListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            String name=data.getString(flag[0][1].toString());
            String year=data.getString(flag[1][1].toString());
//            String status=data.getString(flag[2][1].toString());

            //全部筛选之后的结果
            JSONArray result=jc.carModelListFilterManage(shopId,"1","10",name,year).getJSONArray("list");
            for(int i=0;i<result.size();i++){
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(name),"参数全部输入的查询的"+name+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(year),"参数全部输入的查询的"+year+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[1][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[2][1])).contains(status),"参数全部输入的查询的"+status+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));

            }

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("车型列表填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :车型列表查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void carModelListEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.carModelListFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("车型列表参数不填写，结果校验");
        }
    }

    /**
     * @description :角色列表-筛选栏单项查询
     * @date :2020/11/27
     **/
    @Test(dataProvider = "SELECT_roleListFilter",dataProviderClass = Constant.class)
    public void roleListOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.roleListFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.roleListFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "角色列表按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("角色列表单项查询，结果校验");
        }
    }

    /**
     * @description :角色列表-筛选栏填写全部参数查询
     * @date :2020/11/27
     **/
    @Test()
    public void roleListAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.roleListFilter_pram();
            JSONArray res=jc.roleListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            String name=data.getString(flag[0][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.roleListFilterManage(shopId,"1","10",name).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(name.contains(result.getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+name+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("角色列表填写全部参数查询，结果校验");
        }
    }


    /**
     * @description :角色列表查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void roleListEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
           jc.roleListFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("角色列表参数不填写，结果校验");
        }
    }

    /**
     * @description :员工列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_roleListFilter",dataProviderClass = Constant.class)
    public void staffListOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.staffListFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.staffListFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "员工列表按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("员工列表单项查询，结果校验");
        }
    }

    /**
     * @description :员工列表-筛选栏填写全部参数查询
     * @date :2020/11/27
     **/
    @Test()
    public void staffListAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.satffListFilter_pram();
            JSONArray res=jc.staffListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            String role_name=data.getString(flag[0][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.staffListFilterManage(shopId,"1","10",role_name).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(role_name.contains(result.getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+role_name+"不一致"));


        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("员工列表填写全部参数查询，结果校验");
        }
    }


    /**
     * @description :员工列表查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void staffListEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.staffListFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("员工列表参数不填写，结果校验");
        }
    }

    /**
     * @description :导入记录列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_importListFilter",dataProviderClass = Constant.class)
    public void importListOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.importListFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.importListFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "导入记录按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("导入记录列表单项查询，结果校验");
        }
    }

    /**
     * @description :导入记录列表-筛选栏填写全部参数查询
     * @date :2020/11/27
     **/
    @Test()
    public void importListAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.importListFilter_pram();
            JSONArray res=jc.importListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            String type=data.getString(flag[0][1].toString());
            String user=data.getString(flag[1][1].toString());
            String import_date=data.getString(flag[2][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.importListFilterManage(shopId,"1","10",type,user,import_date).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(type),"参数全部输入的查询的"+type+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(user),"参数全部输入的查询的"+user+"与列表信息的第一行的"+result.getString(flag[1][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(import_date),"参数全部输入的查询的"+import_date+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));


        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("导入记录列表填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :导入记录列表-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void importListSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.importListFilter_pram();
            JSONArray res=jc.importListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            String type=data.getString(flag[0][1].toString());
            String user=data.getString(flag[1][1].toString());
//            String import_date=data.getString(flag[2][1].toString());

            //全部筛选之后的结果
            JSONArray result=jc.importListFilterManage(shopId,"1","10",type,user,"").getJSONArray("list");
            for(int i=0;i<result.size();i++){
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(type),"参数全部输入的查询的"+type+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(user),"参数全部输入的查询的"+user+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[1][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[2][1])).contains(import_date),"参数全部输入的查询的"+import_date+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[2][1].toString()+"不一致"));

            }



        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("导入记录列表填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :导入记录查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void importListEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.importListFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("导入记录数不填写，结果校验");
        }
    }

    /**
     * @description :导出记录列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_exportListFilter",dataProviderClass = Constant.class)
    public void exportListOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.exportListFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.exportListFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "导出记录按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("导出记录列表单项查询，结果校验");
        }
    }

    /**
     * @description :导出记录列表-筛选栏填写全部参数查询
     * @date :2020/11/27
     **/
    @Test()
    public void exportListAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.exportListFilter_pram();
            JSONArray res=jc.exportListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            String type=data.getString(flag[0][1].toString());
            String user=data.getString(flag[1][1].toString());
            String export_time=data.getString(flag[2][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.exportListFilterManage(shopId,"1","10",type,user,export_time).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(type),"参数全部输入的查询的"+type+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(user),"参数全部输入的查询的"+user+"与列表信息的第一行的"+result.getString(flag[1][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(export_time),"参数全部输入的查询的"+export_time+"与列表信息的第一行的"+result.getString(flag[2][1].toString()+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("导出记录列表填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :导出记录列表-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void exportListSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.exportListFilter_pram();
            JSONArray res=jc.exportListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            String type=data.getString(flag[0][1].toString());
            String user=data.getString(flag[1][1].toString());
//            String export_time=data.getString(flag[2][1].toString());

            //全部筛选之后的结果
            JSONArray result=jc.exportListFilterManage(shopId,"1","10",type,user,"").getJSONArray("list");
            for(int i=0;i<result.size();i++){
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(type),"参数全部输入的查询的"+type+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(user),"参数全部输入的查询的"+user+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[1][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[2][1])).contains(export_time),"参数全部输入的查询的"+export_time+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[2][1].toString()+"不一致"));

            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("导出记录列表填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :导出记录查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void exportListEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.exportListFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("导出记录数不填写，结果校验");
        }
    }

    /**
     * @description :消息记录列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_pushMsgListFilter",dataProviderClass = Constant.class)
    public void pushMsgListOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.pushMsgListFilterManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.pushMsgListFilterManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
                for(int i=0;i<10;i++){
                    String Flag=list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "消息记录按"+result+"查询，结果错误"+Flag);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("消息记录列表单项查询，结果校验");
        }
    }
    /**
     * @description :消息记录列表-筛选栏填写全部参数查询
     * @date :2020/11/27
     **/
    @Test()
    public void pushMsgListAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.pushMsgListFilter_pram();
            JSONArray res=jc.pushMsgListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            String customer_type=data.getString(flag[0][1].toString());
            String push_date=data.getString(flag[1][1].toString());

            //全部筛选之后的结果
            JSONObject result=jc.pushMsgListFilterManage1(shopId,"1","10",customer_type,push_date).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(customer_type),"参数全部输入的查询的"+customer_type+"与列表信息的第一行的"+result.getString(flag[0][1].toString()+"不一致"));
            Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(push_date),"参数全部输入的查询的"+push_date+"与列表信息的第一行的"+result.getString(flag[1][1].toString()+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("消息记录列表填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :消息记录列表-筛选栏填写参数多项查询
     * @date :2020/11/28
     **/
    @Test()
    public void pushMsgListSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.pushMsgListFilter_pram();
            JSONArray res=jc.pushMsgListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            String customer_type=data.getString(flag[0][1].toString());
//            String push_date=data.getString(flag[1][1].toString());

            //全部筛选之后的结果
            JSONArray result=jc.pushMsgListFilterManage1(shopId,"1","10",customer_type,"").getJSONArray("list");
            for(int i=0;i<result.size();i++){
                Preconditions.checkArgument(customer_type.contains(result.getJSONObject(i).getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getJSONObject(i).getString(flag[0][1].toString()+"与列表信息的第"+i+"行的"+customer_type+"不一致"));
                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(customer_type),"参数全部输入的查询的"+customer_type+"与列表信息的第"+i+"行的"+result.getJSONObject(i).getString(flag[0][1].toString()+"不一致"));
//                Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(push_date),"参数全部输入的查询的"+push_date+"与列表信息的第一行的"+result.getJSONObject(i).getString(flag[1][1].toString()+"不一致"));

            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("消息记录列表填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :消息记录列表查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void pushMsgListEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            jc.pushMsgListFilterManage(shopId,"1","10","","").getJSONArray("list");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("消息记录列表数不填写，结果校验");
        }
    }








}
