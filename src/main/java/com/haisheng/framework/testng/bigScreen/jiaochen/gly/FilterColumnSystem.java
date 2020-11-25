package com.haisheng.framework.testng.bigScreen.jiaochen.gly;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
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
     * @description :接待管理查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_ReceptionManageFilter", dataProviderClass = Constant.class)
    public void receptionManageFilter(String pram,String output){
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
     * @description :销售客户查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_PreSleCustomerManageFilter",dataProviderClass = Constant.class)
    public void preSleCustomeManageFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
     try{
         JSONObject respon=jc.preSleCustomeMangae(shopId,"1","10","","");
         int pages=respon.getInteger("pages");
         String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
         for(int page=1;page<=pages;page++){
             JSONArray list=jc.preSleCustomeMangae(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
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
     * @description :售后客户查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_AfterSleCustomerManageFilter",dataProviderClass = Constant.class)
    public void afterSleCustomerManageFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.afterSleCustomeManage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.afterSleCustomeManage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
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
     * @description :小程序客户查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_weChatSleCustomerManageFilter",dataProviderClass = Constant.class)
    public void weChatSleCustomerManageFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject respon=jc.weChatSleCustomeMangage(shopId,"1","10","","");
            int pages=respon.getInteger("pages");
            String result=respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.weChatSleCustomeMangage(shopId,String.valueOf(page),"10",pram,result).getJSONArray("list");
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
     * @description :预约记录
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
     * @description :保养配置
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
     * @description :卡券管理
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
     * @description :发卡记录
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
     * @description :核销记录
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
     * @description :核销人员记录
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
     * @description :套餐表单
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
     * @description :套餐购买记录
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
     * @description :消息表单
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
     * @description :内容管理-文章表单
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
     * @description :报名列表
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
     * @description :报名审批列表
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
     * @description :卡券申请
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
     * @description :门店列表
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
     * @description :参数全填案例
     * @date :2020/11/25 12:15
     **/
    @Test()
    public void selectAppointmentRecodeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String ss[][]={
                {"plate_number", "plate_number"},
                {"reception_sale_id", "reception_sale_name"},
                {"reception_date","reception_date"},
                {"customer_name","customer_name"},
                {"reception_date","reception_date"},
                {"reception_status","registration_status"},
                {"finish_date","finish_time"},
                {"customer_phone","customer_phone"},
                {"reception_type","reception_type"},
                {"shop_id","shop_name"},
            };

            SelectReception sr=new SelectReception();
            JSONArray res=jc.pushMsgListFilterManage(shopId,"1","10","","").getJSONArray("list");
            JSONObject data=res.getJSONObject(0);
            sr.plate_number=data.getString(ss[0][1]);
            sr.reception_sale_id=data.getString(ss[0][2]);
            sr.reception_date=data.getString(ss[0][3]);

            JSONObject result=jc.receptionManageC(sr);

            Preconditions.checkArgument(sr.plate_number.contains(result.getString(ss[0][1])),"");

        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("接待管理列表查询全填，结果校验");
        }
    }






}
