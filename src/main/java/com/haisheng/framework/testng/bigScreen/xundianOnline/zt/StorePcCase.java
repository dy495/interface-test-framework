package com.haisheng.framework.testng.bigScreen.xundianOnline.zt;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.xundianDaily.MendianInfo;
import com.haisheng.framework.testng.bigScreen.xundianDaily.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.XundianScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.StorePcAndAppData;
import com.haisheng.framework.testng.bigScreen.xundianDaily.zt.DataProviderMethod;
import com.haisheng.framework.testng.bigScreen.xundianOnline.MendianInfoOnline;
import com.haisheng.framework.testng.bigScreen.xundianOnline.StoreScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.xundianOnline.XundianScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkArgument;

public class StorePcCase extends TestCaseCommon implements TestCaseStd{



    public static final Logger log = LoggerFactory.getLogger(StorePcAndAppData.class);
    public static final int size = 100;
    public static final int page = 1;
    XundianScenarioUtilOnline xd = XundianScenarioUtilOnline.getInstance();
    StoreScenarioUtilOnline md = StoreScenarioUtilOnline.getInstance();
    MendianInfoOnline info = new MendianInfoOnline();



    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "周涛";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "mendian-online-test");
        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店(巡店) 线上");
        commonConfig.dingHook = DingWebhook.ONLINE_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"13604609869", "15084928847"};
        commonConfig.shopId = getXunDianShopOnline(); //要改！！！
        beforeClassInit(commonConfig);
        logger.debug("xundian " + xd);
        xd.login("storedemo@winsense.ai","b0581aa73b04d9fe6e3057a613e6f363");
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }


    //客流分析门店搜索
    @Test(dataProvider = "type",dataProviderClass = DataProviderMethod.class)
    public void ShopperTrak(String type_1) throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray type = new JSONArray();
            type.add(type_1);
            //获取list
            JSONArray list = md.customerFlowList("110000",type,info.shop_id_01_chin,"张三丰",null,null,1,10,null).getJSONArray("list");
            //获取district_code,type,shop_name
            for(int i=0;i<list.size();i++){
                String district_code = list.getJSONObject(i).getString("district_code");
                String district_name = list.getJSONObject(i).getString("district_name");
                String type1 = list.getJSONObject(i).getString("type");
                String shop_name = list.getJSONObject(i).getString("name");
                String manager_name = list.getJSONObject(i).getString("manager_name");
                checkArgument(district_code.contains("110"), "选择的地理位置" + district_code + "!=搜索出来门店展示的地理位置" + district_name);
                checkArgument(type1.equals(type_1), "选择的门店类型" + type_1 + "!=搜索出来门店展示的门店类型" + type1);
                checkArgument(shop_name.equals(info.shop_id_01_chin), "输入的门店名称" + info.shop_id_01_chin + "!=搜索出来门店的名称" + shop_name);
                checkArgument(manager_name.equals("张三丰"), "输入的门店负责人" + "张三丰" + "!=搜索出来门店的负责人" + manager_name);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客流分析通过搜索框输入==客流分析搜索出来门店的内容");
        }
    }



    //    //门店客户列表通过搜索框搜索门店
    @Test(dataProvider = "type",dataProviderClass = DataProviderMethod.class)
    public void StoreTrak(String type_1) throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray type = new JSONArray();
            type.add(type_1);
            //获取list
            JSONArray list = md.shopPageMemberV31("110000",type,info.shop_id_01_chin,"张三丰",null,null,1,10).getJSONArray("list");
            int total = md.shopPageMemberV31("110000",type,info.shop_id_01_chin,"张三丰",null,null,1,10).getInteger("total");
            if ( total!=0 ){
                for(int i=0;i<list.size();i++){
                    String district_code = list.getJSONObject(i).getString("district_code");
                    String district_name = list.getJSONObject(i).getString("district_name");
                    String type1 = list.getJSONObject(i).getString("type");
                    String shop_name = list.getJSONObject(i).getString("name");
                    String manager_name = list.getJSONObject(i).getString("manager_name");
                    checkArgument(district_code.contains("110"), "选择的地理位置" + district_code + "!=搜索出来门店展示的地理位置" + district_name);
                    checkArgument(type1.equals(type_1), "选择的门店类型" + "NORMAL" + "!=搜索出来门店展示的门店类型" + type1);
                    checkArgument(shop_name.equals(info.shop_id_01_chin), "输入的门店名称" + info.shop_id_01_chin + "!=搜索出来门店的名称" + shop_name);
                    checkArgument(manager_name.equals("张三丰"), "输入的门店负责人" + "张三丰" + "!=搜索出来门店的负责人" + manager_name);
                }
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店客户通过搜索框输入==门店客户搜索出来门店的内容");
        }
    }




    //    //门店客户列表通过搜索框搜索门店
    @Test(dataProvider = "type",dataProviderClass = DataProviderMethod.class)
    public void NewStoreUser(String type_1) throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray type = new JSONArray();
            type.add(type_1);
            //获取list
            JSONArray list = md.NewUser("110000",type,info.shop_id_01_chin,"张三丰",null,null,1,10).getJSONArray("list");
            int total = md.NewUser("110000",type,info.shop_id_01_chin,"张三丰",null,null,1,10).getInteger("total");
            if ( total!=0 ){
                for(int i=0;i<list.size();i++){
                    String district_code = list.getJSONObject(i).getString("district_code");
                    String district_name = list.getJSONObject(i).getString("district_name");
                    String type1 = list.getJSONObject(i).getString("type");
                    String shop_name = list.getJSONObject(i).getString("name");
                    String manager_name = list.getJSONObject(i).getString("manager_name");
                    checkArgument(district_code.contains("110"), "选择的地理位置" + district_code + "!=搜索出来门店展示的地理位置" + district_name);
                    checkArgument(type1.equals(type_1), "选择的门店类型" + type_1 + "!=搜索出来门店展示的门店类型" + type1);
                    checkArgument(shop_name.equals(info.shop_id_01_chin), "输入的门店名称" + info.shop_id_01_chin + "!=搜索出来门店的名称" + shop_name);
                    checkArgument(manager_name.equals("张三丰"), "输入的门店负责人" + "张三丰" + "!=搜索出来门店的负责人" + manager_name);
                }
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新增客户通过搜索框输入==新增客户搜索出来门店的内容");
        }
    }

    //会员信息通过会员id筛选
    @Test(dataProvider = "memberId",dataProviderClass = DataProviderMethod.class)
    public void searchMember(String memberId) throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.MemberList(page,size,memberId,null,null,null,null).getJSONArray("list");
            for (int j = 0 ; j < list.size();j++) {
                String memId = list.getJSONObject(j).getString("member_id");
                Preconditions.checkArgument(memId.equals(memberId),"根据"+memberId+"查询，结果包含"+memId);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员信息列表通过会员Id搜索");
        }
    }

    //会员信息通过姓名筛选（包阔模糊搜索和精确搜索）
    @Test(dataProvider = "memberName",dataProviderClass = DataProviderMethod.class)
    public void searchMember1(String memberName) throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.MemberList(page,size,null,memberName,null,null,null).getJSONArray("list");
            for (int j = 0 ; j < list.size();j++) {
                String memName = list.getJSONObject(j).getString("member_name");
                Preconditions.checkArgument(memName.contains(memberName),"根据"+memberName+"查询，结果包含"+memName);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员信息列表通过会员姓名搜索");
        }
    }


    //会员信息列表通过电话筛选
    @Test(dataProvider = "memberPhone",dataProviderClass = DataProviderMethod.class)
    public void searchMember2(String memberPhone) throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.MemberList(page,size,null,null,memberPhone,null,null).getJSONArray("list");
            for (int j = 0 ; j < list.size();j++) {
                String memPhone = list.getJSONObject(j).getString("phone");
                Preconditions.checkArgument(memPhone.contains(memberPhone), "根据" + memberPhone + "查询，结果包含" + memPhone);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员信息列表通过会员电话搜索");
        }
    }

    //会员信息列表通过人物id筛选
    @Test(dataProvider = "userId",dataProviderClass = DataProviderMethod.class)
    public void searchMember3(String userId) throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.MemberList(page,size,null,null,null,userId,null).getJSONArray("list");
            for (int j = 0 ; j < list.size();j++) {
                String memUserId = list.getJSONObject(j).getString("user_id");
                Preconditions.checkArgument(memUserId.contains(userId), "根据" + userId + "查询，结果包含" + memUserId);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员信息列表通过人物id搜索");
        }
    }


    //会员信息列表通过会员身份筛选
    @Test(dataProvider = "identity",dataProviderClass = DataProviderMethod.class)
    public void searchMember4(String identity) throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.MemberList(page,size,null,null,null,null,identity).getJSONArray("list");
            for (int j = 0 ; j < list.size();j++) {
                String memIdentity = list.getJSONObject(j).getString("identity");
                Preconditions.checkArgument(memIdentity.contains(identity), "根据" + identity + "查询，结果包含" + memIdentity);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员信息列表通过会员身份搜索");
        }
    }






    //会员信息列表通过会员id和会员姓名筛选
    @Test(dataProvider = "userIdName",dataProviderClass = DataProviderMethod.class)
    public void searchMember5(String userId,String userName ) throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list0 = md.MemberList(page,size,userId,userName,null,null,null).getJSONArray("list");
            if (list0.size()!=0){
                String memberId1 = list0.getJSONObject(0).getString("member_id");
                String memberName1 = list0.getJSONObject(0).getString("member_name");
                Preconditions.checkArgument(userId.equals(memberId1),"根据"+userId+"查询，返回的结果"+memberId1);
                Preconditions.checkArgument(userName.equals(memberName1),"根据"+userName+"查询，返回结果"+memberName1);
            }

//                }
//            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员信息列表通过会员id和会员姓名搜索");
        }
    }

    //会员信息列表通过会员id和会员姓名+联系电话筛选
    @Test(dataProvider = "userList1",dataProviderClass = DataProviderMethod.class)
    public void searchMember6(String userId,String userName,String phone) throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list0 = md.MemberList(page,size,userId,userName,phone,null,null).getJSONArray("list");
            if (list0.size()!=0){
                String memberId = list0.getJSONObject(0).getString("member_id");
                String memberName = list0.getJSONObject(0).getString("member_name");
                String memberPhone = list0.getJSONObject(0).getString("phone");
                Preconditions.checkArgument(userId.equals(memberId),"根据"+userId+"查询，返回的结果"+memberId);
                Preconditions.checkArgument(userName.equals(memberName),"根据"+userName+"查询，返回结果"+memberName);
                Preconditions.checkArgument(phone.equals(memberPhone),"根据"+phone+"查询，返回结果"+memberPhone);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员信息列表通过会员id+会员姓名+会员电话搜索");
        }
    }


    //会员信息列表通过会员id和会员姓名+联系电话+人物id筛选
    @Test(dataProvider = "userList2",dataProviderClass = DataProviderMethod.class)
    public void searchMember7(String userId,String userName,String phone,String memberUserId) throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list0 = md.MemberList(page,size,userId,userName,phone,memberUserId,null).getJSONArray("list");
            if (list0.size()!=0){
                String memberId = list0.getJSONObject(0).getString("member_id");
                String memberName = list0.getJSONObject(0).getString("member_name");
                String memberPhone = list0.getJSONObject(0).getString("phone");
                String memberUId = list0.getJSONObject(0).getString("user_id");
                Preconditions.checkArgument(userId.equals(memberId),"根据"+userId+"查询，返回的结果"+memberId);
                Preconditions.checkArgument(userName.equals(memberName),"根据"+userName+"查询，返回结果"+memberName);
                Preconditions.checkArgument(phone.equals(memberPhone),"根据"+phone+"查询，返回结果"+memberPhone);
                Preconditions.checkArgument(memberUId.equals(memberUserId),"根据"+memberUserId+"查询，返回结果"+memberUId);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员信息列表通过会员id+会员姓名+会员电话+人物id搜索");
        }
    }
}

