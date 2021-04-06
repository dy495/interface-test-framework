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
            String shopName = "虚拟购物中心";
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
            String managerName = "yecan";
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
                    boolean status1 = shopList.getJSONObject(i).getBoolean("is_show");
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

    //编辑门店

    //客户管理，根据用户名搜索
    @Test()
    public void memberSearch() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String memberName = "门店";
            JSONArray memList = md.member_list(null,1,10,null,memberName,null).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String name = memList.getJSONObject(i).getString("name");
                    Preconditions.checkArgument(name.equals(memberName),"通过" + memberName +  "搜索客户展示结果为" + name );
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理，根据用户名称搜索");
        }
    }
    //客户管理，根据电话号进行搜索
    @Test()
    public void memberSearch1() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String memphone = "15677889566";
            JSONArray memList = md.member_list(null,1,10,null,null,memphone).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String phone = memList.getJSONObject(i).getString("phone");
                    Preconditions.checkArgument(phone.equals(memphone),"通过" + memphone +  "搜索客户展示结果为" + phone );
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理，根据电话号搜索");
        }
    }

    //客户管理，根据注册日期进行搜索
    @Test()
    public void memberSearch2() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String memdata = "2021-04-05";
            JSONArray memList = md.member_list(null,1,10,memdata,null,null).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String register_date = memList.getJSONObject(i).getString("register_date");
                    Preconditions.checkArgument(register_date.equals(memdata),"通过" + memdata +  "搜索客户展示结果为" + register_date );
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理，根据日期搜索");
        }
    }

    //客户管理，根据注册日期，用户名称进行搜索
    @Test()
    public void memberSearch3() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String memdata = "2021-04-05";
            String memName = "李离";
            JSONArray memList = md.member_list(null,1,10,memdata,memName,null).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String register_date = memList.getJSONObject(i).getString("register_date");
                    String name = memList.getJSONObject(i).getString("name");
                    Preconditions.checkArgument(register_date.equals(memdata)&&name.equals(memName),"通过" + memdata +"和"+memName+  "搜索客户展示结果为" + register_date+"和"+name );
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理，根据日期和用户名称搜索");
        }
    }

    //客户管理，根据注册日期，用户名称、联系方式进行搜索
    @Test()
    public void memberSearch4() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String memdata = "2021-04-05";
            String memName = "李离";
            String memphone = "15677889566";
            JSONArray memList = md.member_list(null,1,10,memdata,memName,memphone).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String register_date = memList.getJSONObject(i).getString("register_date");
                    String name = memList.getJSONObject(i).getString("name");
                    String phone = memList.getJSONObject(i).getString("phone");
                    Preconditions.checkArgument(register_date.equals(memdata)&&name.equals(memName),"通过" + memdata +"和"+memName+ "和"+memphone+ "搜索客户展示结果为" + register_date+"和"+name+"和"+phone );
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理，根据日期和用户名称搜索");
        }
    }

    //会员等级通过等级名称搜索
    @Test()
    public void levelSearch() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String name = "黑海会员";
            JSONArray memList = md.member_level_page(name,1,10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String level_name = memList.getJSONObject(i).getString("level_name");
                    Preconditions.checkArgument(level_name.equals(name),"通过" + name +  "搜索会员等级展示结果为" + level_name);
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员等级管理，根据会员等级名称搜索");
        }
    }

    //用户反馈通过等级名称搜索
    @Test()
    public void feedbackSearch() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String name = "会员";
            JSONArray memList = md.member_level_page(name,1,10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String level_name = memList.getJSONObject(i).getString("level_name");
                    Preconditions.checkArgument(level_name.equals(name),"通过" + name +  "搜索会员等级展示结果为" + level_name);
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员等级管理，根据会员等级名称搜索");
        }
    }
    //用户反馈通过反馈类型搜索
    @Test()
    public void feedbackSearch1() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            JSONArray levelList = md.feedbackTypeAll().getJSONArray("list");
            if (levelList != null)
                for (int i = 0; i < levelList.size(); i++) {
                    Integer feedback_type_id = levelList.getJSONObject(i).getInteger("feedback_type_id");
                    JSONArray feedlist = md.feedbackList(null, feedback_type_id, 1, 10).getJSONArray("list");
                    if (feedlist != null) {
                        for (int j = 0; j < feedlist.size(); j++) {
                            Integer feedback_type_id0 = feedlist.getJSONObject(j).getInteger("feedback_type_id");
                            Preconditions.checkArgument(feedback_type_id==feedback_type_id0, "通过" + feedback_type_id + "搜索口味反馈展示结果为" + feedback_type_id0);
                        }
                    }
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("用户反馈通过反馈类型搜索");
        }
    }

    //用户反馈通过等级名称搜索
    @Test()
    public void feedbackSearch2() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String name = "会员";
            JSONArray memList = md.member_level_page(name,1,10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String level_name = memList.getJSONObject(i).getString("level_name");
                    Preconditions.checkArgument(level_name.equals(name),"通过" + name +  "搜索会员等级展示结果为" + level_name);
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员等级管理，根据会员等级名称搜索");
        }
    }

    //反馈类型通过类型名称搜索
    @Test()
    public void feedbackSearch3() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String name = "口味反馈1";
            JSONArray memList = md.feedbackList(name,1,10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String feedback_type = memList.getJSONObject(i).getString("feedback_type");
                    Preconditions.checkArgument(feedback_type.equals(name),"通过" + name +  "搜索反馈类型展示结果为" + feedback_type);
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("反馈类型通过类型名称搜索");
        }
    }

    //内容管理通过内容标题进行搜索
    @Test()
    public void articleSearch() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String name = "活动活动";
            JSONArray memList = md.article_page(name,null,null,null,null,1,10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String title = memList.getJSONObject(i).getString("title");
                    Preconditions.checkArgument(title.equals(name),"通过" + name +  "搜索内容管理展示结果为" + title);
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容管理通过内容标题进行搜索");
        }
    }

    //内容管理通过内容标题进行搜索
    @Test()
    public void articleSearch1() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String start = "2021-04-06";
            String end = "2021-04-06";
            JSONArray memList = md.article_page(null,start,end,null,null,1,10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String create_start = memList.getJSONObject(i).getString("create_start");
                    String create_end = memList.getJSONObject(i).getString("create_end");
                    Preconditions.checkArgument(start.equals(create_start)&&end.equals(create_end),"通过" + start +"和"+end+  "搜索内容管理展示结果为" + create_start+"和"+create_end);
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容管理通过创建时间进行搜索");
        }
    }

    //内容管理通过更新时间进行搜索
    @Test()
    public void articleSearch2() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String start = "2021-04-06";
            String end = "2021-04-06";
            JSONArray memList = md.article_page(null,null,null,start,end,1,10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String modify_start = memList.getJSONObject(i).getString("modify_start");
                    String modify_end = memList.getJSONObject(i).getString("modify_end");
                    Preconditions.checkArgument(start.equals(modify_start)&&end.equals(modify_end),"通过" + start +"和"+end+  "搜索内容管理展示结果为" + modify_start+"和"+modify_end);
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容管理通过更新时间进行搜索");
        }
    }

    //内容管理通过更新时间和创建时间进行搜索
    @Test()
    public void articleSearch3() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String start = "2021-04-06";
            String end = "2021-04-06";
            JSONArray memList = md.article_page(null,start,end,start,end,1,10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String create_start = memList.getJSONObject(i).getString("create_start");
                    String create_end = memList.getJSONObject(i).getString("create_end");
                    String modify_start = memList.getJSONObject(i).getString("modify_start");
                    String modify_end = memList.getJSONObject(i).getString("modify_end");
                    Preconditions.checkArgument(start.equals(modify_start)&&end.equals(modify_end)&&start.equals(create_start)&&end.equals(create_end),"通过" + start +"和"+end+  "搜索内容管理展示结果为" + modify_start+"和"+modify_end);
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容管理通过更新时间和创建时间进行搜索");
        }
    }

    //内容管理通过内容标题和更新时间和创建时间进行搜索
    @Test()
    public void articleSearch4() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String name = "活动活动";
            String start = "2021-04-06";
            String end = "2021-04-06";
            JSONArray memList = md.article_page(null,start,end,start,end,1,10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String title = memList.getJSONObject(i).getString("title");
                    String create_start = memList.getJSONObject(i).getString("create_start");
                    String create_end = memList.getJSONObject(i).getString("create_end");
                    String modify_start = memList.getJSONObject(i).getString("modify_start");
                    String modify_end = memList.getJSONObject(i).getString("modify_end");
                    Preconditions.checkArgument(title.equals(name)&&start.equals(modify_start)&&end.equals(modify_end)&&start.equals(create_start)&&end.equals(create_end),"通过" + start +"和"+end+  "搜索内容管理展示结果为" + modify_start+"和"+modify_end);
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容管理通过内容标题和更新时间和创建时间进行搜索");
        }
    }

    //口味管理通过口味名称进行搜索
    @Test()
    public void tasteSearch() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String name = "口味排行";
            JSONArray memList = md.taste_search(name,1,10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String taste_name = memList.getJSONObject(i).getString("taste_name");
                    Preconditions.checkArgument(taste_name.equals(name),"通过" + name +  "搜索口味管理展示结果为" + taste_name);
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("口味管理通过口味名称进行搜索");
        }
    }
}
