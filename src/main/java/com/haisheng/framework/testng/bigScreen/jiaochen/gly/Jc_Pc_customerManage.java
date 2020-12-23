package com.haisheng.framework.testng.bigScreen.jiaochen.gly;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumRefer;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumShopId;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.PublicParm;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class Jc_Pc_customerManage extends TestCaseCommon implements TestCaseStd {
    ScenarioUtil jc = new ScenarioUtil();
    PublicParm pp = new PublicParm();
//    JsonPathUtil jpu = new JsonPathUtil();
    public String shopId = "-1";
    CommonPram cp=new CommonPram();
    public String appletTocken= EnumAppletToken.JC_GLY_DAILY.getToken();
    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_JIAOCHEN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "郭丽雅";
        commonConfig.produce = EnumProduce.JC.name();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "jc-daily-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getName() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.shopId = EnumShopId.JIAOCHEN_DAILY.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);
        commonConfig.referer = EnumRefer.JIAOCHEN_REFERER_DAILY.getReferer();
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
        jc.pcLogin(pp.gwphone, pp.gwpassword);
    }

    /**
     * @description :客户管理-销售客户-异常校验-客户名称51个字-先校验注册再校验客户名称51个字--研发没有校验，数据库长度越界
     * @date :2020/12/22
     **/
    @Test(enabled = false)
    public void customerManage_System1(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] phone=cp.phoneNumberFormat();
            Object[][] sex=cp.sex();
            Object[][] name=cp.name();
            Object[][] customerType=cp.customerType();
            //姓名为空
            JSONObject respon=jc.createCustomer("",name[0][1].toString(),cp.phoneNumber,sex[1][1].toString(),customerType[1][1].toString());
            String message=respon.getString("message");
            Preconditions.checkArgument(message.equals("客户名称不允许为空"),"姓名入参为空，返回的的message为："+message);
            //客户姓名51个字
            JSONObject respon1=jc.createCustomer("",name[0][1].toString(),"13373166806",sex[1][1].toString(),customerType[1][1].toString());
            String message1=respon1.getString("message");
            Preconditions.checkArgument(message1.equals(" "),"姓名入参51个字，返回的的message为："+message1);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理-销售客户-异常校验-客户名称51个字");
        }
    }

    /**
     * @description :客户管理-销售客户-异常校验-手机号格式校验---现在都报异常，服务端没有校验
     * @date :2020/12/22
     **/
    @Test(enabled = false)
    public void customerManage_System2(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] phone=cp.phoneNumberFormat();
            Object[][] sex=cp.sex();
            Object[][] name=cp.name();
            Object[][] customerType=cp.customerType();
            for(int i=0;i<7;i++){
                JSONObject respon=jc.createCustomer("",cp.name,phone[i][1].toString(),sex[1][1].toString(),customerType[1][1].toString());
                String message=respon.getString("message");
                Preconditions.checkArgument(message.equals("手机号格式不正确")||message.equals("手机号已存在")||message.equals("手机号不能为空"),phone[i][0].toString()+" : "+phone[i][1].toString());
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理-销售客户-异常校验-手机号格式校验");
        }
    }

    /**
     * @description :客户管理-销售客户-异常校验-性别异常校验---现在都报异常，服务端没有校验，message信息现在没有填写
     * @date :2020/12/22
     **/
    @Test(enabled = false)
    public void customerManage_System3(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] phone=cp.phoneNumberFormat();
            Object[][] sex=cp.sex();
            Object[][] name=cp.name();
            Object[][] customerType=cp.customerType();
            for(int i=2;i<=3;i++){
                JSONObject respon=jc.createCustomer("",cp.name,cp.phoneNumber,sex[i][1].toString(),customerType[1][1].toString());
                String message=respon.getString("message");
                Preconditions.checkArgument(message.equals(""),sex[i][0].toString()+" : "+sex[i][1].toString());
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理-销售客户-异常校验-性别异常校验");
        }
    }

    /**
     * @description :客户管理-销售客户-异常校验-车主类型异常校验---现在都报异常，服务端没有校验,message信息现在没有填写
     * @date :2020/12/22
     **/
    @Test(enabled = false)
    public void customerManage_System4(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] phone=cp.phoneNumberFormat();
            Object[][] sex=cp.sex();
            Object[][] name=cp.name();
            Object[][] customerType=cp.customerType();
            for(int i=2;i<=3;i++){
                JSONObject respon=jc.createCustomer("",cp.name,cp.phoneNumber,sex[1][1].toString(),customerType[i][1].toString());
                String message=respon.getString("message");
                Preconditions.checkArgument(message.equals(""),customerType[i][0].toString()+" : "+customerType[i][1].toString());
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理-销售客户-异常校验-车主类型异常校验");
        }
    }

    /**
     * @description :客户管理-销售客户-列表项校验不为空--现在销售客户界面存在问题，列表为空
     * @date :2020/12/22
     **/
    @Test
    public void customerManage_System5(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon=jc.preSleCustomerManage("","1","10","","");
            int pages=respon.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.preSleCustomerManage("",String.valueOf(page),"10","","").getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    String shopName=list.getJSONObject(i).getString("shop_name");
                    String ownerTypeName=list.getJSONObject(i).getString("owner_type_name");
                    String customerTypeName=list.getJSONObject(i).getString("customer_type_name");
                    String registrationStatusName=list.getJSONObject(i).getString("registration_status_name");
                    String customerName=list.getJSONObject(i).getString("customer_name");
                    String customerPhone=list.getJSONObject(i).getString("customer_phone");
                    String sex=list.getJSONObject(i).getString("sex");
                    String createDate=list.getJSONObject(i).getString("create_date");
                    String saleName=list.getJSONObject(i).getString("sale_name");
                    Preconditions.checkArgument(shopName!=null&&ownerTypeName!=null&&customerTypeName!=null&&registrationStatusName!=null&&customerPhone!=null&&customerName!=null&&sex!=null&&createDate!=null&&saleName!=null,"销售客户列表中第 "+(i+1)+"行，列表项为空");
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理-销售客户-列表项校验不为空");
        }
    }

    /**
     * @description :客户管理-销售客户-列表排序按照创建时间倒序排列
     * @date :2020/12/22
     **/
    @Test
    public void customerManage_System6(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon=jc.preSleCustomerManage("","1","10","","");
            int pages=respon.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=jc.preSleCustomerManage("",String.valueOf(page),"10","","").getJSONArray("list");
                for(int i=0;i<list.size()-1;i++){
                    String createDate=list.getJSONObject(i).getString("create_date");
                    String createDateNext=list.getJSONObject(i+1).getString("create_date");
                    Preconditions.checkArgument(createDate.compareTo(createDateNext)>0||createDate.compareTo(createDateNext)==0,"表排序按照创建时间倒序排列，现在的创建时间是:"+createDate+"下一个创建时间为："+createDateNext);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理-销售客户-列表排序按照创建时间倒序排列");
        }
    }

}

