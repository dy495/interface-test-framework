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
           saveData("接待管理查询，结果校验");
       }
    }

    /**
     * @description :接待管路-筛选栏参数全填查询
     * @date :2020/11/25 12:15
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
            Preconditions.checkArgument(sr.plate_number.contains(result.getString(String.valueOf(ss[0][1]))),"参数全部输入的查询的"+result.getString(ss[0][1].toString()+"与列表信息的第一行的"+sr.plate_number+"不一致"));
            Preconditions.checkArgument(sr.reception_sale_id.contains(result.getString(ss[1][1].toString())),"参数全部输入的查询的"+result.getString(ss[1][1].toString()+"与列表信息的第一行的"+sr.reception_sale_id+"不一致"));
            Preconditions.checkArgument(sr.reception_date.contains(result.getString(ss[2][1].toString())),"参数全部输入的查询的"+result.getString(ss[2][1].toString()+"与列表信息的第一行的"+sr.reception_date+"不一致"));
            Preconditions.checkArgument(sr.customer_name.contains(result.getString(String.valueOf(ss[3][1]))),"参数全部输入的查询的"+result.getString(ss[3][1].toString()+"与列表信息的第一行的"+sr.customer_name+"不一致"));
            Preconditions.checkArgument(sr.reception_status.contains(result.getString(ss[4][1].toString())),"参数全部输入的查询的"+result.getString(ss[4][1].toString()+"与列表信息的第一行的"+sr.reception_status+"不一致"));
            Preconditions.checkArgument(sr.finish_date.contains(result.getString(ss[5][1].toString())),"参数全部输入的查询的"+result.getString(ss[5][1].toString()+"与列表信息的第一行的"+sr.finish_date+"不一致"));
            Preconditions.checkArgument(sr.customer_phone.contains(result.getString(String.valueOf(ss[6][1]))),"参数全部输入的查询的"+result.getString(ss[6][1].toString()+"与列表信息的第一行的"+sr.customer_phone+"不一致"));
            Preconditions.checkArgument(sr.reception_type.contains(result.getString(ss[7][1].toString())),"参数全部输入的查询的"+result.getString(ss[7][1].toString()+"与列表信息的第一行"+sr.reception_type+"不一致"));
            Preconditions.checkArgument(sr.shop_id.contains(result.getString(ss[8][1].toString())),"参数全部输入的查询的"+result.getString(ss[8][1].toString()+"与列表信息的第一行的"+sr.shop_id+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("接待管理列表查询全填，结果校验");
        }
    }


    /**
     * @description :销售客户查询-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_PreSleCustomerManageFilter",dataProviderClass = Constant.class)
    public void preSleCustomerManageFilter(String pram,String output){
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
         saveData("销售客户查询，结果校验");
     }
    }

    /**
     * @description :销售客户查询-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void preSleCustomerManageFilter(){
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
            Preconditions.checkArgument(variable.customer_name.contains(result.getString(flag[0][1].toString())),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+variable.customer_name+"不一致"));
            Preconditions.checkArgument(variable.customer_phone.contains(result.getString(flag[1][1].toString())),"参数全部输入的查询的"+result.getString(flag[1][1].toString()+"与列表信息的第一行的"+variable.customer_phone+"不一致"));
            Preconditions.checkArgument(variable.create_date.contains(result.getString(flag[2][1].toString())),"参数全部输入的查询的"+result.getString(flag[2][1].toString()+"与列表信息的第一行的"+variable.create_date+"不一致"));
            Preconditions.checkArgument(variable.sale_name.contains(result.getString(flag[3][1].toString())),"参数全部输入的查询的"+result.getString(flag[3][1].toString()+"与列表信息的第一行的"+variable.sale_name+"不一致"));
            Preconditions.checkArgument(variable.customer_type.contains(result.getString(flag[4][1].toString())),"参数全部输入的查询的"+result.getString(flag[4][1].toString()+"与列表信息的第一行的"+variable.customer_type+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("销售客户查询，结果校验");
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
            saveData("售后客户客户查询，结果校验");
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
            Preconditions.checkArgument(variable.vehicle_chassis_code.contains(result.getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+variable.vehicle_chassis_code+"不一致"));
            Preconditions.checkArgument(variable.create_date.contains(result.getString(flag[1][1].toString())),"参数全部输入的查询的"+result.getString(flag[1][1].toString()+"与列表信息的第一行的"+variable.create_date+"不一致"));
            Preconditions.checkArgument(variable.customer_name.contains(result.getString(flag[2][1].toString())),"参数全部输入的查询的"+result.getString(flag[2][1].toString()+"与列表信息的第一行的"+variable.customer_name+"不一致"));
            Preconditions.checkArgument(variable.customer_phone.contains(result.getString(String.valueOf(flag[3][1]))),"参数全部输入的查询的"+result.getString(flag[3][1].toString()+"与列表信息的第一行的"+variable.customer_phone+"不一致"));
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("售后客户客户查询，结果校验");
        }
    }

    /**
     * @description :小程序客户查询-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_weChatSleCustomerManageFilter",dataProviderClass = Constant.class)
    public void weChatSleCustomerManageFilter(String pram,String output){
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
            saveData("客户小程序客户查询，结果校验");
        }
    }

    /**
     * @description :小程序客户查询-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void weChatSleCustomerManageFilter(){
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
            Preconditions.checkArgument(variable.create_date.contains(result.getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+variable.create_date+"不一致"));
            Preconditions.checkArgument(variable.active_type.contains(result.getString(flag[1][1].toString())),"参数全部输入的查询的"+result.getString(flag[1][1].toString()+"与列表信息的第一行的"+variable.active_type+"不一致"));
            Preconditions.checkArgument(variable.customer_phone.contains(result.getString(flag[2][1].toString())),"参数全部输入的查询的"+result.getString(flag[2][1].toString()+"与列表信息的第一行的"+variable.customer_phone+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("客户小程序客户查询，结果校验");
        }
    }

    /**
     * @description :预约记录-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_appointmentRecordFilter",dataProviderClass = Constant.class)
    public void appointmentRecordFilter(String pram,String output){
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
            saveData("预约记录查询，结果校验");
        }
    }
    /**
     * @description :预约记录-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void appointmentRecordFilter(){
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
            Preconditions.checkArgument(variable.plate_number.contains(result.getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+variable.plate_number+"不一致"));
            Preconditions.checkArgument(variable.customer_manager.contains(result.getString(flag[1][1].toString())),"参数全部输入的查询的"+result.getString(flag[1][1].toString()+"与列表信息的第一行的"+variable.customer_manager+"不一致"));
            Preconditions.checkArgument(variable.shop_id.contains(result.getString(flag[2][1].toString())),"参数全部输入的查询的"+result.getString(flag[2][1].toString()+"与列表信息的第一行的"+variable.shop_id+"不一致"));
            Preconditions.checkArgument(variable.customer_name.contains(result.getString(flag[3][1].toString())),"参数全部输入的查询的"+result.getString(flag[3][1].toString()+"与列表信息的第一行的"+variable.customer_name+"不一致"));
            Preconditions.checkArgument(variable.appointment_status.contains(result.getString(flag[4][1].toString())),"参数全部输入的查询的"+result.getString(flag[4][1].toString()+"与列表信息的第一行的"+variable.appointment_status+"不一致"));
            Preconditions.checkArgument(variable.customer_phone.contains(result.getString(flag[5][1].toString())),"参数全部输入的查询的"+result.getString(flag[5][1].toString()+"与列表信息的第一行的"+variable.customer_phone+"不一致"));
            Preconditions.checkArgument(variable.is_overtime.contains(result.getString(flag[6][1].toString())),"参数全部输入的查询的"+result.getString(flag[6][1].toString()+"与列表信息的第一行的"+variable.is_overtime+"不一致"));
            Preconditions.checkArgument(variable.confirm_time.contains(result.getString(flag[7][1].toString())),"参数全部输入的查询的"+result.getString(flag[7][1].toString()+"与列表信息的第一行的"+variable.confirm_time+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("预约记录查询，结果校验");
        }
    }

    /**
     * @description :保养配置-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_maintainFilter",dataProviderClass = Constant.class)
    public void maintainFilter(String pram,String output){
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
            saveData("保养配置查询，结果校验");
        }
    }

    /**
     * @description :保养配置-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void maintainFilter(){
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
            Preconditions.checkArgument(variable.brand_name.contains(result.getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+variable.brand_name+"不一致"));
            Preconditions.checkArgument(variable.manufacturer.contains(result.getString(flag[1][1].toString())),"参数全部输入的查询的"+result.getString(flag[1][1].toString()+"与列表信息的第一行的"+variable.manufacturer+"不一致"));
            Preconditions.checkArgument(variable.car_model.contains(result.getString(flag[2][1].toString())),"参数全部输入的查询的"+result.getString(flag[2][1].toString()+"与列表信息的第一行的"+variable.car_model+"不一致"));
            Preconditions.checkArgument(variable.year.contains(result.getString(flag[3][1].toString())),"参数全部输入的查询的"+result.getString(flag[3][1].toString()+"与列表信息的第一行的"+variable.year+"不一致"));
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("保养配置查询，结果校验");
        }
    }

    /**
     * @description :卡券管理-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_voucherFormFilter",dataProviderClass = Constant.class)
    public void voucherFormFilter(String pram,String output){
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
            saveData("卡券管理查询，结果校验");
        }
    }

    /**
     * @description :卡券管理-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void voucherFormFilter(){
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
            Preconditions.checkArgument(variable.subject_name.contains(result.getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+variable.subject_name+"不一致"));
            Preconditions.checkArgument(variable.voucher_name.contains(result.getString(flag[1][1].toString())),"参数全部输入的查询的"+result.getString(flag[1][1].toString()+"与列表信息的第一行的"+variable.voucher_name+"不一致"));
            Preconditions.checkArgument(variable.creator.contains(result.getString(flag[2][1].toString())),"参数全部输入的查询的"+result.getString(flag[2][1].toString()+"与列表信息的第一行的"+variable.creator+"不一致"));
            Preconditions.checkArgument(variable.is_diff.contains(result.getString(flag[3][1].toString())),"参数全部输入的查询的"+result.getString(flag[3][1].toString()+"与列表信息的第一行的"+variable.is_diff+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("卡券管理查询，结果校验");
        }
    }

    /**
     * @description :发卡记录-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_sendRecordFilter",dataProviderClass = Constant.class)
    public void sendRecordFilter(String pram,String output){
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
            saveData("发卡记录查询，结果校验");
        }
    }

    /**
     * @description :发卡记录-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void sendRecordFilter(){
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
            Preconditions.checkArgument(variable.voucher_name.contains(result.getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+variable.voucher_name+"不一致"));
            Preconditions.checkArgument(variable.sender.contains(result.getString(flag[1][1].toString())),"参数全部输入的查询的"+result.getString(flag[1][1].toString()+"与列表信息的第一行的"+variable.sender+"不一致"));
            Preconditions.checkArgument(variable.start_time.contains(result.getString(flag[2][1].toString())),"参数全部输入的查询的"+result.getString(flag[2][1].toString()+"与列表信息的第一行的"+variable.start_time+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("发卡记录查询，结果校验");
        }
    }

    /**
     * @description :核销记录-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_verificationRecordFilter",dataProviderClass = Constant.class)
    public void verificationRecordFilter(String pram,String output){
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
            saveData("核销记录查询，结果校验");
        }
    }



    /**
     * @description :核销记录-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void verificationRecordFilter(){
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
            Preconditions.checkArgument(variable.voucher_name.contains(result.getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+variable.voucher_name+"不一致"));
            Preconditions.checkArgument(variable.sender.contains(result.getString(flag[1][1].toString())),"参数全部输入的查询的"+result.getString(flag[1][1].toString()+"与列表信息的第一行的"+variable.sender+"不一致"));
            Preconditions.checkArgument(variable.start_time.contains(result.getString(flag[2][1].toString())),"参数全部输入的查询的"+result.getString(flag[2][1].toString()+"与列表信息的第一行的"+variable.start_time+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("核销记录查询，结果校验");
        }
    }

    /**
     * @description :核销人员记录-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_verificationPeopleFilter",dataProviderClass = Constant.class)
    public void verificationPeopleFilter(String pram,String output){
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
            saveData("核销记录查询，结果校验");
        }
    }
    /**
     * @description :核销人员记录-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void verificationPeopleFilter(){
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
            Preconditions.checkArgument(variable.verification_person.contains(result.getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+variable.verification_person+"不一致"));
            Preconditions.checkArgument(variable.verification_phone.contains(result.getString(flag[1][1].toString())),"参数全部输入的查询的"+result.getString(flag[1][1].toString()+"与列表信息的第一行的"+variable.verification_phone+"不一致"));
            Preconditions.checkArgument(variable.verification_code.contains(result.getString(flag[2][1].toString())),"参数全部输入的查询的"+result.getString(flag[2][1].toString()+"与列表信息的第一行的"+variable.verification_code+"不一致"));


        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("核销记录查询，结果校验");
        }
    }

    /**
     * @description :套餐表单-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_packageFormFilter",dataProviderClass = Constant.class)
    public void packageFormFilter(String pram,String output){
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
            saveData("套餐表单查询，结果校验");
        }
    }

    /**
     * @description :套餐表单-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void packageFormFilter(){
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
            Preconditions.checkArgument(variable.package_name.contains(result.getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+variable.package_name+"不一致"));
            Preconditions.checkArgument(variable.creator.contains(result.getString(flag[1][1].toString())),"参数全部输入的查询的"+result.getString(flag[1][1].toString()+"与列表信息的第一行的"+variable.creator+"不一致"));
            Preconditions.checkArgument(variable.start_time.contains(result.getString(flag[2][1].toString())),"参数全部输入的查询的"+result.getString(flag[2][1].toString()+"与列表信息的第一行的"+variable.start_time+"不一致"));
            Preconditions.checkArgument(variable.shop_name.contains(result.getString(flag[3][1].toString())),"参数全部输入的查询的"+result.getString(flag[3][1].toString()+"与列表信息的第一行的"+variable.shop_name+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("套餐表单查询，结果校验");
        }
    }

    /**
     * @description :套餐购买记录-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_buyPackageRecordFilter",dataProviderClass = Constant.class)
    public void buyPackageRecordFilter(String pram,String output){
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
            saveData("套餐购买记录查询，结果校验");
        }
    }

    /**
     * @description :套餐购买记录-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void buyPackageRecordFilter(){
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
            Preconditions.checkArgument(variable.package_name.contains(result.getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+variable.package_name+"不一致"));
            Preconditions.checkArgument(variable.start_time.contains(result.getString(flag[1][1].toString())),"参数全部输入的查询的"+result.getString(flag[1][1].toString()+"与列表信息的第一行的"+variable.start_time+"不一致"));
            Preconditions.checkArgument(variable.send_type.contains(result.getString(flag[2][1].toString())),"参数全部输入的查询的"+result.getString(flag[2][1].toString()+"与列表信息的第一行的"+variable.send_type+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("套餐购买记录查询，结果校验");
        }
    }

    /**
     * @description :消息表单-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_messageFormFilter",dataProviderClass = Constant.class)
    public void messageFormFilter(String pram,String output){
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
            saveData("消息表单查询，结果校验");
        }
    }

    /**
     * @description :消息表单-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void messageFormFilter(){
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
            Preconditions.checkArgument(variable.shop_id.contains(result.getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+variable.shop_id+"不一致"));
            Preconditions.checkArgument(variable.start_time.contains(result.getString(flag[1][1].toString())),"参数全部输入的查询的"+result.getString(flag[1][1].toString()+"与列表信息的第一行的"+variable.start_time+"不一致"));
            Preconditions.checkArgument(variable.customer_name.contains(result.getString(flag[2][1].toString())),"参数全部输入的查询的"+result.getString(flag[2][1].toString()+"与列表信息的第一行的"+variable.customer_name+"不一致"));
            Preconditions.checkArgument(variable.message_type.contains(result.getString(flag[3][1].toString())),"参数全部输入的查询的"+result.getString(flag[3][1].toString()+"与列表信息的第一行的"+variable.message_type+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("消息表单查询，结果校验");
        }
    }

    /**
     * @description :内容管理-文章表单-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_articleFilter",dataProviderClass = Constant.class)
    public void articleFilter(String pram,String output){
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
            saveData("文章表单查询，结果校验");
        }
    }

    /**
     * @description :内容管理-文章表单-筛选栏填写全部参数查询-----活动开始时间结束时间，报名开始时间结束时间对不上
     * @date :2020/11/24
     **/
    @Test()
    public void articleFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.articleFilter_pram();
            articleVariable variable=new articleVariable();
            JSONArray res=jc.articleFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            variable.title=data.getString(flag[0][1].toString());


            //全部筛选之后的结果
            JSONObject result=jc.articleFilterManage(variable).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(variable.title.contains(result.getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+variable.title+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("文章表单查询，结果校验");
        }
    }

    /**
     * @description :报名列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_registerListFilter",dataProviderClass = Constant.class)
    public void registerListFilter(String pram,String output){
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
            saveData("报名列表查询，结果校验");
        }
    }

    /**
     * @description :报名列表-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void registerListFilter(){
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
            Preconditions.checkArgument(variable.title.contains(result.getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+variable.title+"不一致"));
            Preconditions.checkArgument(variable.start_date.contains(result.getString(flag[1][1].toString())),"参数全部输入的查询的"+result.getString(flag[1][1].toString()+"与列表信息的第一行的"+variable.start_date+"不一致"));
            Preconditions.checkArgument(variable.end_date.contains(result.getString(flag[2][1].toString())),"参数全部输入的查询的"+result.getString(flag[2][1].toString()+"与列表信息的第一行的"+variable.end_date+"不一致"));
            Preconditions.checkArgument(variable.register_start_date.contains(result.getString(flag[3][1].toString())),"参数全部输入的查询的"+result.getString(flag[3][1].toString()+"与列表信息的第一行的"+variable.register_start_date+"不一致"));
            Preconditions.checkArgument(variable.register_end_date.contains(result.getString(flag[4][1].toString())),"参数全部输入的查询的"+result.getString(flag[4][1].toString()+"与列表信息的第一行的"+variable.register_end_date+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("报名列表查询，结果校验");
        }
    }

    /**
     * @description :报名审批列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_registerListFilter",dataProviderClass = Constant.class)
    public void approvalListFilter(String pram,String output){
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
            saveData("报名审批查询，结果校验");
        }
    }

    /**
     * @description :报名审批列表-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void approvalListFilter(){
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
            Preconditions.checkArgument(variable.customer_name.contains(result.getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+variable.customer_name+"不一致"));
            Preconditions.checkArgument(variable.phone.contains(result.getString(flag[1][1].toString())),"参数全部输入的查询的"+result.getString(flag[1][1].toString()+"与列表信息的第一行的"+variable.phone+"不一致"));
            Preconditions.checkArgument(variable.status.contains(result.getString(flag[2][1].toString())),"参数全部输入的查询的"+result.getString(flag[2][1].toString()+"与列表信息的第一行的"+variable.status+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("报名审批查询，结果校验");
        }
    }

    /**
     * @description :卡券申请-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_applyListFilter",dataProviderClass = Constant.class)
    public void applyListFilter(String pram,String output){
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
            saveData("卡券申请查询，结果校验");
        }
    }

    /**
     * @description :卡券申请-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void applyListFilter(){
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
            Preconditions.checkArgument(variable.name.contains(result.getString(String.valueOf(flag[0][1]))),"参数全部输入的查询的"+result.getString(flag[0][1].toString()+"与列表信息的第一行的"+variable.name+"不一致"));
            Preconditions.checkArgument(variable.apply_name.contains(result.getString(flag[1][1].toString())),"参数全部输入的查询的"+result.getString(flag[1][1].toString()+"与列表信息的第一行的"+variable.apply_name+"不一致"));
            Preconditions.checkArgument(variable.status.contains(result.getString(flag[2][1].toString())),"参数全部输入的查询的"+result.getString(flag[2][1].toString()+"与列表信息的第一行的"+variable.status+"不一致"));
            Preconditions.checkArgument(variable.apply_time.contains(result.getString(String.valueOf(flag[3][1]))),"参数全部输入的查询的"+result.getString(flag[3][1].toString()+"与列表信息的第一行的"+variable.apply_time+"不一致"));
            Preconditions.checkArgument(variable.apply_group.contains(result.getString(flag[4][1].toString())),"参数全部输入的查询的"+result.getString(flag[4][1].toString()+"与列表信息的第一行的"+variable.apply_group+"不一致"));
            Preconditions.checkArgument(variable.apply_item.contains(result.getString(flag[5][1].toString())),"参数全部输入的查询的"+result.getString(flag[5][1].toString()+"与列表信息的第一行的"+variable.apply_item+"不一致"));

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("卡券申请查询，结果校验");
        }
    }

    /**
     * @description :门店列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_shopListFilter",dataProviderClass = Constant.class)
    public void shopListFilter(String pram,String output){
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
            saveData("门店列表查询，结果校验");
        }
    }

    /**
     * @description :门店列表-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void shopListFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("门店列表查询，结果校验");
        }
    }

    /**
     * @description :品牌列表
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_brandListFilter",dataProviderClass = Constant.class)
    public void brandListFilter(String pram,String output){
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
            saveData("品牌列表查询，结果校验");
        }
    }

    /**
     * @description :品牌列表
     * @date :2020/11/24
     **/
    @Test()
    public void brandListFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("品牌列表查询，结果校验");
        }
    }

    /**
     * @description :车系列表
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_brandListFilter",dataProviderClass = Constant.class)
    public void carStyleListFilter(String pram,String output){
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
            saveData("车系列表查询，结果校验");
        }
    }

    /**
     * @description :车系列表
     * @date :2020/11/24
     **/
    @Test()
    public void carStyleListFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("车系列表查询，结果校验");
        }
    }

    /**
     * @description :车型列表
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_brandListFilter",dataProviderClass = Constant.class)
    public void carModelListFilter(String pram,String output){
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
            saveData("车型列表查询，结果校验");
        }
    }
    /**
     * @description :车型列表
     * @date :2020/11/24
     **/
    @Test()
    public void carModelListFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("车型列表查询，结果校验");
        }
    }

    /**
     * @description :角色列表
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_roleListFilter",dataProviderClass = Constant.class)
    public void roleListFilter(String pram,String output){
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
            saveData("角色列表查询，结果校验");
        }
    }

    /**
     * @description :角色列表
     * @date :2020/11/24
     **/
    @Test()
    public void roleListFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("角色列表查询，结果校验");
        }
    }

    /**
     * @description :员工列表
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_roleListFilter",dataProviderClass = Constant.class)
    public void staffListFilter(String pram,String output){
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
            saveData("员工列表查询，结果校验");
        }
    }

    /**
     * @description :员工列表
     * @date :2020/11/24
     **/
    @Test()
    public void staffListFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("员工列表查询，结果校验");
        }
    }

    /**
     * @description :导入记录列表
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_importListFilter",dataProviderClass = Constant.class)
    public void importListFilter(String pram,String output){
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
            saveData("导入记录列表查询，结果校验");
        }
    }

    /**
     * @description :导入记录列表
     * @date :2020/11/24
     **/
    @Test()
    public void importListFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("导入记录列表查询，结果校验");
        }
    }

    /**
     * @description :导出记录列表
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_exportListFilter",dataProviderClass = Constant.class)
    public void exportListFilter(String pram,String output){
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
            saveData("导出记录列表查询，结果校验");
        }
    }

    /**
     * @description :导出记录列表
     * @date :2020/11/24
     **/
    @Test()
    public void exportListFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("导出记录列表查询，结果校验");
        }
    }

    /**
     * @description :消息记录列表
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_pushMsgListFilter",dataProviderClass = Constant.class)
    public void pushMsgListFilter(String pram,String output){
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
            saveData("消息记录列表查询，结果校验");
        }
    }
    /**
     * @description :消息记录列表
     * @date :2020/11/24
     **/
    @Test()
    public void pushMsgListFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("消息记录列表查询，结果校验");
        }
    }







}
