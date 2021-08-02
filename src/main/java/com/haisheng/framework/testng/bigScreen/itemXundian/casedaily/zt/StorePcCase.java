package com.haisheng.framework.testng.bigScreen.itemXundian.casedaily.zt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.equipmentmanagement.auth.AllDeviceListScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.vouchermanage.VoucherFormVoucherPageScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.punchclockandsignrule.PunchClockAndSignRuleUpdateScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.shop.collection.AddScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.shop.collection.CancelScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.shop.collection.ShopDeviceListScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.shop.device.LiveScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.*;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import static com.google.common.base.Preconditions.checkArgument;

public class StorePcCase extends TestCaseCommon implements TestCaseStd {
    private final EnumTestProduct product = EnumTestProduct.XD_DAILY;
    public VisitorProxy visitor = new VisitorProxy(product);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);
    public static final int page = 1;
    public static final int size = 100;
    public static final int s =(int) (Math.random() * 10000);
    XundianScenarioUtil xd = XundianScenarioUtil.getInstance();
    StoreScenarioUtil md = StoreScenarioUtil.getInstance();
    MendianInfo info = new MendianInfo();
    String name = "zdh";
    String phone = "13614534511";
    String email = "334411@qq.com";
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "周涛";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_DAILY_TEST.getJobName());
        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduct.XD_DAILY.getDesc());
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672", "18513118484", "18810332354", "15084928847"};
        commonConfig.shopId = EnumTestProduct.XD_DAILY.getShopId();
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


    //定检巡查展示图片类型+日期==返回的图片类型+日期
    @Test
    public void picScheduled2(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String start_time = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), -5), "yyyy-MM-dd");
            String end_time = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd");
            int pages = md.picturePage("SCHEDULED",start_time,end_time,"",null,1,8).getInteger("pages");
            for(int i=1;i<=pages;i++){
                JSONArray list = md.picturePage("SCHEDULED",start_time,end_time,"",null,i,8).getJSONArray("list");
                for(int j=0;j<list.size();j++){
                    String tips = list.getJSONObject(j).getString("tips");
                    String date_time = list.getJSONObject(j).getString("date_time");
                    String s = date_time.substring(0,10);
                    int a = s.compareTo(start_time);
                    int b = s.compareTo(end_time);
                    checkArgument(tips.contains("定检巡店"), "定检巡查" + "!=图片返回的类型" + tips);
                    checkArgument(a>=0&&b<=0, "定检巡查+日期" + "!=图片返回日期" + date_time);
                }

            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("定检巡查展示图片类型+日期==返回的图片类型+日期");
        }
    }


    //定检巡查展示图片类型+日期+门店名称+异常==返回的图片类型+日期+门店名称+异常
    @Test
    public void picScheduled3(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String start_time =DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), -5), "yyyy-MM-dd");
            String end_time = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd");
            int pages = md.picturePage("SCHEDULED",start_time,end_time,info.shop_id_01_chin,1,1,8).getInteger("pages");
            for(int i=1;i<=pages;i++){
                JSONArray list = md.picturePage("SCHEDULED",start_time,end_time,info.shop_id_01_chin,1,i,8).getJSONArray("list");
                for(int j=0;j<list.size();j++){
                    String tips = list.getJSONObject(j).getString("tips");
                    String date_time = list.getJSONObject(j).getString("date_time");
                    String title = list.getJSONObject(j).getString("title");
                    int isAbnormal = list.getJSONObject(j).getInteger("is_abnormal");
                    String s = date_time.substring(0,10);
                    int a = s.compareTo(start_time);
                    int b = s.compareTo(end_time);
                    checkArgument(tips.contains("定检巡店"), "定检巡查" + "!=图片返回的类型" + tips);
                    checkArgument(a>=0&&b<=0, "定检巡查+日期" + "!=图片返回日期" + date_time);
                    checkArgument(title.contains(info.shop_id_01_chin), "输入的门店名称" + "!=返回的门店名称" + title);
                    checkArgument(isAbnormal==1, "异常图片" + "!=图片返回的状态" + isAbnormal);
                }

            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("定检巡查展示图片类型+日期+门店名称+异常==返回的图片类型+日期+门店名称+异常");
        }
    }


    //定检巡查展示图片类型+日期+门店名称+非异常==返回的图片类型+日期+门店名称+非异常
    @Test
    public void picScheduled4(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String start_time =DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), -5), "yyyy-MM-dd");
            String end_time = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd");
//            int pictotal = md.picturePage("SCHEDULED",start_time,end_time,"",1,1,8).getInteger("total");
            int pages = md.picturePage("SCHEDULED",start_time,end_time,info.shop_id_01_chin,0,1,8).getInteger("pages");
            for(int i=1;i<=pages;i++){
                JSONArray list = md.picturePage("SCHEDULED",start_time,end_time,info.shop_id_01_chin,0,i,8).getJSONArray("list");
                for(int j=0;j<list.size();j++){
                    String tips = list.getJSONObject(j).getString("tips");
                    String date_time = list.getJSONObject(j).getString("date_time");
                    String title = list.getJSONObject(j).getString("title");
                    int isAbnormal = list.getJSONObject(j).getInteger("is_abnormal");
                    String s = date_time.substring(0,10);
                    int a = s.compareTo(start_time);
                    int b = s.compareTo(end_time);
                    checkArgument(tips.contains("定检巡店"), "定检巡查" + "!=图片返回的类型" + tips);
                    checkArgument(a>=0&&b<=0, "定检巡查+日期" + "!=图片返回日期" + date_time);
                    checkArgument(title.contains(info.shop_id_01_chin), "输入的门店名称" + "!=返回的门店名称" + title);
                    checkArgument(isAbnormal==0, "异常图片" + "!=图片返回的状态" + isAbnormal);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("定检巡查展示图片类型+日期+门店名称+异常==返回的图片类型+日期+门店名称+异常");
        }
    }

    //手动留痕展示图片类型==返回的图片类型
    @Test
    public void picSpot1(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String start_time =DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), -5), "yyyy-MM-dd");
            String end_time = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd");
            int pages = md.picturePage("SPOT",start_time,end_time,"",null,1,8).getInteger("pages");
            for(int i=1;i<=pages;i++){
                JSONArray list = md.picturePage("SPOT",start_time,end_time,"",null,i,8).getJSONArray("list");
                for(int j=0;j<list.size();j++){
                    String tips = list.getJSONObject(j).getString("tips");
                    checkArgument(tips.contains("现场巡店")||tips.contains("远程巡店"), "手动巡查" + "!=图片返回的类型" + tips);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("手动留痕展示图片类型==返回的图片类型");
        }
    }


    //手动展示图片类型+日期+门店名称+非异常==返回的图片类型+日期+门店名称+非异常
    @Test
    public void picSpot4(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String start_time =DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), -5), "yyyy-MM-dd");
            String end_time = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd");
            int pages = md.picturePage("SPOT",start_time,end_time,info.shop_id_01_chin,0,1,8).getInteger("pages");
            for(int i=1;i<=pages;i++){
                JSONArray list = md.picturePage("SPOT",start_time,end_time,info.shop_id_01_chin,0,i,8).getJSONArray("list");
                for(int j=0;j<list.size();j++){
                    String tips = list.getJSONObject(j).getString("tips");
                    String date_time = list.getJSONObject(j).getString("date_time");
                    String title = list.getJSONObject(j).getString("title");
                    int isAbnormal = list.getJSONObject(j).getInteger("is_abnormal");
                    String s = date_time.substring(0,10);
                    int a = s.compareTo(start_time);
                    int b = s.compareTo(end_time);
                    checkArgument(tips.contains("现场巡店")||tips.contains("远程巡店"), "定检巡查" + "!=图片返回的类型" + tips);
                    checkArgument(a>=0&&b<=0, "手动留痕+日期" + "!=图片返回日期" + date_time);
                    checkArgument(title.contains(info.shop_id_01_chin), "输入的门店名称" + "!=返回的门店名称" + title);
                    checkArgument(isAbnormal==0, "异常图片" + "!=图片返回的状态" + isAbnormal);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("手动展示图片类型+日期+门店名称+异常==返回的图片类型+日期+门店名称+异常");
        }
    }


    //客流分析门店搜索
    @Test(dataProvider = "type",dataProviderClass = DataProviderMethod.class)
    public void ShopperTrak(String type_1){
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

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客流分析通过搜索框输入==客流分析搜索出来门店的内容");
        }
    }


//    //门店客户列表通过搜索框搜索门店
    @Test(dataProvider = "type",dataProviderClass = DataProviderMethod.class)
    public void StoreTrak(String type_1){
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
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店客户通过搜索框输入==门店客户搜索出来门店的内容");
        }
    }


    //门店客户列表通过搜索框搜索门店
    @Test(dataProvider = "type",dataProviderClass = DataProviderMethod.class)
    public void NewStoreUser(String type_1){
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
        } catch (AssertionError | Exception e) {
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
    //    非自定义导出报表
    @Test
    public void ReportExport(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = xd.reportList(1,100,"巡店月报",null,"MONTH",null).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                int id = list.getJSONObject(i).getInteger("id");
                int code = xd.customizeReportExport(id,null,null,null,null,null,null).getInteger("code");
//                int code = xd. reportExport(id).getInteger("code");
                Preconditions.checkArgument(code==1000,"非自定义导出报表 id="+id+", 状态码"+code);
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC非自定义导出报表");
        }
    }

    //自定义导出报表
    @Test
    public void ReportExport1(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String start_time =DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), -5), "yyyy-MM-dd");
            String end_time = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd");
            JSONArray list = xd.reportList(1,100,null,null,"CUSTOM",null).getJSONArray("list");
            for (int i=0;i<list.size();i++){
                int id = list.getJSONObject(i).getInteger("id");
                int code = xd.customizeReportExport(id,null,null,null,start_time,end_time,null).getInteger("code");
                Preconditions.checkArgument(code==1000,"非自定义导出报表 id="+id+", 状态码"+code);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC自定义导出报表");
        }
    }

    //
//
//
//    /**
//     * ====================新增账号======================
//     */
    @Test
    public void accountAdd_Phone() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray roleIdList = new JSONArray();
            roleIdList.add(2);
            JSONArray shopIdList = new JSONArray();
            shopIdList.add(4116);
            int status = 1;
            String type = "PHONE";
            //用phone新增一个账号
            JSONObject res = md.organizationAccountAddTwo("",name,"123456","uid_ef6d2de5", type,null, phone,status,roleIdList,shopIdList);
            Integer code = res.getInteger("code");
            Preconditions.checkArgument(code == 1000, "用手机号:" + phone + "新增一个账号失败了");
            //从列表获取刚刚新增的那个账户的名称进行搜获获取她的account
            JSONArray accountList = md.organizationAccountPage(name, "", "", phone, "", "", page, size).getJSONArray("list");
            String account = accountList.getJSONObject(0).getString("account");
            //新建后编辑账号
            JSONObject res1 = md.organizationAccountEditTwo(account,"qqqqq","111","uid_ef6d2de5",type,null,phone,status,roleIdList,shopIdList);
            Integer code2 = res1.getInteger("code");
            Preconditions.checkArgument(code2 == 1000, "用姓名:" + "qqqqq" + "编辑一个账号失败了");
            //新建成功以后删除新建的账号
            Integer code1 = md.organizationAccountDelete(account).getInteger("code");
            Preconditions.checkArgument(code1 == 1000, "删除手机号的账号:" + phone + "失败了");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("用手机号新增账号,编辑账号,删除账号");
        }
    }
    //
    @Test
    public void accountAdd_Email(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray roleIdList = new JSONArray();
            roleIdList.add(2);
            JSONArray shopIdList = new JSONArray();
            shopIdList.add(4116);
            int status = 1;
            String type = "EMAIL";
            //用email新增一个账号
            JSONObject res = md.organizationAccountAddTwo("",name,"123456","uid_ef6d2de5", type,email, null,status,roleIdList,shopIdList);
            Integer code = res.getInteger("code");
            Preconditions.checkArgument(code == 1000, "用邮箱号:" + email + "新增一个账号失败了");
            //从列表获取刚刚新增的那个账户的名称进行搜获获取她的account
            JSONArray accountList = md.organizationAccountPage(name, "", email,"", "", "", page, size).getJSONArray("list");
            String account = accountList.getJSONObject(0).getString("account");
            //新建后编辑账号
            JSONObject res1 = md.organizationAccountEditTwo(account,"qqqqq","111","uid_ef6d2de5",type,"33001@qq.com",null,status,roleIdList,shopIdList);
            Integer code2 = res1.getInteger("code");
            Preconditions.checkArgument(code2 == 1000, "编辑失败:" +res.getString("message") );
            //新建成功以后删除新建的账号
            Integer code1 = md.organizationAccountDelete(account).getInteger("code");
            Preconditions.checkArgument(code1 == 1000, "删邮箱的账号:" + email + "失败了");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("用邮箱号新增账号,编辑账号,删除账号");
        }
    }


    /**
     * ====================新增角色======================
     */
    @Test
    public void role_add() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            String description = "青青测试给店长用的角色";
            JSONArray moduleId = new JSONArray();
            moduleId.add(7);
            moduleId.add(9);
            //新增一个角色
            JSONObject res = md.organizationRoleAddTwo(name,2, description, moduleId);
            Integer code = res.getInteger("code");
            int role_id = md.organizationRolePage(name, page, size).getJSONArray("list").getJSONObject(0).getInteger("role_id");
            checkArgument(code == 1000, "新增角色失败了");
            //编辑角色
            String name1 = "AUTOtest在编辑";
            Integer code1 = md.organizationRoleEditTwo(role_id, 2,name1, description, moduleId).getInteger("code");
            checkArgument(code1 == 1000, "编辑角色的信息失败了");
            //列表中编辑过的角色是否已更新
            JSONArray list1 = md.organizationRolePage(name1, page, size).getJSONArray("list");
            String role_name = list1.getJSONObject(0).getString("role_name");
            checkArgument(name1.equals(role_name), "编辑过的角色没有更新在列表");
            //新建成功以后删除新建的账号
            if (name1.equals(role_name)) {
                Integer code2 = md.organizationRoleDelete(role_id).getInteger("code");
                checkArgument(code2 == 1000, "删除角色:" + role_id + "失败了");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新增删改查角色");
        }
    }


    //会员信息通过会员id筛选
    @Test(dataProvider = "memberId",dataProviderClass = DataProviderMethod.class)
    public void searchMember(String memberId){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.MemberList(page,size,memberId,null,null,null,null).getJSONArray("list");
            for (int j = 0 ; j < list.size();j++) {
                String memId = list.getJSONObject(j).getString("member_id");
                Preconditions.checkArgument(memId.equals(memberId),"根据"+memberId+"查询，结果包含"+memId);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员信息列表通过会员Id搜索");
        }
    }

    //会员信息通过姓名筛选（包阔模糊搜索和精确搜索）
    @Test(dataProvider = "memberName",dataProviderClass = DataProviderMethod.class)
    public void searchMember1(String memberName){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.MemberList(page,size,null,memberName,null,null,null).getJSONArray("list");
            for (int j = 0 ; j < list.size();j++) {
                String memName = list.getJSONObject(j).getString("member_name");
                Preconditions.checkArgument(memName.contains(memberName),"根据"+memberName+"查询，结果包含"+memName);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员信息列表通过会员姓名搜索");
        }
    }


    //会员信息列表通过电话筛选
    @Test(dataProvider = "memberPhone",dataProviderClass = DataProviderMethod.class)
    public void searchMember2(String memberPhone){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.MemberList(page,size,null,null,memberPhone,null,null).getJSONArray("list");
            for (int j = 0 ; j < list.size();j++) {
                String memPhone = list.getJSONObject(j).getString("phone");
                Preconditions.checkArgument(memPhone.contains(memberPhone), "根据" + memberPhone + "查询，结果包含" + memPhone);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员信息列表通过会员电话搜索");
        }
    }

    //会员信息列表通过人物id筛选
    @Test(dataProvider = "userId",dataProviderClass = DataProviderMethod.class)
    public void searchMember3(String userId){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.MemberList(page,size,null,null,null,userId,null).getJSONArray("list");
            for (int j = 0 ; j < list.size();j++) {
                String memUserId = list.getJSONObject(j).getString("user_id");
                Preconditions.checkArgument(memUserId.contains(userId), "根据" + userId + "查询，结果包含" + memUserId);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员信息列表通过人物id搜索");
        }
    }


//    //会员信息列表通过会员身份筛选
//    @Test(dataProvider = "identity",dataProviderClass = DataProviderMethod.class)
//    public void searchMember4(String identity) throws Exception{
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONArray list = md.MemberList(page,size,null,null,null,null,identity).getJSONArray("list");
//            for (int j = 0 ; j < list.size();j++) {
//                String memIdentity = list.getJSONObject(j).getString("identity");
//                Preconditions.checkArgument(memIdentity.contains(identity), "根据" + identity + "查询，结果包含" + memIdentity);
//            }
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("会员信息列表通过会员身份搜索");
//        }
//    }






    //会员信息列表通过会员id和会员姓名筛选
    @Test(dataProvider = "userIdName",dataProviderClass = DataProviderMethod.class)
    public void searchMember5(String userId,String userName ){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list0 = md.MemberList(page,size,userId,userName,null,null,null).getJSONArray("list");
            if (list0.size()!=0){
                String memberId1 = list0.getJSONObject(0).getString("member_id");
                String memberName1 = list0.getJSONObject(0).getString("member_name");
                Preconditions.checkArgument(userId.equals(memberId1),"根据"+userId+"查询，返回的结果"+memberId1);
                Preconditions.checkArgument(userName.equals(memberName1),"根据"+userName+"查询，返回结果"+memberName1);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员信息列表通过会员id和会员姓名搜索");
        }
    }

    //会员信息列表通过会员id和会员姓名+联系电话筛选
    @Test(dataProvider = "userList1",dataProviderClass = DataProviderMethod.class)
    public void searchMember6(String userId,String userName,String phone){
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
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员信息列表通过会员id+会员姓名+会员电话搜索");
        }
    }

    //会员信息列表通过会员id和会员姓名+联系电话+人物id筛选
    @Test(dataProvider = "userList2",dataProviderClass = DataProviderMethod.class)
    public void searchMember7(String userId,String userName,String phone,String memberUserId){
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

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员信息列表通过会员id+会员姓名+会员电话+人物id搜索");
        }
    }

    //注册会员的异常情况
    @Test(dataProvider = "FACE_URL",dataProviderClass = DataProviderMethod.class)
    public void regMemError(String face_url){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //注册会员时会员id重复也成功
            String base64 = MendianInfo.getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/common/multimedia/picture/女人脸.jpg");
            String path = md.checkPic(base64).getString("pic_path");
            JSONObject res0 = md.RegisterMember1(null,path,"uid_2cd5f8b4","qq11啊","13656788899","1998-10-01",null,130);
            Preconditions.checkArgument(res0.getString("message").equals("当前会员ID已经存在！"), "注册会员重复会员id也成功了message"+res0.getString("message"));

            //注册会员时会员电话重复
            JSONObject res1 = md.RegisterMember1(null,path,"212","这是一个二","13604609869","1998-10-01",null,130);
            Preconditions.checkArgument(res1.getString("message").equals("当前手机号已经存在！"), "注册会员时电话重复也注册成功了返回的message"+res1.getString("message"));
//
            //注册会员时会员名称过长
            JSONObject res2 = md.RegisterMember1(null,path,"213","这是一个二十一字的名字我也不知道怎么说就是123213","13656788899","1998-10-01",null,130);
            Preconditions.checkArgument(res2.getInteger("code") != 1000, "注册会员时名字长度超过也注册成功了返回的message"+res2.getString("message"));
//
            //注册会员时电话是12位
            JSONObject res3 = md.RegisterMember1(null,path,"214","啊","136567888991","1998-10-01",null,130);
            Preconditions.checkArgument(res3.getInteger("code") != 1000, "注册会员时电话12位也注册成功了返回的message"+res3.getString("message"));
//
            //注册会员时电话是10位
            JSONObject res4 = md.RegisterMember1(null,path,"215","啊1","1365678889","1998-10-01",null,130);
            Preconditions.checkArgument(res4.getInteger("code") != 1000, "注册会员时电话10位也注册成功了返回的message"+res4.getString("message"));
//
            //注册会员时电话有英文中文特殊字符
            JSONObject res5 = md.RegisterMember1(null,face_url,"216","啊2","1365678￥啊a89","1998-10-01",null,130);
            Preconditions.checkArgument(res5.getInteger("code") != 1000, "注册会员时电话有英文等也注册成功了返回的message"+res5.getString("message"));
//
            //注册会员时选择未来日期
            JSONObject res6 = md.RegisterMember1(null,path,"217","啊3","1365678889","2998-10-01",null,130);
            Preconditions.checkArgument(res6.getInteger("code") != 1000, "注册会员时选择未来日期message"+res6.getString("message"));
//
            //注册会员时全都为空
            JSONObject res7 = md.RegisterMember1(null,null,null,null,null,null,null,0);
            Preconditions.checkArgument(res7.getInteger("code") != 1000, "注册会员时全都为空"+res7.getString("message"));
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("注册会员的异常情况");
        }
    }


    //添加会员身份的异常情况
    @Test()
    public void identity(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //身份名称为空
            JSONObject res = md.AddMember1(null);
            Preconditions.checkArgument(res.getInteger("code")==1001, "身份名称为空也添加成功"+res.getInteger("code"));

            //身份名称过长
            JSONObject res1 = md.AddMember1("11111111111111111111111111111111111");
            Preconditions.checkArgument(res1.getInteger("code")==1009, "身份名称过长也添加成功"+res.getInteger("code"));

            //身份名称重复
            JSONObject res2 = md.AddMember1("VIP");
            Preconditions.checkArgument(res2.getInteger("code")==1001, "身份名称重复也添加成功"+res.getInteger("code"));
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加会员身份特殊情况");
        }
    }



    //设备管理，查看设备
    @Test(dataProvider = "device_id",dataProviderClass = DataProviderMethod.class)
    public void deviceDetail(String deviceid,String devicename){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int code = md.cameraDetail(null,deviceid).getInteger("code");
            Preconditions.checkArgument(code == 1000, "查询失败,设备名称:"+devicename+"  设备ID:"+deviceid + "code :"+code);
//            Preconditions.checkArgument(status_name.equals("运行中") , "salesdemo门店的直播报错了,设备名称:"+device_name+"  设备ID:"+device_id + "摄像头状态 :"+status_name);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("设备管理编辑云台设备");
        }
    }

    //查看预置位
    @Test(dataProvider = "device_id",dataProviderClass = DataProviderMethod.class)
    public void presetList(String deviceid,String devicename){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int code = md. cameraList(43072,deviceid,"PRESET").getInteger("code");
            Preconditions.checkArgument(code == 1000, "查询预置位失败,设备名称:"+devicename+"  设备ID:"+deviceid + "code :"+code);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("查询预置位");
        }
    }

    //查看看守位
    @Test(dataProvider = "device_id",dataProviderClass = DataProviderMethod.class)
    public void guardList(String deviceid,String devicename){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int code = md. cameraList(43072,deviceid,"GUARD").getInteger("code");
            Preconditions.checkArgument(code == 1000, "查询看守位失败,设备名称:"+devicename+"  设备ID:"+deviceid + "code :"+code);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("查询看守位");
        }
    }

    //新建预置位,删除一个预置位
    @Test(dataProvider = "device_id",dataProviderClass = DataProviderMethod.class)
    public void createPreset0(String deviceid,String devicename){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建一个预置位
            String name = "预置位_" + CommonUtil.getRandom(2);
            JSONObject res1 = md.creatPreset(43072,deviceid,name,"60");
            Preconditions.checkArgument(res1.getInteger("code")==1000, "创建不成功" + res1.getString("message")+"设备名称:"+devicename+" 设备ID:"+deviceid);
            //获取创建预置位后的预置位列表
            JSONObject data1 = md.cameraList(43072,deviceid,"PRESET").getJSONObject("data");
            JSONArray list1 = data1.getJSONArray("list");
            //获取最后一个预置位的preset_index
            int preset_index =list1.getJSONObject(list1.size()-1).getInteger("preset_index");
            //删除最后一个预置位
            JSONObject res3 = md.deletePreset(43072,deviceid,preset_index);
            Preconditions.checkArgument(res3.getInteger("code")==1000, "删除不成功" + res3.getString("message"));
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建预置位、删除预置位");
        }
    }

    //新建看守位、删除看守位
    @Test()
    public void createGuard0(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建一个看守位
            JSONObject res1 = md.Guard(43072,"8097818264503296");
            Preconditions.checkArgument(res1.getInteger("code")==1000, "创建不成功" + res1.getString("message"));
            JSONObject res = md.dyGuard(43072,"8097818264503296");
            checkArgument(res.getInteger("code")==1000, "调用不成功原因" + res.getString("message"));
            //获取创建预置位后的预置位列表
            JSONObject data1 = md.cameraList(43072,"8097818264503296","GUARD").getJSONObject("data");
            JSONArray list1 = data1.getJSONArray("list");
            //获取最后一个预置位的preset_index
            int preset_index =list1.getJSONObject( list1.size()-1).getInteger("preset_index");
            //删除最后一个预置位
            JSONObject res3 = md.deletePreset(43072,"8097818264503296",preset_index);
            Preconditions.checkArgument(res3.getInteger("code")==1000, "删除不成功" + res3.getString("message"));
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建看守位、调用看守位、删除看守位");
        }
    }


    //调用预置位
    @Test(dataProvider = "device_id",dataProviderClass = DataProviderMethod.class)
    public void backPreset(String deviceid,String devicename){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取预置位列表
            JSONObject data = md.cameraList(43072,deviceid,"PRESET").getJSONObject("data");
            boolean status2 = data.getBooleanValue("status");
            if(status2){
                JSONArray list = data.getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    int index = list.getJSONObject(i).getInteger("preset_index");
                    JSONObject res = md.dyPreset(43072,deviceid,index);
                    Preconditions.checkArgument(res.getInteger("code")==1000, "调用不成功原因" + res.getString("message")+"设备名称:"+devicename+" 设备ID:"+deviceid);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("调用预置位 ");
        }
    }

    @Test(description = "[视频监控]运行中状态下展示的设备全部都是可用的,1")
    public void videoAvailable1(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取视频监控中进行中设备列表
            JSONArray list = AllDeviceListScene.builder().available(1).build().invoke(visitor,true).getJSONArray("list");
            for(int i = 0; i<list.size() ; i++){
                JSONArray device_list = list.getJSONObject(i).getJSONArray("device_list");
                if(device_list.size()>0){
                    for (int j = 0; j<device_list.size(); j++){
                        int available = device_list.getJSONObject(j).getInteger("available");
                        String device_name = device_list.getJSONObject(j).getString("device_name");
                        Preconditions.checkArgument(available==1,"视频监控中的选择运行中状态:"+available+" 列表中设备:"+device_name+"不是运行中状态");
                    }
                }
            }
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("[视频监控]运行中状态下展示的设备全部都是可用的,1");
        }
    }

    @Test(description = "[视频监控]不可用状态下展示的设备全部都是不可用的,0")
    public void videoAvailable0(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取视频监控中进行中设备列表
            JSONArray list = AllDeviceListScene.builder().available(0).build().invoke(visitor,true).getJSONArray("list");
            for(int i = 0; i<list.size() ; i++){
                JSONArray device_list = list.getJSONObject(i).getJSONArray("device_list");
                if(device_list.size()>0){
                    for (int j = 0; j<device_list.size(); j++){
                        int available = device_list.getJSONObject(j).getInteger("available");
                        String device_name = device_list.getJSONObject(j).getString("device_name");
                        Preconditions.checkArgument(available==0,"视频监控中的选择运行中状态:"+available+" 列表中设备:"+device_name+"不是运行中状态");
                    }
                }
            }
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("[视频监控]不可用状态下展示的设备全部都是不可用的,0");
        }
    }

    @Test(description = "[视频监控]收藏栏下的门店全部都是以收藏的")
    public void collectionShop(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
        JSONArray list = ShopDeviceListScene.builder().build().invoke(visitor,true).getJSONArray("list");
        for (int i = 0; i<list.size(); i++){
            boolean collected = list.getJSONObject(i).getBoolean("collected");
            String subject_name = list.getJSONObject(i).getString("subject_name");
            Preconditions.checkArgument(collected,"收藏门店列表下此门店"+subject_name+"不是收藏门店");
        }
        }catch (AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally {
            saveData("[视频监控]收藏栏下的门店全部都是以收藏的");
        }
    }
    @Test(description = "[视频监控]收藏门店")
    public void addCollectedShop(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray list = AllDeviceListScene.builder().build().invoke(visitor,true).getJSONArray("list");
            for(int i = 0; i<list.size(); i++){
                boolean collected = list.getJSONObject(i).getBoolean("collected");
                if(!collected){
                    Long subject_id = list.getJSONObject(i).getLong("subject_id");
                    String subject_name = list.getJSONObject(i).getString("subject_name");
                    String message = AddScene.builder().id(subject_id).build().invoke(visitor,false).getString("message");
                    Preconditions.checkArgument(message.equals("success"),"收藏门店失败,门店名称"+subject_name);
                }
            }
        }catch (AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally {
            saveData("[视频监控]收藏门店");
        }
    }

    @Test(description = "[视频监控]取消收藏门店")
    public void cancelCollectionShop(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray list = ShopDeviceListScene.builder().build().invoke(visitor,true).getJSONArray("list");
            for(int i = 0; i<list.size(); i++){
                Long subject_id = list.getJSONObject(i).getLong("subject_id");
                String subject_name = list.getJSONObject(i).getString("subject_name");
                String message = CancelScene.builder().id(subject_id).build().invoke(visitor,false).getString("message");
                Preconditions.checkArgument(message.equals("success"),"取消收藏门店失败，门店名称"+subject_name);
            }
        }catch (AssertionError|Exception e){
            appendFailReason(e.toString());
        }
        saveData("[视频监控]取消收藏门店");
    }

    @Test(description = "[视频监控]门店名称筛选框")
    public void authAllDeviceList(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray list = AllDeviceListScene.builder().build().invoke(visitor,true).getJSONArray("list");
            for (int i=0; i<list.size() ; i++){
                String subject_name = list.getJSONObject(i).getString("subject_name");
                JSONArray shop_list = AllDeviceListScene.builder().shopName(subject_name).build().invoke(visitor,true).getJSONArray("list");
                for (int j=0; j<shop_list.size() ; j++){
                    String shop_name = shop_list.getJSONObject(j).getString("subject_name");
                    Preconditions.checkArgument(shop_name.equals(subject_name),"筛选框输入门店名称"+subject_name+"列表展示出的门店名称"+shop_name);
                }
            }
        }catch (AssertionError|Exception e){
            appendFailReason(e.toString());
        }
        saveData("[视频监控]门店名称筛选框");
    }

    @Test(description = "[视频监控]播放视频")
    public void deviceLive(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = AllDeviceListScene.builder().build().invoke(visitor,true).getJSONArray("list");
            for (int i = 0; i<list.size() ; i++){
                JSONArray device_list = list.getJSONObject(i).getJSONArray("device_list");
                Long shopId = list.getJSONObject(i).getLong("subject_id");
                String shopName = list.getJSONObject(i).getString("subject_name");
                for(int j = 0;j<device_list.size();j++){
                    int available = device_list.getJSONObject(j).getInteger("available");
                    String device_id = device_list.getJSONObject(j).getString("device_id");
                    if(available==1){
                        String message = LiveScene.builder().deviceId(device_id).shopId(shopId).build().invoke(visitor,false).getString("message");
                        Preconditions.checkArgument(message.equals("success"),"播放视频失败，设备id"+device_id+"门店名称"+shopName);
                    }
                }
            }
        }catch (AssertionError|Exception e){
            appendFailReason(e.toString());
        }
        saveData("[视频监控]播放视频");
    }

    @Test(dataProvider = "updatetype" , dataProviderClass = DataProviderMethod.class,description = "签到-打卡-自然月、自然周，累计天数和连续天数初始配置")
    public void punchclockandsignrule(String date,String punch){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray voucher_list = VoucherFormVoucherPageScene.builder()
                    .voucherStatus("WORKING").build().invoke(visitor, true).getJSONArray("list");
                long voucher_id = voucher_list.getJSONObject(0).getLong("id");
                String[] types = {"TOTAL","CONTINUE"};
                Arrays.stream(types).forEach(type -> {
                    JSONObject res = PunchClockAndSignRuleUpdateScene.builder()
                            .cycleType(date)
                            .initIntegral(s)
                            .punchOrSignType(punch)
                            .type(type)
                            .initCouponId(voucher_id)
                            .build()
                            .invoke(visitor, false);
                    Preconditions.checkArgument(res.getInteger("code").equals(1000), "新建初始奖励失败，失败原因" + res.getString("message"));
                });
        }catch (AssertionError | Exception e){
            appendFailReason(e.toString());
        }
        saveData("签到-打卡-自然月、自然周，累计天数和连续天数初始配置");
    }


    @Test(dataProvider = "updatetype" , dataProviderClass = DataProviderMethod.class,description = "签到-打卡-天数设置")
    public void punchclockandsignrule1(String date,String punch){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray voucher_list = VoucherFormVoucherPageScene.builder()
                    .voucherStatus("WORKING").build().invoke(visitor, true).getJSONArray("list");
            long voucher_id = voucher_list.getJSONObject(0).getLong("id");
            String[] types = {"TOTAL","CONTINUE"};
            Arrays.stream(types).forEach(type -> {
                JSONObject res = PunchClockAndSignRuleUpdateScene.builder()
                        .cycleType(date)
                        .initIntegral(s)
                        .punchOrSignType(punch)
                        .type(type)
                        .ruleDaysList(util.getRuledays())
                        .initCouponId(voucher_id)
                        .build()
                        .invoke(visitor, false);
                Preconditions.checkArgument(res.getInteger("code").equals(1000), "新建初始奖励失败，失败原因" + res.getString("message"));
            });
        }catch (AssertionError | Exception e){
            appendFailReason(e.toString());
        }
        saveData("签到-打卡-天数设置");
    }

    @Test(dataProvider = "updatetype" , dataProviderClass = DataProviderMethod.class,description = "签到-打卡-固定周期")
    public void punchclockandsignrule2(String date,String punch){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray voucher_list = VoucherFormVoucherPageScene.builder()
                    .voucherStatus("WORKING").build().invoke(visitor, true).getJSONArray("list");
            long voucher_id = voucher_list.getJSONObject(0).getLong("id");
            String[] types = {"TOTAL","CONTINUE"};
            Arrays.stream(types).forEach(type -> {
                JSONObject res = PunchClockAndSignRuleUpdateScene.builder()
                        .cycleType(date)
                        .initIntegral(s)
                        .punchOrSignType(punch)
                        .type(type)
                        .ruleDaysList(util.getRuledays())
                        .anniversaryList(util.fixed())
                        .initCouponId(voucher_id)
                        .build()
                        .invoke(visitor, false);
                Preconditions.checkArgument(res.getInteger("code").equals(1000), "新建初始奖励失败，失败原因" + res.getString("message"));
            });
        }catch (AssertionError | Exception e){
            appendFailReason(e.toString());
        }
        saveData("签到-打卡-固定周期");
    }

    @Test(dataProvider = "updatetype" , dataProviderClass = DataProviderMethod.class,description = "签到-打卡-动态周期")
    public void punchclockandsignrule3(String date,String punch){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray voucher_list = VoucherFormVoucherPageScene.builder()
                    .voucherStatus("WORKING").build().invoke(visitor, true).getJSONArray("list");
            long voucher_id = voucher_list.getJSONObject(0).getLong("id");
            String[] types = {"TOTAL","CONTINUE"};
            Arrays.stream(types).forEach(type -> {
                JSONObject res = PunchClockAndSignRuleUpdateScene.builder()
                        .cycleType(date)
                        .initIntegral(s)
                        .punchOrSignType(punch)
                        .type(type)
                        .ruleDaysList(util.getRuledays())
                        .anniversaryList(util.dynamic())
                        .initCouponId(voucher_id)
                        .build()
                        .invoke(visitor, false);
                Preconditions.checkArgument(res.getInteger("code").equals(1000), "新建初始奖励失败，失败原因" + res.getString("message"));
            });
        }catch (AssertionError | Exception e){
            appendFailReason(e.toString());
        }
        saveData("签到-打卡-动态周期");
    }

    @Test(dataProvider = "updatetype" , dataProviderClass = DataProviderMethod.class,description = "签到-打卡-周期日期-月")
    public void punchclockandsignrule4(String date,String punch){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray voucher_list = VoucherFormVoucherPageScene.builder()
                    .voucherStatus("WORKING").build().invoke(visitor, true).getJSONArray("list");
            long voucher_id = voucher_list.getJSONObject(0).getLong("id");
            String[] types = {"TOTAL","CONTINUE"};
            Arrays.stream(types).forEach(type -> {
                JSONObject res = PunchClockAndSignRuleUpdateScene.builder()
                        .cycleType(date)
                        .initIntegral(s)
                        .punchOrSignType(punch)
                        .type(type)
                        .ruleDaysList(util.getRuledays())
                        .anniversaryList(util.cyclemonth())
                        .initCouponId(voucher_id)
                        .build()
                        .invoke(visitor, false);
                Preconditions.checkArgument(res.getInteger("code").equals(1000), "新建初始奖励失败，失败原因" + res.getString("message"));
            });
        }catch (AssertionError | Exception e){
            appendFailReason(e.toString());
        }
        saveData("签到-打卡-周期日期-月");
    }

    @Test(dataProvider = "updatetype" , dataProviderClass = DataProviderMethod.class,description = "签到-打卡-周期日期-周")
    public void punchclockandsignrule5(String date,String punch){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray voucher_list = VoucherFormVoucherPageScene.builder()
                    .voucherStatus("WORKING").build().invoke(visitor, true).getJSONArray("list");
            long voucher_id = voucher_list.getJSONObject(0).getLong("id");
            String[] types = {"TOTAL","CONTINUE"};
            Arrays.stream(types).forEach(type -> {
                JSONObject res = PunchClockAndSignRuleUpdateScene.builder()
                        .cycleType(date)
                        .initIntegral(s)
                        .punchOrSignType(punch)
                        .type(type)
                        .ruleDaysList(util.getRuledays())
                        .anniversaryList(util.cycleweek())
                        .initCouponId(voucher_id)
                        .build()
                        .invoke(visitor, false);
                Preconditions.checkArgument(res.getInteger("code").equals(1000), "新建初始奖励失败，失败原因" + res.getString("message"))
                ;
            });
        }catch (AssertionError | Exception e){
            appendFailReason(e.toString());
        }
        saveData("签到-打卡-周期日期-周");
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




