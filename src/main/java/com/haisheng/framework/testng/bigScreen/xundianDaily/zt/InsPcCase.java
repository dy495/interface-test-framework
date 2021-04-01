package com.haisheng.framework.testng.bigScreen.xundianDaily.zt;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.xundianDaily.MendianInfo;
import com.haisheng.framework.testng.bigScreen.xundianDaily.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.WechatScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.XundianScenarioUtil;
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
import java.util.ArrayList;
import java.util.List;

public class InsPcCase extends TestCaseCommon implements TestCaseStd {
//    public static final Logger log = LoggerFactory.getLogger(StorePcAndAppData.class);
    public static final int page = 1;
    public static final int size = 100;
    XundianScenarioUtil xd = XundianScenarioUtil.getInstance();
    StoreScenarioUtil md = StoreScenarioUtil.getInstance();
    WechatScenarioUtil wx = WechatScenarioUtil.getInstance();
    MendianInfo info = new MendianInfo();


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
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.INS_DAILY.getDesc());
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672", "18513118484", "18810332354", "15084928847"};
        commonConfig.shopId = EnumTestProduce. INS_DAILY.getShopId();
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


    /**
     * -------------------------------------INS相关---------------------------------------------------------------------------
     */


//按照门店名称搜索门店
    @Test()
    public void shopSearch() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String shopName = "门店";
            JSONArray shopList = md.searchShop(shopName,null,null,null,1,10).getJSONArray("list");
            if(shopList!=null)
                for (int i=0; i<shopList.size() ;i++){
                    String shop_name = shopList.getJSONObject(i).getString("shop_name");
                    Preconditions.checkArgument(shop_name.equals(shopName), "通过" + shopName +"搜索门店展示结果为" + shop_name);
                }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("按照门店名称搜索门店");
        }
    }

    //按照联系人搜索门店
    @Test()
    public void shopSearch0() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String managerName = "张三丰";
            JSONArray shopList = md.searchShop(null,managerName,null,null,1,10).getJSONArray("list");
            if(shopList!=null)
                for (int i=0; i<shopList.size() ;i++) {
                    String manager_name = shopList.getJSONObject(i).getString("manager_name");
                    Preconditions.checkArgument(manager_name.equals(managerName), "通过" + managerName + "搜索门店展示结果为" + manager_name);
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("按照联系人搜索门店");
        }
    }

    //按照城市搜索门店
    @Test()
    public void shopSearch1() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String cityName = "北京";
            JSONArray shopList = md.searchShop(null,null,cityName,null,1,10).getJSONArray("list");
            if(shopList!=null)
                for (int i=0; i<shopList.size() ;i++) {
                    String city_name = shopList.getJSONObject(i).getString("city");
                    Preconditions.checkArgument(city_name.equals(cityName), "通过" + cityName + "搜索门店展示结果为" + city_name);
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("按照城市搜索门店");
        }
    }

    //按照开启状态搜索门店
    @Test()
    public void shopSearch2() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            boolean status = true;
            JSONArray shopList = md.searchShop(null,null,null,status,1,10).getJSONArray("list");
            if(shopList!=null)
                for (int i=0; i<shopList.size() ;i++) {
                    boolean status1 = shopList.getJSONObject(i).getBoolean("status");
                    Preconditions.checkArgument(status1==status, "通过" + status + "搜索门店展示结果为" + status1);
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("按照开启状态搜索门店");
        }
    }

    //按照关闭状态搜索门店
    @Test()
    public void shopSearch3() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            boolean status = false;
            JSONArray shopList = md.searchShop(null,null,null,status,1,10).getJSONArray("list");
            if(shopList!=null)
                for (int i=0; i<shopList.size() ;i++) {
                    boolean status1 = shopList.getJSONObject(i).getBooleanValue("status");
                    Preconditions.checkArgument(status1==status, "通过" + status + "搜索门店展示结果为" + status1);
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("按照关闭状态搜索门店");
        }
    }

    //按照门店名称和门店联系人搜索门店
    @Test()
    public void shopSearch4() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String shopName = "门店";
            String managerName = "张三丰";
            JSONArray shopList = md.searchShop(shopName,managerName,null,null,1,10).getJSONArray("list");
            if(shopList!=null)
                for (int i=0; i<shopList.size() ;i++) {
                    String shop_name = shopList.getJSONObject(i).getString("shop_name");
                    String manager_name = shopList.getJSONObject(i).getString("manager_name");
                    Preconditions.checkArgument(shop_name.equals(shopName)&&manager_name.equals(managerName), "通过" + shopName +"和"+ managerName+"搜索门店展示结果为" + shop_name+"和"+manager_name);
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("按照门店名称和门店联系人搜索门店");
        }
    }

    //按照门店名称和门店联系人和城市搜索门店
    @Test()
    public void shopSearch5() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String shopName = "门店";
            String managerName = "张三丰";
            String cityName = "北京";
            JSONArray shopList = md.searchShop(shopName, managerName, cityName, null, 1, 10).getJSONArray("list");
            if (shopList != null)
                for (int i = 0; i < shopList.size(); i++) {
                    String shop_name = shopList.getJSONObject(i).getString("shop_name");
                    String manager_name = shopList.getJSONObject(i).getString("manager_name");
                    String city = shopList.getJSONObject(i).getString("city");
                    Preconditions.checkArgument(shop_name.equals(shopName) && manager_name.equals(managerName)
                            && city.equals(cityName),
                            "通过" + shopName + "和" + managerName +"和"+cityName+ "搜索门店展示结果为" + shop_name + "和" + manager_name+"和"+city);
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("按照门店名称和门店联系人和城市搜索门店");
        }
    }

    //按照门店名称和门店联系人和城市和开启状态搜索门店
    @Test()
    public void shopSearch6() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String shopName = "门店";
            String managerName = "张三丰";
            String cityName = "北京";
            boolean status = true;
            JSONArray shopList = md.searchShop(shopName, managerName, cityName, status, 1, 10).getJSONArray("list");
            if (shopList != null)
                for (int i = 0; i < shopList.size(); i++) {
                    String shop_name = shopList.getJSONObject(i).getString("shop_name");
                    String manager_name = shopList.getJSONObject(i).getString("manager_name");
                    String city = shopList.getJSONObject(i).getString("city");
                    boolean status1 = shopList.getJSONObject(i).getBooleanValue("city");
                    Preconditions.checkArgument(shop_name.equals(shopName) && manager_name.equals(managerName)
                                    && city.equals(cityName) && status1==status,
                            "通过" + shopName + "和" + managerName +"和"+cityName+"和"+status+ "搜索门店展示结果为" + shop_name + "和" + manager_name+"和"+city+"和"+status1);
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("按照门店名称和门店联系人和城市和开启状态搜索门店");
        }
    }

    //按照门店名称和门店联系人和城市和关闭状态搜索门店
    @Test()
    public void shopSearch7() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String shopName = "门店";
            String managerName = "张三丰";
            String cityName = "北京";
            boolean status = false;
            JSONArray shopList = md.searchShop(shopName, managerName, cityName, status, 1, 10).getJSONArray("list");
            if (shopList != null)
                for (int i = 0; i < shopList.size(); i++) {
                    String shop_name = shopList.getJSONObject(i).getString("shop_name");
                    String manager_name = shopList.getJSONObject(i).getString("manager_name");
                    String city = shopList.getJSONObject(i).getString("city");
                    boolean status1 = shopList.getJSONObject(i).getBooleanValue("city");
                    Preconditions.checkArgument(shop_name.equals(shopName) && manager_name.equals(managerName)
                                    && city.equals(cityName) && status1==status,
                            "通过" + shopName + "和" + managerName +"和"+cityName+"和"+status+ "搜索门店展示结果为" + shop_name + "和" + manager_name+"和"+city+"和"+status1);
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("按照门店名称和门店联系人和城市和关闭状态搜索门店");
        }
    }
}
