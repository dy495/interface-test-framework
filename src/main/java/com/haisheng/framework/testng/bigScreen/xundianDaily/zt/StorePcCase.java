package com.haisheng.framework.testng.bigScreen.xundianDaily.zt;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.xundianDaily.MendianInfo;
import com.haisheng.framework.testng.bigScreen.xundianDaily.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.XundianScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.StorePcAndAppData;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkArgument;

public class StorePcCase extends TestCaseCommon implements TestCaseStd {
    public static final Logger log = LoggerFactory.getLogger(StorePcAndAppData.class);
    public static final int page = 1;
    public static final int size = 100;
    XundianScenarioUtil xd = XundianScenarioUtil.getInstance();
    StoreScenarioUtil md = StoreScenarioUtil.getInstance();
    MendianInfo info = new MendianInfo();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.QQ.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_DAILY_TEST.getJobName());
        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.XUNDIAN_DAILY.getDesc());
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672", "18513118484", "18810332354", "15084928847"};
        commonConfig.shopId = EnumTestProduce.XUNDIAN_DAILY.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("xundian " + xd);
        xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
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
            JSONArray list = md.customerFlowList("110000",type,info.shop_id_01_chin,"徐鹏",null,null,1,10,null).getJSONArray("list");
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
                checkArgument(manager_name.equals("徐鹏"), "输入的门店负责人" + "徐鹏" + "!=搜索出来门店的负责人" + manager_name);
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
            JSONArray list = md.shopPageMemberV3("110000",type,info.shop_id_01_chin,"徐鹏",null,null,1,10).getJSONArray("list");
            int total = md.shopPageMemberV3("110000",type,info.shop_id_01_chin,"徐鹏",null,null,1,10).getInteger("total");
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
                    checkArgument(manager_name.equals("徐鹏"), "输入的门店负责人" + "徐鹏" + "!=搜索出来门店的负责人" + manager_name);
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
            JSONArray list = md.NewUser("110000",type,info.shop_id_01_chin,"徐鹏",null,null,1,10).getJSONArray("list");
            int total = md.NewUser("110000",type,info.shop_id_01_chin,"徐鹏",null,null,1,10).getInteger("total");
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
                    checkArgument(manager_name.equals("徐鹏"), "输入的门店负责人" + "徐鹏" + "!=搜索出来门店的负责人" + manager_name);
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



    //会员到访列表中的人物id，可以在会员信息列表中搜索出来
//    @Test
//    public void MemUserId ()throws Exception{
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //会员身份列表页获取人物id
//            JSONArray list = md.MemberVisit("","","").getJSONArray("list");
//            String userid = list.getJSONObject(0).getString("user_id");
//            JSONArray list1 = md.MemberList(page,size,"","","",userid,"").getJSONArray("list");
//            String userId = list1.getJSONObject(0).getString("user_id");
//            checkArgument(userid.equals(userId), "会员到访页人物id"+userid+"进行搜索"+"会员信息列表页搜索出的"+userId);
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("会员到访列表中的人物id在会员信息页");
//        }
//    }

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

//    //会员信息列表通过会员id和会员姓名+联系电话+人物id+会员身份筛选
//    @Test()
//    public void searchMember8() throws Exception{
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONArray list = md.MemberList(page,size,"","","","","").getJSONArray("list");
//            if (list.size()!=0){
//                String memberId = list.getJSONObject(0).getString("member_id");
//                String memberName = list.getJSONObject(0).getString("member_name");
//                String memberPhone = list.getJSONObject(0).getString("phone");
//                String memberUserId = list.getJSONObject(0).getString("user_id");
//                String memberidentity = list.getJSONObject(0).getString("identity");
//                JSONArray list0 = md.MemberList(page,size,memberId,memberName,memberPhone,memberUserId,memberidentity).getJSONArray("list");
//                for(int i=0;i<list0.size();i++){
//                    String memberId1 = list0.getJSONObject(i).getString("member_id");
//                    String memberName1 = list0.getJSONObject(i).getString("member_name");
//                    String memberPhone1 = list0.getJSONObject(i).getString("phone");
//                    String memberUserId1 = list0.getJSONObject(i).getString("user_id");
//                    String memberidentity1 = list.getJSONObject(i).getString("identity");
//                    Preconditions.checkArgument(memberId.equals(memberId1),"根据"+memberId+"查询，返回的结果"+memberId1);
//                    Preconditions.checkArgument(memberName.equals(memberName1),"根据"+memberName+"查询，返回结果"+memberName1);
//                    Preconditions.checkArgument(memberPhone.equals(memberPhone1),"根据"+memberPhone+"查询，返回结果"+memberPhone1);
//                    Preconditions.checkArgument(memberUserId.equals(memberUserId1),"根据"+memberUserId+"查询，返回结果"+memberUserId1);
//                    Preconditions.checkArgument(memberidentity.equals(memberidentity1),"根据"+memberidentity+"查询，返回结果"+memberidentity1);
//                }
//
////                Preconditions.checkArgument(memberId.equals(memberId1) && memberName.equals(memberName1) && memberPhone.equals(memberPhone1) && memberUserId.equals(memberUserId1)&&memberidentity.equals(memberidentity1)
////                        ,"根据会员id="+memberId+"&&会员姓名="+memberName+"&&会员电话="+memberPhone+"&&人物id"+memberUserId+"&&人物身份"+memberidentity+"查询，结果包含门店名称="+reportresult+"&&报表类型="+reporttype);
//            }
//
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("会员信息列表通过会员id+会员姓名+会员电话+人物id+会员身份搜索");
//        }
//    }

//    //会员到访列表通过user_id查询
//    @Test(dataProvider = "visitId",dataProviderClass = DataProviderMethod.class)
//    public void memberVisits(String visitId) throws Exception{
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONArray list = md.MemberVisit(visitId,"","").getJSONArray("list");
//            for(int i=0;i<list.size();i++){
//                String userId = list.getJSONObject(i).getString("user_id");
//                Preconditions.checkArgument(userId.equals(visitId),"根据"+visitId+"查询，返回的结果"+userId);
//            }
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("会员到访列表通过user_id查询");
//        }
//    }
//
//    //会员到访列表通过name查询
//    @Test(dataProvider = "visitName",dataProviderClass = DataProviderMethod.class)
//    public void memberVisits1(String visitName) throws Exception{
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONArray list = md.MemberVisit("",visitName,"").getJSONArray("list");
//            for(int i=0;i<list.size();i++){
//                String userName = list.getJSONObject(i).getString("name");
//                Preconditions.checkArgument(userName.equals(visitName),"根据"+visitName+"查询，返回的结果"+userName);
//            }
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("会员到访列表通过name查询");
//        }
//    }
//
//    //会员到访列表通过shop_name查询
//    @Test(dataProvider = "visitShopName",dataProviderClass = DataProviderMethod.class)
//    public void memberVisits2(String visitShopName) throws Exception{
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONArray list = md.MemberVisit("","",visitShopName).getJSONArray("list");
//            for(int i=0;i<list.size();i++){
//                String shopName = list.getJSONObject(i).getString("shop_name");
//                Preconditions.checkArgument(shopName.equals(visitShopName),"根据"+visitShopName+"查询，返回的结果"+shopName);
//            }
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("会员到访列表通过shop_name查询");
//        }
//    }
//
//
//    //会员到访列表通过user_id+name查询
//    @Test()
//    public void memberVisits3() throws Exception{
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONArray list = md.MemberVisit("","","").getJSONArray("list");
//            if(list.size()!=0){
//                String userId = list.getJSONObject(0).getString("user_id");
//                String name = list.getJSONObject(0).getString("name");
//                JSONArray list0 = md.MemberVisit(userId,name,"").getJSONArray("list");
//                for(int i=0;i<list0.size();i++){
//                    String userId1 = list.getJSONObject(0).getString("user_id");
//                    String Name = list0.getJSONObject(i).getString("name");
//                    Preconditions.checkArgument(userId1.equals(userId),"根据"+userId+"查询，返回的结果"+userId1);
//                    Preconditions.checkArgument(Name.equals(name),"根据"+name+"查询，返回的结果"+Name);
//                }
//            }
//
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("会员到访列表通过user_id+name查询");
//        }
//    }
//
//    //会员到访列表通过user_id+name+shop_name查询
//    @Test()
//    public void memberVisits4() throws Exception{
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONArray list = md.MemberVisit("","","").getJSONArray("list");
//            if(list.size()!=0){
//                String userId = list.getJSONObject(0).getString("user_id");
//                String name = list.getJSONObject(0).getString("name");
//                String shopName = list.getJSONObject(0).getString("shop_name");
//                JSONArray list0 = md.MemberVisit(userId,name,shopName).getJSONArray("list");
//                for(int i=0;i<list0.size();i++){
//                    String userId1 = list.getJSONObject(i).getString("user_id");
//                    String Name = list0.getJSONObject(i).getString("name");
//                    String shopName1 = list.getJSONObject(i).getString("shop_name");
//                    Preconditions.checkArgument(userId1.equals(userId),"根据"+userId+"查询，返回的结果"+userId1);
//                    Preconditions.checkArgument(Name.equals(name),"根据"+name+"查询，返回的结果"+Name);
//                    Preconditions.checkArgument(shopName.equals(shopName1),"根据"+shopName+"查询，返回的结果"+shopName1);
//                }
//            }
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("会员到访列表通过user_id+name+shop_name查询");
//        }
//    }
}




