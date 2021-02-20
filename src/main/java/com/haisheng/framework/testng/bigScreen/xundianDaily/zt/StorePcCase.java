package com.haisheng.framework.testng.bigScreen.xundianDaily.zt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.haisheng.framework.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkArgument;

public class StorePcCase extends TestCaseCommon implements TestCaseStd {
    public static final Logger log = LoggerFactory.getLogger(StorePcAndAppData.class);
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
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.MENDIAN_DAILY.getDesc());
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672", "18513118484", "18810332354", "15084928847"};
        commonConfig.shopId = EnumTestProduce.MENDIAN_DAILY.getShopId();
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
    @Test(dataProvider = "type")
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
    @Test(dataProvider = "type")
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
                    checkArgument(district_code.contains("110000"), "选择的地理位置" + district_code + "!=搜索出来门店展示的地理位置" + district_name);
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
    @Test(dataProvider = "type")
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
            saveData("新增客户通过搜索框输入==新增客户搜索出来门店的内容");
        }
    }

    @DataProvider(name = "type")
    public Object[] type_1() {

        return new String[][]{
                {"NORMAL"},
                {"COMMUNITY"},
                {"PLAZA"},
                {"FLAGSHIP"},

        };
    }


}




