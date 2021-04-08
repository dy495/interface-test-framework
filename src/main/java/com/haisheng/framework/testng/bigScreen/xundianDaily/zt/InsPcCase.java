package com.haisheng.framework.testng.bigScreen.xundianDaily.zt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.xundianDaily.MendianInfo;
import com.haisheng.framework.testng.bigScreen.xundianDaily.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.WechatScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.XundianScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.util.InsInfo;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.jayway.jsonpath.JsonPath;
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
//    InsInfo info = new  InsInfo();
//    InsDesc info= new EnumDesc();

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
    //新建门店、删除门店
    @Test()
    public void create_shop() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String shopName ="创建测试门店";
            String label = "明星店";
            String openingTime = "00:00:00";
            String closingTime = "23:59:59";
            String managerName = "联系人1";
            String managerPhone ="13666666666";
            String city = "110000";
            String address = "圆明园";
            double longitude = 23.99;
            double latitude = 17.22;
            String tripartite_shop_id = "4321";
            int recommended = 70;
            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/INS.jpg";
            String path = md.pcFileUpload(pic).getString("pic_path");
            String result = md.createShop(path,shopName,label,openingTime,closingTime,managerName,managerPhone,city,address,longitude,latitude,tripartite_shop_id,recommended).getString("result");
            Preconditions.checkArgument(result.equals("true"), "新建门店失败" + result);
            int id = md.searchShop(null,null,null,null,1,10).getJSONArray("list").getJSONObject(0).getInteger("id");
            md.deleteShop(id);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建门店、删除门店");
        }
    }


    //编辑门店
    @Test()
    public void update_shop() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String shopName ="创建测试门店";
            String shopName0 ="创建测试门店";
            String label = "明星店";
            String openingTime = "00:00:00";
            String closingTime = "23:59:59";
            String managerName = "联系人1";
            String city = "110000";
            String phone = "13666666666";
            String address = "圆明园";
            double longitude = 23.99;
            double latitude = 17.22;
            String tripartite_shop_id = "4321";
            int recommended = 70;
            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/INS.jpg";
            String path = md.pcFileUpload(pic).getString("pic_path");
            md.createShop(path,shopName,label,openingTime,closingTime,managerName,phone,city,address,longitude,latitude,tripartite_shop_id,recommended);
            int id = md.searchShop(null,null,null,null,1,10).getJSONArray("list").getJSONObject(0).getInteger("id");
            md.updateShop(id,path,shopName0,label,openingTime,closingTime,managerName,phone,city,address,longitude,latitude,tripartite_shop_id,recommended);
            JSONArray arr = md.searchShop(null,null,null,null,1,10).getJSONArray("list");
            for (int i = 0 ; i < arr.size(); i++){
                JSONObject obj = arr.getJSONObject(i);
                if (obj.getInteger("id")==id){
                    Preconditions.checkArgument(obj.getString("shop_name").equals(shopName0),"修改前名字是"+shopName+"，期望修改为"+shopName0+"，实际修改后为"+obj.getString("shop_name"));
                }
            }
            md.deleteShop(id);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("编辑门店");
        }
    }

    //新建门店不合格项校验
    @Test()
    public void create_shop1() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String shopName ="创建测试门店";
            String shopName0 ="创建测试门店";
            String label = "明星店";
            String openingTime = "00:00:00";
            String closingTime = "23:59:59";
            String managerName = "联系人1";
            String city = "110000";
            String address = "圆明园";
            double longitude = 23.99;
            double latitude = 17.22;
            String tripartite_shop_id = "4321";
            int recommended = 70;
            String phone = "13666666666";
            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/INS.jpg";
            String path = md.pcFileUpload(pic).getString("pic_path");
            //不传图片
            int code = md.createShop0(null,shopName,label,openingTime,closingTime,managerName,phone,city,address,longitude,latitude,tripartite_shop_id,recommended).getInteger("code");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+code);
            //门店名称大于10
            int code0 = md.createShop0(path,EnumDesc.DESC_BETWEEN_15_20.getDesc() ,label,openingTime,closingTime,managerName,phone,city,address,longitude,latitude,tripartite_shop_id,recommended).getInteger("code");
            Preconditions.checkArgument(code0==1001,"状态码期待1001，实际"+code0);
            //门店标签大于5个字
            int code1 = md.createShop0(path,shopName ,"123456",openingTime,closingTime,managerName,phone,city,address,longitude,latitude,tripartite_shop_id,recommended).getInteger("code");
            Preconditions.checkArgument(code1==1001,"状态码期待1001，实际"+code1);
            //门店联系人大于10个字
            int code2 = md.createShop0(path,shopName,label,openingTime,closingTime,EnumDesc.DESC_BETWEEN_20_30.getDesc(),phone,city,address,longitude,latitude,tripartite_shop_id,recommended).getInteger("code");
            Preconditions.checkArgument(code2==1001,"状态码期待1001，实际"+code2);
            //联系电话大于11位
            int code3 = md.createShop0(path,shopName,label,openingTime,closingTime,managerName ,"136046098690",city,address,longitude,latitude,tripartite_shop_id,recommended).getInteger("code");
            Preconditions.checkArgument(code3==1001,"状态码期待1001，实际"+code3);
            //详情地址大于50字
            int code4 = md.createShop0(path,shopName,label,openingTime,closingTime,managerName ,phone,city,EnumDesc.DESC_BETWEEN_200_300.getDesc() ,longitude,latitude,tripartite_shop_id,recommended).getInteger("code");
            Preconditions.checkArgument(code4==1001,"状态码期待1001，实际"+code4);
            //经纬度是3位小数
            int code5 = md.createShop0(path,shopName,label,openingTime,closingTime,managerName ,phone,city,address ,17.222,23.999,tripartite_shop_id,recommended).getInteger("code");
            Preconditions.checkArgument(code5==1001,"状态码期待1001，实际"+code5);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("编辑门店");
        }
    }

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
    //新建会员等级
    @Test()
    public void create_level() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/INS.jpg";
            String path = md.pcFileUpload(pic).getString("pic_path");
            int code = md.member_level_add0("等级",path,10,"嗷嗷","aa",1,10,10,false).getInteger("code");
            Preconditions.checkArgument(code==1000,"状态码期待1000，实际"+code);
            Integer total = md.member_level_page(null,1,10).getInteger("total");
            JSONArray list = md.member_level_page(null,1,10).getJSONArray("list");
            int a = total-1;
            //获取刚创建等级的id
            int id = list.getJSONObject(a).getInteger("id");
            //删除等级
            int code1 = md.member_level_delete0(id).getInteger("code");
            Preconditions.checkArgument(code1==1000,"状态码期待1000，实际"+code1);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员等级管理，根据会员等级名称搜索");
        }
    }
    //编辑会员等级
    @Test()
    public void update_level() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String level_name = "等级";
            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/INS.jpg";
            String path = md.pcFileUpload(pic).getString("pic_path");
            md.member_level_add0(level_name,path,10,"嗷嗷","aa",1,10,10,true);

            Integer total = md.member_level_page(null,1,10).getInteger("total");
            JSONArray list = md.member_level_page(null,1,10).getJSONArray("list");
            int a = total-1;
            //获取刚创建等级的id
            int id = list.getJSONObject(a).getInteger("id");
            String levelname = "等级111";
            md.member_level_update(id,levelname,path,"aa","嗷嗷","aa",1,10,10,true);
            JSONArray arr = md.member_level_page(null,1,10).getJSONArray("list");
            for (int i = 0 ; i < arr.size(); i++){
                JSONObject obj = arr.getJSONObject(i);
                if (obj.getInteger("id")==id){
                    Preconditions.checkArgument(obj.getString("level_name").equals(levelname),"修改前名字是"+level_name+"，期望修改为"+levelname+"，实际修改后为"+obj.getString("level_name"));
                }
            }
            md.member_level_delete(id);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("编辑会员等级");
        }
    }

    //新建等级异常情况

    @Test()
    public void create_level0() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //等级名称超过20个字
            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/INS.jpg";
            String path = md.pcFileUpload(pic).getString("pic_path");
            int code = md.member_level_add0(EnumDesc.DESC_BETWEEN_20_30.getDesc(),path,10,"嗷嗷","aa",1,10,10,true).getInteger("code");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+code);
            //等级icon为空
            int code0 = md.member_level_add0("等级",null,10,"嗷嗷","aa",1,10,10,true).getInteger("code");
            Preconditions.checkArgument(code0==1009,"状态码期待1009，实际"+code0);
//            //levle——sort大于10

            int code1 = md.member_level_add0("等级",path,15,"嗷嗷","aa",15,15,15,true).getInteger("code");
            Preconditions.checkArgument(code1==1001,"状态码期待1001，实际"+code1);
//            //晋级条件大于100字
            int code2 = md.member_level_add0("等级",path,10,EnumDesc.DESC_BETWEEN_200_300.getDesc(),"aa",1,10,10,true).getInteger("code");
            Preconditions.checkArgument(code2==1001,"状态码期待1001，实际"+code2);
//            //会员权益大于100字
            int code3 = md.member_level_add0("等级",path,10,"aa",EnumDesc.DESC_BETWEEN_200_300.getDesc(),1,10,10,true).getInteger("code");
            Preconditions.checkArgument(code3==1001,"状态码期待1001，实际"+code3);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建等级异常情况");
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

    //新建反馈类型、删除反馈类型
    @Test()
    public void addfeedback() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int code = md.feedback_add0(EnumDesc.DESC_BETWEEN_5_10.getDesc(),EnumDesc.DESC_BETWEEN_5_10.getDesc()).getInteger("code");
            Preconditions.checkArgument(code==1000,"状态码期待1000，实际"+code);
            Integer total = md.feedbackList(null,1,10).getInteger("total");
            int a = total-1;
            int id = md.feedbackList(null,1,10).getJSONArray("list").getJSONObject(a).getInteger("id");
            String result = md.feedback_type_delete(id).getString("result");
            Preconditions.checkArgument(result.equals("true"),"状态码期待true，实际"+result);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建反馈类型、删除反馈类型");
        }
    }

    //编辑反馈类型
    @Test()
    public void updatefeedback() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String feedbackType = "反馈类型111";
            String feedbackType0 = "反馈类型11111";
            md.feedback_add(feedbackType,EnumDesc.DESC_BETWEEN_5_10.getDesc());
            Integer total = md.feedbackList(null,1,10).getInteger("total");
            int a = total-1;
            String id = md.feedbackList(null,1,10).getJSONArray("list").getJSONObject(a).getString("id");
            md.updateFeedbackType(id,feedbackType0,EnumDesc.DESC_BETWEEN_5_10.getDesc());
            JSONArray arr = md.feedbackList(null,1,10).getJSONArray("list");
            for (int i = 0 ; i < arr.size(); i++){
                JSONObject obj = arr.getJSONObject(i);
                if (obj.getString("id").equals(id)){
                    Preconditions.checkArgument(obj.getString("feedback_type").equals(feedbackType0),"修改前名字是"+feedbackType+"，期望修改为"+feedbackType0+"，实际修改后为"+obj.getString("feedback_type"));
                }
            }
            int id1 = Integer.parseInt(id);
            md.feedback_type_delete(id1);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("编辑反馈类型");
        }
    }

    //内容管理通过内容标题进行搜索
    @Test()
    public void articleSearch() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String name = "推荐活动";
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
            String start = "2021-04-08";
            String end = "2021-04-08";
            JSONArray memList = md.article_page(null,start,end,null,null,1,10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String create_start = memList.getJSONObject(i).getString("create_time");
                    Preconditions.checkArgument(create_start.contains(start),"通过" + start +"和"+end+  "搜索内容管理展示结果为" + create_start);
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
            String start = "2021-04-08";
            String end = "2021-04-08";
            JSONArray memList = md.article_page(null,null,null,start,end,1,10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String modify_start = memList.getJSONObject(i).getString("modify_time");
                    Preconditions.checkArgument(modify_start.contains(start),"通过" + start +"和"+end+  "搜索内容管理展示结果为" + modify_start);
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
            String start = "2021-04-08";
            String end = "2021-04-08";
            JSONArray memList = md.article_page(null,start,end,start,end,1,10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String title = memList.getJSONObject(i).getString("title");
                    String create_start = memList.getJSONObject(i).getString("create_time");
//                    String create_end = memList.getJSONObject(i).getString("create_end");
                    String modify_start = memList.getJSONObject(i).getString("modify_time");
//                    String modify_end = memList.getJSONObject(i).getString("modify_end");
                    Preconditions.checkArgument(modify_start.contains(start)&&create_start.contains(start),"通过" + start +"和"+end+  "搜索内容管理展示结果为" + create_start+"和"+modify_start);
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
            String name = "推荐活动";
            String start = "2021-04-08";
            String end = "2021-04-08";
            JSONArray memList = md.article_page(name,start,end,start,end,1,10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String title = memList.getJSONObject(i).getString("title");
                    String create_start = memList.getJSONObject(i).getString("create_time");
//                    String create_end = memList.getJSONObject(i).getString("create_end");
                    String modify_start = memList.getJSONObject(i).getString("modify_time");
//                    String modify_end = memList.getJSONObject(i).getString("modify_end");
                    Preconditions.checkArgument(title.contains(name)&&modify_start.contains(start)&&create_start.contains(start),"通过" + start +"和"+end+  "搜索内容管理展示结果为" + create_start+"和"+modify_start);
                }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容管理通过内容标题和更新时间和创建时间进行搜索");
        }
    }

    //新建内容一张大图
    @Test()
    public void create_message() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/INS.jpg";
            String path = md.pcFileUpload(pic).getString("pic_path");
            JSONArray piclist = new JSONArray();
            piclist.add(path);
            JSONObject res = md.article_export("活动活动","ONE_BIG","1","PREFERENTIAL",piclist);
            Preconditions.checkArgument(res.getInteger("code")==1000,"状态码期待1000，实际"+res.getInteger("code"));
            Long id  = res.getJSONObject("data").getLong("id");
            JSONObject res1 = md.article_status_change(id);
            Preconditions.checkArgument(res1.getInteger("code")==1000,"状态码期待1000，实际"+res1.getInteger("code"));
            JSONObject res2 = md.article_delete(id);
            Preconditions.checkArgument(res2.getInteger("code")==1000,"状态码期待1000，实际"+res2.getInteger("code"));
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建内容一张大图并删除");
        }
    }

    //新建内容三张小图
    @Test()
    public void create_message1() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/INS.jpg";
            String path = md.pcFileUpload(pic).getString("pic_path");
            JSONArray piclist = new JSONArray();
            piclist.add(path);
            piclist.add(path);
            piclist.add(path);
            JSONObject res = md.article_export("活动活动","THREE","1","RED_PAPER",piclist);
            Preconditions.checkArgument(res.getInteger("code")==1000,"状态码期待1000，实际"+res.getInteger("code"));
            Long id  = res.getJSONObject("data").getLong("id");
            JSONObject res1 = md.article_status_change(id);
            Preconditions.checkArgument(res1.getInteger("code")==1000,"状态码期待1000，实际"+res1.getInteger("code"));
            JSONObject res2 = md.article_delete(id);
            Preconditions.checkArgument(res2.getInteger("code")==1000,"状态码期待1000，实际"+res2.getInteger("code"));
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建内容三张小图并删除");
        }
    }

    //新建内容左一图
    @Test()
    public void create_message2() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/INS.jpg";
            String path = md.pcFileUpload(pic).getString("pic_path");
            JSONArray piclist = new JSONArray();
            piclist.add(path);
            JSONObject res = md.article_export("活动活动","ONE_LEFT","1","RED_PAPER",piclist);
            Preconditions.checkArgument(res.getInteger("code")==1000,"状态码期待1000，实际"+res.getInteger("code"));
            Long id  = res.getJSONObject("data").getLong("id");
            JSONObject res1 = md.article_status_change(id);
            Preconditions.checkArgument(res1.getInteger("code")==1000,"状态码期待1000，实际"+res1.getInteger("code"));
            JSONObject res2 = md.article_delete(id);
            Preconditions.checkArgument(res2.getInteger("code")==1000,"状态码期待1000，实际"+res2.getInteger("code"));
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建内容左一图并删除");
        }
    }
    //新建内容异常项
    @Test()
    public void create_message3() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/INS.jpg";
            String path = md.pcFileUpload(pic).getString("pic_path");
            JSONArray piclist = new JSONArray();
            piclist.add(path);
            //新建内容无图片
            String message = md.article_export("活动活动","ONE_LEFT","1","RED_PAPER",null).getString("message");
            Preconditions.checkArgument(message.equals("图片不能为空"),"期待图片不能为空，实际"+message);
            //新建内容标题小于四个字
            String message1 = md.article_export("活动","ONE_LEFT","1","RED_PAPER",piclist).getString("message");
            Preconditions.checkArgument(message1.equals("文章标题长度范围为[4,20]"),"期待文章标题长度范围为[4,20]，实际"+message1);
            //内容详情为空
            String message2 = md.article_export("活动活动","ONE_LEFT",null,"RED_PAPER",piclist).getString("message");
            Preconditions.checkArgument(message2.equals("内容详情不能为空"),"期待实际内容详情不能为空，实际"+message2);
            //文章样式为空
            String message3 = md.article_export("活动活动","ONE_LEFT","1",null,piclist).getString("message");
            Preconditions.checkArgument(message3.equals("文章标签不能为空"),"期待文章标签不能为空，实际"+message3);
            //文章标签
            String message4 = md.article_export("活动活动","ONE_LEFT","1",null,piclist).getString("message");
            Preconditions.checkArgument(message4.equals("文章标签不能为空"),"期待文章标签不能为空，实际"+message4);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建内容异常项");
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

    //新建口味、删除口味
    @Test()
    public void create_taste() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/INS.jpg";
            String path = md.pcFileUpload(pic).getString("pic_path");
//            JSONArray piclist = new JSONArray();
//            piclist.add(path);
            JSONObject res = md.taste_add(path,path,path,"芒果","1",1,true);
            Preconditions.checkArgument(res.getInteger("code")==1000,"状态码期待1000，实际"+res.getInteger("code"));
            int total = md.taste_search(null,1,10).getInteger("total");
            int a = total-1;
            int id = md.taste_search(null,1,10).getJSONArray("list").getJSONObject(a).getInteger("id");
//            JSONArray list = md.
            int code = md.taste_delete(id).getInteger("code");
            Preconditions.checkArgument(code==1000,"状态码期待1000，实际"+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建口味、删除口味");
        }
    }
}
