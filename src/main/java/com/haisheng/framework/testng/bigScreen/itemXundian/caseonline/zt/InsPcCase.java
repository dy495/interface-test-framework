package com.haisheng.framework.testng.bigScreen.itemXundian.caseonline.zt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
//import com.haisheng.framework.testng.bigScreen.xundian.util.MendianInfo;
import com.haisheng.framework.testng.bigScreen.itemXundian.util.MendianInfoOnline;
import com.haisheng.framework.testng.bigScreen.itemXundian.util.StoreScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.itemXundian.util.XundianScenarioUtilOnline;

import com.haisheng.framework.testng.bigScreen.itemXundian.util.WechatScenarioUtilOnline;

import com.haisheng.framework.testng.bigScreen.itemXundian.util.SupporterUtil;
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

public class InsPcCase extends TestCaseCommon implements TestCaseStd {
    //    public static final Logger log = LoggerFactory.getLogger(StorePcAndAppData.class);
    public static final int page = 1;
    public static final int size = 100;
    private static final EnumTestProduce PRODUCE = EnumTestProduce.INS_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    XundianScenarioUtilOnline xd = XundianScenarioUtilOnline.getInstance();
    StoreScenarioUtilOnline md = StoreScenarioUtilOnline.getInstance();
    WechatScenarioUtilOnline wx = WechatScenarioUtilOnline.getInstance();
    MendianInfoOnline info = new MendianInfoOnline();
    SupporterUtil util = new SupporterUtil(visitor);
//    InsInfo info = new  InsInfo();
//    InsDesc info= new EnumDesc();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "周涛";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_ONLINE_TEST.getJobName());
        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.INS_ONLINE.getDesc());
        commonConfig.dingHook = DingWebhook.ONLINE_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"13604609869", "15084928847"};
//        commonConfig.shopId = EnumTestProduce. INS_DAILY.getShopId();
        commonConfig.referer = PRODUCE.getReferer();
        beforeClassInit(commonConfig);
        logger.debug("xundian " + xd);

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
        xd.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }


    /**
     * -------------------------------------INS相关---------------------------------------------------------------------------
     */


//按照门店名称搜索门店
    @Test()
    public void shopSearch() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //搜索门店
            String shopName = "中关村1号店";
            JSONArray shopList = md.searchShop(shopName, null, null, null, 1, 10).getJSONArray("list");
            if (shopList != null)
                for (int i = 0; i < shopList.size(); i++) {
                    String shop_name = shopList.getJSONObject(i).getString("shop_name");
                    Preconditions.checkArgument(shop_name.equals(shopName), "通过" + shopName + "搜索门店展示结果为" + shop_name);
                }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("按照门店名称搜索门店");
        }
    }

    //按照联系人搜索门店
    @Test()
    public void shopSearch0() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String managerName = "张三丰";
            JSONArray shopList = md.searchShop(null, managerName, null, null, 1, 10).getJSONArray("list");
            if (shopList != null)
                for (int i = 0; i < shopList.size(); i++) {
                    String manager_name = shopList.getJSONObject(i).getString("manager_name");
                    Preconditions.checkArgument(manager_name.contains(managerName), "通过" + managerName + "搜索门店展示结果为" + manager_name);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("按照联系人搜索门店");
        }
    }

    //按照城市搜索门店
    @Test()
    public void shopSearch1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String cityName = "北京";
            JSONArray shopList = md.searchShop(null, null, cityName, null, 1, 10).getJSONArray("list");
            if (shopList != null)
                for (int i = 0; i < shopList.size(); i++) {
                    String city_name = shopList.getJSONObject(i).getString("city");
                    Preconditions.checkArgument(city_name.equals(cityName), "通过" + cityName + "搜索门店展示结果为" + city_name);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("按照城市搜索门店");
        }
    }

    //按照开启状态搜索门店
    @Test()
    public void shopSearch2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            boolean status = true;
            JSONArray shopList = md.searchShop(null, null, null, status, 1, 10).getJSONArray("list");
            if (shopList != null)
                for (int i = 0; i < shopList.size(); i++) {
                    boolean status1 = shopList.getJSONObject(i).getBoolean("is_show");
                    Preconditions.checkArgument(status1, "通过" + status + "搜索门店展示结果为" + status1);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("按照开启状态搜索门店");
        }
    }

    //按照关闭状态搜索门店
    @Test()
    public void shopSearch3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            boolean status = false;
            JSONArray shopList = md.searchShop(null, null, null, status, 1, 10).getJSONArray("list");
            if (shopList != null)
                for (int i = 0; i < shopList.size(); i++) {
                    boolean status1 = shopList.getJSONObject(i).getBooleanValue("status");
                    Preconditions.checkArgument(status1 == status, "通过" + status + "搜索门店展示结果为" + status1);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("按照关闭状态搜索门店");
        }
    }

    //按照门店名称和门店联系人搜索门店
    @Test()
    public void shopSearch4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String shopName = "中关村1号店";
            String managerName = "张三丰";
            JSONArray shopList = md.searchShop(shopName, managerName, null, null, 1, 10).getJSONArray("list");
            if (shopList != null)
                for (int i = 0; i < shopList.size(); i++) {
                    String shop_name = shopList.getJSONObject(i).getString("shop_name");
                    String manager_name = shopList.getJSONObject(i).getString("manager_name");
                    Preconditions.checkArgument(shop_name.equals(shopName) && manager_name.equals(managerName), "通过" + shopName + "和" + managerName + "搜索门店展示结果为" + shop_name + "和" + manager_name);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("按照门店名称和门店联系人搜索门店");
        }
    }

    //按照门店名称和门店联系人和城市搜索门店
    @Test()
    public void shopSearch5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String shopName = "中关村1号店";
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
                            "通过" + shopName + "和" + managerName + "和" + cityName + "搜索门店展示结果为" + shop_name + "和" + manager_name + "和" + city);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("按照门店名称和门店联系人和城市搜索门店");
        }
    }

    //按照门店名称和门店联系人和城市和开启状态搜索门店
    @Test()
    public void shopSearch6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String shopName = "中关村1号店";
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
                                    && city.equals(cityName) && status1 == status,
                            "通过" + shopName + "和" + managerName + "和" + cityName + "和" + status + "搜索门店展示结果为" + shop_name + "和" + manager_name + "和" + city + "和" + status1);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("按照门店名称和门店联系人和城市和开启状态搜索门店");
        }
    }

    //按照门店名称和门店联系人和城市和关闭状态搜索门店
    @Test()
    public void shopSearch7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String shopName = "中关村1号店";
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
                                    && city.equals(cityName) && status1 == status,
                            "通过" + shopName + "和" + managerName + "和" + cityName + "和" + status + "搜索门店展示结果为" + shop_name + "和" + manager_name + "和" + city + "和" + status1);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("按照门店名称和门店联系人和城市和关闭状态搜索门店");
        }
    }

    //新建门店、删除门店
    @Test()
    public void create_shop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String shopName = "两杆大烟枪";
            String label = "明星店";
            String openingTime = "00:00:00";
            String closingTime = "23:59:59";
            String managerName = "联系人1";
            String managerPhone = "13666666666";
            String city = "110105";
            String address = "圆明园";
            double longitude = 23.99;
            double latitude = 17.22;
            String tripartite_shop_id = "4321";
            int recommended = 70;
            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/multimedia/picture/INS.jpg";
            String base64 = MendianInfoOnline.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
            String result = md.createShop(path, shopName, label, openingTime, closingTime, managerName, managerPhone, city, address, longitude, latitude, tripartite_shop_id, recommended).getString("result");
            Preconditions.checkArgument(result.equals("true"), "新建门店失败" + result);
            int total = md.searchShop("两杆大烟枪", null, null, null, 1, 100).getInteger("total");
            int a = total - 1;
            int id = md.searchShop("两杆大烟枪", null, null, null, 1, 100).getJSONArray("list").getJSONObject(a).getInteger("id");
            md.deleteShop(id);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建门店、删除门店");
        }
    }


    //编辑门店
    @Test()
    public void update_shop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String shopName = "创建测试门店333";
            String shopName0 = "创建测试门店123";
            String label = "明星店";
            String openingTime = "00:00:00";
            String closingTime = "23:59:59";
            String managerName = "联系人1";
            String city = "110105";
            String phone = "13666666666";
            String address = "圆明园";
            double longitude = 23.99;
            double latitude = 17.22;
            String tripartite_shop_id = "4321";
            int recommended = 70;
            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/multimedia/picture/INS.jpg";
            String base64 = MendianInfoOnline.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
            md.createShop(path, shopName, label, openingTime, closingTime, managerName, phone, city, address, longitude, latitude, tripartite_shop_id, recommended);
            int pages = md.searchShop(null, null, null, null, 1, 10).getInteger("pages");
            int page_size = md.searchShop(null, null, null, null, pages, 10).getInteger("page_size");
            int a = page_size - 1;
            int id = md.searchShop(null, null, null, null, pages, 10).getJSONArray("list").getJSONObject(a).getInteger("id");
            md.updateShop(id, path, shopName0, label, openingTime, closingTime, managerName, phone, city, address, longitude, latitude, tripartite_shop_id, recommended);
            JSONArray arr = md.searchShop(null, null, null, null, pages, 10).getJSONArray("list");
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if (obj.getInteger("id") == id) {
                    Preconditions.checkArgument(obj.getString("shop_name").equals(shopName0), "修改前名字是" + shopName + "，期望修改为" + shopName0 + "，实际修改后为" + obj.getString("shop_name"));
                }
            }
            md.deleteShop(id);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("编辑门店");
        }
    }

    //新建门店不合格项校验
    @Test()
    public void create_shop1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String shopName = "创建测试门店11";
            String label = "明星店";
            String openingTime = "00:00:00";
            String closingTime = "23:59:59";
            String managerName = "联系人1";
            String city = "110105";
            String address = "圆明园";
            double longitude = 23.99;
            double latitude = 17.22;
            String tripartite_shop_id = "4321";
            int recommended = 70;
            String phone = "13666666666";
            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/multimedia/picture/INS.jpg";
            String base64 = MendianInfoOnline.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
            //不传图片
            int code = md.createShop0(null, shopName, label, openingTime, closingTime, managerName, phone, city, address, longitude, latitude, tripartite_shop_id, recommended).getInteger("code");
            Preconditions.checkArgument(code == 1001, "状态码期待1001，实际" + code);
//            //门店名称大于10
            int code0 = md.createShop0(path, EnumDesc.DESC_BETWEEN_15_20.getDesc(), label, openingTime, closingTime, managerName, phone, city, address, longitude, latitude, tripartite_shop_id, recommended).getInteger("code");
            Preconditions.checkArgument(code0 == 1001, "状态码期待1001，实际" + code0);
//            //门店标签大于5个字
            int code1 = md.createShop0(path, shopName, "123455555555", openingTime, closingTime, managerName, phone, city, address, longitude, latitude, tripartite_shop_id, recommended).getInteger("code");
            Preconditions.checkArgument(code1 == 1001, "状态码期待1001，实际" + code1);
//            //门店联系人大于10个字
            int code2 = md.createShop0(path, shopName, label, openingTime, closingTime, EnumDesc.DESC_BETWEEN_20_30.getDesc(), phone, city, address, longitude, latitude, tripartite_shop_id, recommended).getInteger("code");
            Preconditions.checkArgument(code2 == 1001, "状态码期待1001，实际" + code2);
////            //联系电话大于11位
            int code3 = md.createShop0(path, shopName, label, openingTime, closingTime, managerName, "136046098690", city, address, longitude, latitude, tripartite_shop_id, recommended).getInteger("code");
            Preconditions.checkArgument(code3 == 1001, "状态码期待1001，实际" + code3);
////            //详情地址大于50字
            int code4 = md.createShop0(path, shopName, label, openingTime, closingTime, managerName, phone, city, EnumDesc.DESC_BETWEEN_200_300.getDesc(), longitude, latitude, tripartite_shop_id, recommended).getInteger("code");
            Preconditions.checkArgument(code4 == 1001, "状态码期待1001，实际" + code4);
//            //经纬度是3位小数
//            int code5 = md.createShop0(path, shopName, label, openingTime, closingTime, managerName, phone, city, address, 17.222, 23.999, tripartite_shop_id, recommended).getInteger("code");
//            Preconditions.checkArgument(code5 == 1001, "状态码期待1001，实际" + code5);
            //不传门店名称
            int code00 = md.createShop0(path, "", label, openingTime, closingTime, managerName, phone, city, address, longitude, latitude, tripartite_shop_id, recommended).getInteger("code");
            Preconditions.checkArgument(code00 == 1001, "状态码期待1001，实际" + code00);
            //不传门店名称
            int code01 = md.createShop0(path, null, label, openingTime, closingTime, managerName, phone, city, address, longitude, latitude, tripartite_shop_id, recommended).getInteger("code");
            Preconditions.checkArgument(code01 == 1001, "状态码期待1001，实际" + code01);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("编辑门店");
        }
    }

    //客户管理，根据用户名搜索
    @Test()
    public void memberSearch() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String memberName = "杨立新";
            JSONArray memList = md.member_list(null, 1, 10, null, memberName, null).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String name = memList.getJSONObject(i).getString("nickname");
                    Preconditions.checkArgument(name.equals(memberName), "通过" + memberName + "搜索客户展示结果为" + name);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理，根据用户名称搜索");
        }
    }

    //客户管理，根据电话号进行搜索
    @Test()
    public void memberSearch1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String memphone = "15810940698";
            JSONArray memList = md.member_list(null, 1, 10, null, null, memphone).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String phone = memList.getJSONObject(i).getString("phone");
                    Preconditions.checkArgument(phone.equals(memphone), "通过" + memphone + "搜索客户展示结果为" + phone);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理，根据电话号搜索");
        }
    }

    //客户管理，根据注册日期进行搜索
    @Test()
    public void memberSearch2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String memdata = "2021-04-10";
            JSONArray memList = md.member_list(null, 1, 10, memdata, null, null).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String register_date = memList.getJSONObject(i).getString("register_date");
                    Preconditions.checkArgument(register_date.equals(memdata), "通过" + memdata + "搜索客户展示结果为" + register_date);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理，根据日期搜索");
        }
    }

    //客户管理，根据注册日期，用户名称进行搜索
    @Test()
    public void memberSearch3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String memdata = "2021-04-10";
            String memName = "杨立新";
            JSONArray memList = md.member_list(null, 1, 10, memdata, memName, null).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String register_date = memList.getJSONObject(i).getString("register_date");
                    String name = memList.getJSONObject(i).getString("nickname");
                    Preconditions.checkArgument(register_date.equals(memdata) && name.equals(memName), "通过" + memdata + "和" + memName + "搜索客户展示结果为" + register_date + "和" + name);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理，根据日期和用户名称搜索");
        }
    }

    //客户管理，根据注册日期，用户名称、联系方式进行搜索
    @Test()
    public void memberSearch4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String memdata = "2021-04-10";
            String memName = "杨立新";
            String memphone = "15810940698";
            JSONArray memList = md.member_list(null, 1, 10, memdata, memName, memphone).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String register_date = memList.getJSONObject(i).getString("register_date");
                    String name = memList.getJSONObject(i).getString("nickname");
                    String phone = memList.getJSONObject(i).getString("phone");
                    Preconditions.checkArgument(register_date.equals(memdata) && name.equals(memName), "通过" + memdata + "和" + memName + "和" + memphone + "搜索客户展示结果为" + register_date + "和" + name + "和" + phone);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理，根据日期和用户名称搜索");
        }
    }

    @Test()
    public void memberSearch5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            JSONArray list = md.member_list(null, 1, 10, null, null, null).getJSONArray("list");
            if (list.size() != 0) {
                String uid = md.member_list(null, 1, 10, null, null, null).getJSONArray("list").getJSONObject(0).getString("uid");
                String nickname = md.member_list(null, 1, 10, null, null, null).getJSONArray("list").getJSONObject(0).getString("nickname");
                String name = md.member_list(null, 1, 10, null, null, null).getJSONArray("list").getJSONObject(0).getString("name");
                int score = md.member_list(null, 1, 10, null, null, null).getJSONArray("list").getJSONObject(0).getInteger("score");
                String phone = md.member_list(null, 1, 10, null, null, null).getJSONArray("list").getJSONObject(0).getString("phone");
                int consume = md.member_list(null, 1, 10, null, null, null).getJSONArray("list").getJSONObject(0).getInteger("consume");


                String id = md.member_detail(null, uid).getString("id");
                String nickname1 = md.member_detail(null, uid).getString("nickname");
                String name1 = md.member_detail(null, uid).getString("name");
                int score1 = md.member_detail(null, uid).getInteger("score");
                String phone1 = md.member_detail(null, uid).getString("phone");
                int consume1 = md.member_detail(null, uid).getInteger("consume");

                Preconditions.checkArgument(id.equals(uid), "通过" + uid + "客户详情展示结果为" + id);
                Preconditions.checkArgument(nickname.equals(nickname1), "通过" + nickname + "客户详情展示结果为" + nickname1);
                Preconditions.checkArgument(name.equals(name1), "通过" + name + "客户详情展示结果为" + name1);
                Preconditions.checkArgument(phone.equals(phone1), "通过" + phone + "客户详情展示结果为" + phone1);
                Preconditions.checkArgument(score == score1, "通过" + score + "客户详情展示结果为" + score1);
                Preconditions.checkArgument(consume == consume1, "通过" + consume + "客户详情展示结果为" + consume1);
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理中会员信息与此会员详情信息一致");
        }
    }

    //通过客户管理查看积分详情
    @Test()
    public void memberSearch6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            JSONArray listnum = md.member_list(null, 1, 10, null, null, null).getJSONArray("list");
            if (listnum.size() != 0) {
                String id = md.member_list(null, 1, 10, null, null, null).getJSONArray("list").getJSONObject(0).getString("id");
                String name = md.member_list(null, 1, 10, null, null, null).getJSONArray("list").getJSONObject(0).getString("nickname");
                JSONArray list = md.exchange_detailed(null, 1, 100, null, null, null, null, null, null, id).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String name0 = list.getJSONObject(i).getString("exchange_customer_name");
                    Preconditions.checkArgument(name.equals(name0), "通过" + name + "客户详情展示结果为" + name0);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("查看会员积分详情");
        }
    }

    //客户详情-积分明细
    @Test()
    public void memberSearch7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray listnum = md.member_list(null, 1, 10, null, null, null).getJSONArray("list");
            if (listnum.size() != 0) {
                String id = md.member_list(null, 1, 10, null, null, null).getJSONArray("list").getJSONObject(0).getString("uid");
                String name = md.member_list(null, 1, 10, null, null, null).getJSONArray("list").getJSONObject(0).getString("nickname");
                String id0 = md.member_detail(null, id).getString("id");
                JSONArray list = md.exchange_detailed(null, 1, 100, null, null, null, null, null, null, id0).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String name0 = list.getJSONObject(i).getString("exchange_customer_name");
                    Preconditions.checkArgument(name.equals(name0), "通过" + name + "客户详情展示结果为" + name0);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户详情-积分明细");
        }
    }


    //客户详情-分配等级
    @Test()
    public void memberSearch8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.member_list(null, 1, 100, null, null, null).getJSONArray("list");
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    String uid = list.getJSONObject(i).getString("uid");
                    String levelname = md.member_detail(null, uid).getString("level");
                    int level_id = md.member_level(null, uid).getInteger("level_id");
                    JSONArray list_0 = md.level_enum(null, null, 1, 10).getJSONArray("list");
                    for (int j = 0; j < list_0.size(); j++) {
                        int id = list_0.getJSONObject(j).getInteger("id");
                        if (level_id == id) {
                            String name = list_0.getJSONObject(j).getString("level_name");
                            Preconditions.checkArgument(name.equals(levelname), "会员等级是" + levelname + "分配等级展示为" + name);
                        }
                    }
                }

            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户详情-分配等级");
        }
    }


    //会员等级通过等级名称搜索
    @Test()
    public void levelSearch() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String name = "铂金会员";
            JSONArray memList = md.member_level_page(name, 1, 10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String level_name = memList.getJSONObject(i).getString("level_name");
                    Preconditions.checkArgument(level_name.equals(name), "通过" + name + "搜索会员等级展示结果为" + level_name);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员等级管理，根据会员等级名称搜索");
        }
    }

    //新建会员等级
    @Test()
    public void create_level() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/multimedia/picture/INS.jpg";
            String base64 = MendianInfoOnline.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
            int code = md.member_level_add0("等级", path, 10, "嗷嗷", "aa", 10, 10, 10, false).getInteger("code");
            Preconditions.checkArgument(code == 1000, "状态码期待1000，实际" + code);
            Integer total = md.member_level_page(null, 1, 10).getInteger("total");
            JSONArray list = md.member_level_page(null, 1, 10).getJSONArray("list");
            int a = total - 1;
            //获取刚创建等级的id
            int id = list.getJSONObject(a).getInteger("id");
            //删除等级
            int code1 = md.member_level_delete0(id).getInteger("code");
            Preconditions.checkArgument(code1 == 1000, "状态码期待1000，实际" + code1);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建会员等级");
        }
    }

    //编辑会员等级
    @Test()
    public void update_level() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String level_name = "等级";
            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/multimedia/picture/INS.jpg";
            String base64 = MendianInfoOnline.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
            md.member_level_add0(level_name, path, 10, "嗷嗷", "aa", 4, 10, 10, true);

            Integer total = md.member_level_page(null, 1, 10).getInteger("total");
            JSONArray list = md.member_level_page(null, 1, 10).getJSONArray("list");
            int a = total - 1;
            //获取刚创建等级的id
            int id = list.getJSONObject(a).getInteger("id");
            String levelname = "等级111";
            md.member_level_update(id, levelname, path, "aa", "嗷嗷", 10, 1, 10, 10, true);
            JSONArray arr = md.member_level_page(null, 1, 10).getJSONArray("list");
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if (obj.getInteger("id") == id) {
                    Preconditions.checkArgument(obj.getString("level_name").equals(levelname), "修改前名字是" + level_name + "，期望修改为" + levelname + "，实际修改后为" + obj.getString("level_name"));
                }
            }
            md.member_level_delete(id);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("编辑会员等级");
        }
    }

    //新建等级异常情况

    @Test()
    public void create_level0() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //等级名称超过20个字,后台没有校验
            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/multimedia/picture/INS.jpg";
            String base64 = MendianInfoOnline.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");

            int code01 = md.member_level_add0(null, path, 10, "嗷嗷", "aa", 1, 10, 10, true).getInteger("code");
            Preconditions.checkArgument(code01 == 1001, "状态码期待1001，实际" + code01);

            int code = md.member_level_add0(EnumDesc.DESC_BETWEEN_30_40.getDesc(), path, 10, "嗷嗷", "aa", 1, 10, 10, true).getInteger("code");
            Preconditions.checkArgument(code == 1001, "状态码期待1001，实际" + code);
//            等级icon为空
//            int code0 = md.member_level_add0("等级", null, 10, "嗷嗷", "aa", 1, 10, 10, true).getInteger("code");
//            Preconditions.checkArgument(code0 == 1001, "状态码期待1001，实际" + code0);
//            //levle——sort大于10

//            int code1 = md.member_level_add0("等级", path, 15, "嗷嗷", "aa", 15, 15, 15, true).getInteger("code");
//            Preconditions.checkArgument(code1 == 1001, "状态码期待1001，实际" + code1);
//            //晋级条件大于100字
            int code2 = md.member_level_add0("等级", path, 10, EnumDesc.DESC_BETWEEN_200_300.getDesc(), "aa", 1, 10, 10, true).getInteger("code");
            Preconditions.checkArgument(code2 == 1001, "状态码期待1001，实际" + code2);

            int code20 = md.member_level_add0("等级", path, 10, null, "aa", 1, 10, 10, true).getInteger("code");
            Preconditions.checkArgument(code20 == 1001, "状态码期待1001，实际" + code20);
//            //会员权益大于100字
            int code3 = md.member_level_add0("等级", path, 10, "aa", EnumDesc.DESC_BETWEEN_200_300.getDesc(), 1, 10, 10, true).getInteger("code");
            Preconditions.checkArgument(code3 == 1001, "状态码期待1001，实际" + code3);

            int code30 = md.member_level_add0("等级", path, 10, "aa", null, 1, 10, 10, true).getInteger("code");
            Preconditions.checkArgument(code30 == 1001, "状态码期待1001，实际" + code30);
            int code4 = md.member_level_add0("等级", path, 10, "aa", EnumDesc.DESC_BETWEEN_200_300.getDesc(), null, 10, 10, true).getInteger("code");
            Preconditions.checkArgument(code4 == 1001, "状态码期待1001，实际" + code4);

            int code5 = md.member_level_add0("等级", path, 10, "aa", EnumDesc.DESC_BETWEEN_200_300.getDesc(), 1, null, 10, true).getInteger("code");
            Preconditions.checkArgument(code5 == 1001, "状态码期待1001，实际" + code5);

            int code6 = md.member_level_add0("等级", path, 10, "aa", EnumDesc.DESC_BETWEEN_200_300.getDesc(), 1, 10, null, true).getInteger("code");
            Preconditions.checkArgument(code6 == 1001, "状态码期待1001，实际" + code6);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建等级异常情况");
        }
    }


    //用户反馈通过反馈类型搜索
    @Test()
    public void feedbackSearch1() {
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
                            Preconditions.checkArgument(feedback_type_id.equals(feedback_type_id0), "通过" + feedback_type_id + "搜索口味反馈展示结果为" + feedback_type_id0);
                        }
                    }
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("用户反馈通过反馈类型搜索");
        }
    }

    //用户反馈通过等级名称搜索
    @Test()
    public void feedbackSearch2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String name = "会员";
            JSONArray memList = md.member_level_page(name, 1, 10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String level_name = memList.getJSONObject(i).getString("level_name");
                    Preconditions.checkArgument(level_name.equals(name), "通过" + name + "搜索会员等级展示结果为" + level_name);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员等级管理，根据会员等级名称搜索");
        }
    }

    //反馈类型通过类型名称搜索
    @Test()
    public void feedbackSearch3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String name = "口味反馈1";
            JSONArray memList = md.feedList(name, 1, 10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String feedback_type = memList.getJSONObject(i).getString("feedback_type");
                    Preconditions.checkArgument(feedback_type.equals(name), "通过" + name + "搜索反馈类型展示结果为" + feedback_type);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("反馈类型通过类型名称搜索");
        }
    }

    //新建反馈类型、删除反馈类型
    @Test()
    public void addfeedback() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int code = md.feedback_add0(EnumDesc.DESC_BETWEEN_5_10.getDesc(), EnumDesc.DESC_BETWEEN_5_10.getDesc()).getInteger("code");
            Preconditions.checkArgument(code == 1000, "状态码期待1000，实际" + code);
            Integer total = md.feedList(null, 1, 10).getInteger("total");
            int a = total - 1;
            int id = md.feedList(null, 1, 10).getJSONArray("list").getJSONObject(a).getInteger("id");
            String result = md.feedback_type_delete(id).getString("result");
            Preconditions.checkArgument(result.equals("true"), "状态码期待true，实际" + result);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建反馈类型、删除反馈类型");
        }
    }

    //编辑反馈类型
    @Test()
    public void updatefeedback() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String feedbackType = "反馈类型111";
            String feedbackType0 = "反馈类型11111";
            md.feedback_add(feedbackType, EnumDesc.DESC_BETWEEN_5_10.getDesc());
            Integer total = md.feedList(null, 1, 10).getInteger("total");
            int a = total - 1;
            String id = md.feedList(null, 1, 10).getJSONArray("list").getJSONObject(a).getString("id");
            md.updateFeedbackType(id, feedbackType0, EnumDesc.DESC_BETWEEN_5_10.getDesc());
            JSONArray arr = md.feedList(null, 1, 10).getJSONArray("list");
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if (obj.getString("id").equals(id)) {
                    Preconditions.checkArgument(obj.getString("feedback_type").equals(feedbackType0), "修改前名字是" + feedbackType + "，期望修改为" + feedbackType0 + "，实际修改后为" + obj.getString("feedback_type"));
                }
            }
            int id1 = Integer.parseInt(id);
            md.feedback_type_delete(id1);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("编辑反馈类型");
        }
    }

    //内容管理通过内容标题进行搜索
    @Test()
    public void articleSearch() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String name = "推荐活动";
            JSONArray memList = md.article_page(name, null, null, null, null, 1, 10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String title = memList.getJSONObject(i).getString("title");
                    Preconditions.checkArgument(title.equals(name), "通过" + name + "搜索内容管理展示结果为" + title);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容管理通过内容标题进行搜索");
        }
    }

    //内容管理通过内容标题进行搜索
    @Test()
    public void articleSearch1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String start = "2021-04-09";
            String end = "2021-04-09";
            JSONArray memList = md.article_page(null, start, end, null, null, 1, 10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String create_start = memList.getJSONObject(i).getString("create_time");
                    Preconditions.checkArgument(create_start.contains(start), "通过" + start + "和" + end + "搜索内容管理展示结果为" + create_start);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容管理通过创建时间进行搜索");
        }
    }

    //内容管理通过更新时间进行搜索
    @Test()
    public void articleSearch2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String start = "2021-04-09";
            String end = "2021-04-09";
            JSONArray memList = md.article_page(null, null, null, start, end, 1, 10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String modify_start = memList.getJSONObject(i).getString("modify_time");
                    Preconditions.checkArgument(modify_start.contains(start), "通过" + start + "和" + end + "搜索内容管理展示结果为" + modify_start);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容管理通过更新时间进行搜索");
        }
    }

    //内容管理通过更新时间和创建时间进行搜索
    @Test()
    public void articleSearch3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String start = "2021-04-09";
            String end = "2021-04-09";
            JSONArray memList = md.article_page(null, start, end, start, end, 1, 10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String create_start = memList.getJSONObject(i).getString("create_time");
//                    String create_end = memList.getJSONObject(i).getString("create_end");
                    String modify_start = memList.getJSONObject(i).getString("modify_time");
//                    String modify_end = memList.getJSONObject(i).getString("modify_end");
                    Preconditions.checkArgument(modify_start.contains(start) && create_start.contains(start), "通过" + start + "和" + end + "搜索内容管理展示结果为" + create_start + "和" + modify_start);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容管理通过更新时间和创建时间进行搜索");
        }
    }

    //内容管理通过内容标题和更新时间和创建时间进行搜索
    @Test()
    public void articleSearch4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String name = "推荐活动";
            String start = "2021-04-09";
            String end = "2021-04-09";
            JSONArray memList = md.article_page(name, start, end, start, end, 1, 10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String title = memList.getJSONObject(i).getString("title");
                    String create_start = memList.getJSONObject(i).getString("create_time");
//                    String create_end = memList.getJSONObject(i).getString("create_end");
                    String modify_start = memList.getJSONObject(i).getString("modify_time");
//                    String modify_end = memList.getJSONObject(i).getString("modify_end");
                    Preconditions.checkArgument(title.contains(name) && modify_start.contains(start) && create_start.contains(start), "通过" + start + "和" + end + "搜索内容管理展示结果为" + create_start + "和" + modify_start);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容管理通过内容标题和更新时间和创建时间进行搜索");
        }
    }

    //新建内容一张大图
    @Test()
    public void create_message() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/multimedia/picture/INS.jpg";
            String base64 = MendianInfoOnline.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
            JSONArray piclist = new JSONArray();
            piclist.add(path);
            JSONObject res = md.article_export("活动活动", "ONE_BIG", "1", "PREFERENTIAL", piclist);
            Preconditions.checkArgument(res.getInteger("code") == 1000, "状态码期待1000，实际" + res.getInteger("code"));
            Long id = res.getJSONObject("data").getLong("id");
            JSONObject res1 = md.article_status_change(id);
            Preconditions.checkArgument(res1.getInteger("code") == 1000, "状态码期待1000，实际" + res1.getInteger("code"));
            JSONObject res2 = md.article_delete(id);
            Preconditions.checkArgument(res2.getInteger("code") == 1000, "状态码期待1000，实际" + res2.getInteger("code"));
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建内容一张大图并删除");
        }
    }

    //新建内容三张小图
    @Test()
    public void create_message1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/multimedia/picture/INS.jpg";
            String base64 = MendianInfoOnline.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
            JSONArray piclist = new JSONArray();
            piclist.add(path);
            piclist.add(path);
            piclist.add(path);
            JSONObject res = md.article_export("活动活动", "THREE", "1", "RED_PAPER", piclist);
            Preconditions.checkArgument(res.getInteger("code") == 1000, "状态码期待1000，实际" + res.getInteger("code"));
            Long id = res.getJSONObject("data").getLong("id");
            JSONObject res1 = md.article_status_change(id);
            Preconditions.checkArgument(res1.getInteger("code") == 1000, "状态码期待1000，实际" + res1.getInteger("code"));
            JSONObject res2 = md.article_delete(id);
            Preconditions.checkArgument(res2.getInteger("code") == 1000, "状态码期待1000，实际" + res2.getInteger("code"));
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建内容三张小图并删除");
        }
    }

    //新建内容左一图
    @Test()
    public void create_message2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/multimedia/picture/INS.jpg";
            String base64 = MendianInfoOnline.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
            JSONArray piclist = new JSONArray();
            piclist.add(path);
            JSONObject res = md.article_export("活动活动", "ONE_LEFT", "1", "RED_PAPER", piclist);
            Preconditions.checkArgument(res.getInteger("code") == 1000, "状态码期待1000，实际" + res.getInteger("code"));
            Long id = res.getJSONObject("data").getLong("id");
            JSONObject res1 = md.article_status_change(id);
            Preconditions.checkArgument(res1.getInteger("code") == 1000, "状态码期待1000，实际" + res1.getInteger("code"));
            JSONObject res2 = md.article_delete(id);
            Preconditions.checkArgument(res2.getInteger("code") == 1000, "状态码期待1000，实际" + res2.getInteger("code"));
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建内容左一图并删除");
        }
    }

    //新建内容异常项
    @Test()
    public void create_message3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/multimedia/picture/INS.jpg";
            String base64 = MendianInfoOnline.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
            JSONArray piclist = new JSONArray();
            piclist.add(path);
            //新建内容无图片
            String message = md.article_export("活动活动", "ONE_LEFT", "1", "RED_PAPER", null).getString("message");
            Preconditions.checkArgument(message.equals("图片不能为空"), "期待图片不能为空，实际" + message);
            //新建内容标题小于四个字
            String message1 = md.article_export("活动", "ONE_LEFT", "1", "RED_PAPER", piclist).getString("message");
            Preconditions.checkArgument(message1.equals("文章标题长度范围为[4,20]"), "期待文章标题长度范围为[4,20]，实际" + message1);
            //内容详情为空
            String message2 = md.article_export("活动活动", "ONE_LEFT", null, "RED_PAPER", piclist).getString("message");
            Preconditions.checkArgument(message2.equals("内容详情不能为空"), "期待实际内容详情不能为空，实际" + message2);
            //文章样式为空
            String message3 = md.article_export("活动活动", "ONE_LEFT", "1", null, piclist).getString("message");
            Preconditions.checkArgument(message3.equals("文章标签不能为空"), "期待文章标签不能为空，实际" + message3);
            //文章标签
            String message4 = md.article_export("活动活动", "ONE_LEFT", "1", null, piclist).getString("message");
            Preconditions.checkArgument(message4.equals("文章标签不能为空"), "期待文章标签不能为空，实际" + message4);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建内容异常项");
        }
    }

    //口味管理通过口味名称进行搜索
    @Test()
    public void tasteSearch() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索门店
            String name = "口味排行";
            JSONArray memList = md.taste_search(name, 1, 10).getJSONArray("list");
            if (memList != null)
                for (int i = 0; i < memList.size(); i++) {
                    String taste_name = memList.getJSONObject(i).getString("taste_name");
                    Preconditions.checkArgument(taste_name.equals(name), "通过" + name + "搜索口味管理展示结果为" + taste_name);
                }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("口味管理通过口味名称进行搜索");
        }
    }

    //新建口味、删除口味
    @Test()
    public void create_taste() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/multimedia/picture/INS.jpg";
            String base64 = MendianInfoOnline.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
//            JSONArray piclist = new JSONArray();
//            piclist.add(path);
            JSONObject res = md.taste_add(path, path, path, "芒果", "1", 1, true);
            Preconditions.checkArgument(res.getInteger("code") == 1000, "状态码期待1000，实际" + res.getInteger("code"));
            int total = md.taste_search(null, 1, 100).getInteger("total");
            int a = total - 1;
            int id = md.taste_search(null, 1, 100).getJSONArray("list").getJSONObject(a).getInteger("id");
//            JSONArray list = md.
            int code = md.taste_delete(id).getInteger("code");
            Preconditions.checkArgument(code == 1000, "状态码期待1000，实际" + code);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建口味、删除口味");
        }
    }

    //编辑口味
    @Test()
    public void update_taste() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/multimedia/picture/INS.jpg";
            String base64 = MendianInfoOnline.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
//            JSONArray piclist = new JSONArray();
//            piclist.add(path);
            String name = "芒果";
            String name1 = "香蕉";
            JSONObject res = md.taste_add(path, path, path, name, "1", 1, true);
            Preconditions.checkArgument(res.getInteger("code") == 1000, "状态码期待1000，实际" + res.getInteger("code"));
            int total = md.taste_search(null, 1, 100).getInteger("total");
            int a = total - 1;
            int id = md.taste_search(null, 1, 100).getJSONArray("list").getJSONObject(a).getInteger("id");
            md.updateTaste(id, path, path, path, name1, "1", 1, true);
            JSONArray arr = md.taste_search(null, 1, 100).getJSONArray("list");
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if (obj.getInteger("id") == (id)) {
                    Preconditions.checkArgument(obj.getString("taste_name").equals(name1), "修改前名字是" + name + "，期望修改为" + name1 + "，实际修改后为" + obj.getString("taste_name"));
                }
            }
            int code = md.taste_delete(id).getInteger("code");
            Preconditions.checkArgument(code == 1000, "状态码期待1000，实际" + code);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("编辑口味");
        }
    }

    //创建口味的异常情况
    @Test()
    public void add_taste1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/multimedia/picture/INS.jpg";
            String base64 = MendianInfoOnline.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
            String name = "芒果";
            //不传taste——imagepath
            JSONObject res = md.taste_add(null, path, path, name, "1", 1, true);
            Preconditions.checkArgument(res.getString("message").equals("口味图片不能为空"), "期待口味图片不能为空，实际" + res.getString("message"));
            JSONObject res0 = md.taste_add(path, null, path, name, "1", 1, true);
            Preconditions.checkArgument(res0.getString("message").equals("头部图片不能为空"), "期待头部图片不能为空，实际" + res0.getString("message"));
            JSONObject res1 = md.taste_add(path, path, null, name, "1", 1, true);
            Preconditions.checkArgument(res1.getString("message").equals("展示图片不能为空"), "期待展示图片不能为空，实际" + res1.getString("message"));
            JSONObject res2 = md.taste_add(path, path, path, EnumDesc.DESC_BETWEEN_20_30.getDesc(), "1", 1, true);
            Preconditions.checkArgument(res2.getString("message").equals("口味名称不能超过10个字"), "期待口味名称不能超过10个字，实际" + res2.getString("message"));
            JSONObject res3 = md.taste_add(path, path, path, EnumDesc.DESC_BETWEEN_5_10.getDesc(), EnumDesc.DESC_BETWEEN_400_500.getDesc(), 1, true);
            Preconditions.checkArgument(res3.getString("message").equals("口味描述不能超过200个字"), "期待口味描述不能超过200个字，实际" + res3.getString("message"));
//            JSONObject res4 = md.taste_add(path,path,path,EnumDesc.DESC_BETWEEN_5_10.getDesc(),EnumDesc.DESC_BETWEEN_15_20.getDesc(),null,null);
//            Preconditions.checkArgument(res4.getString("message").equals("口味描述不能超过200个字"),"期待口味描述不能超过200个字，实际"+res4.getString("message"));
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建口味的异常情况");
        }
    }

    //创建评论删除评论
    @Test()
    public void create_comment() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/multimedia/picture/INS.jpg";
            String base64 = MendianInfoOnline.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
            JSONArray piclist = new JSONArray();
            piclist.add(path);
            md.taste_add(path, path, path, "芒果", "1", 1, true);

            int total = md.taste_search(null, 1, 100).getInteger("total");
            int a = total - 1;
            int id = md.taste_search(null, 1, 100).getJSONArray("list").getJSONObject(a).getInteger("id");
            JSONObject res = md.taste_add_comment(id, path, "1234", "1234", 4, true, piclist);
            Preconditions.checkArgument(res.getInteger("code") == 1000, "期待展示1000，实际" + res.getInteger("code"));
            int t = md.taste_search_comment(id, 1, 100, null).getInteger("total");
            int t2 = t - 1;
            int tid = md.taste_search_comment(id, 1, 100, null).getJSONArray("list").getJSONObject(t2).getInteger("id");
            md.deleteComment(tid);
//            JSONArray list = md.
            int code = md.taste_delete(id).getInteger("code");
            Preconditions.checkArgument(code == 1000, "状态码期待1000，实际" + code);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建评论删除评论");
        }
    }


    //新建广告位

    //bug
//    @Test(description = "banner--跳转活动/文章的条数=展示中的文章+进行中或者已结束活动条数之和")
//    public void banner_data_1() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            int num = ArticleList.builder().build().invoke(visitor).getJSONArray("list").size();
//            IScene articlePageScene = ArticlePageScene.builder().build();
//            int articlePageListSize = (int) util.collectBean(articlePageScene, ArticlePageBean.class).stream().filter(e -> e.getStatusName().equals(ArticleStatusEnum.SHOW.getTypeName())).count();
//            CommonUtil.checkResult("跳转活动/文章的条数", articlePageListSize, num);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("banner--跳转活动/文章的条数=展示中的文章+进行中活动条数之和");
//        }
//    }
//
//    //ok
//    @Test(description = "内容运营--banner--填写banner1-banner5的内容")
//    public void banner_data_2() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            List<Long> articleIds = util.getArticleIdList();
//            String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture";
//            File file = new File(filePath);
//            File[] files = file.listFiles();
//            assert files != null;
//            List<String> base64s = Arrays.stream(files).filter(e -> e.toString().contains("banner")).map(e -> new ImageUtil().getImageBinary(e.getPath())).collect(Collectors.toList());
//            List<String> picPaths = base64s.stream().map(e -> visitor.invokeApi(FileUpload.builder().pic(e).permanentPicType(0).isPermanent(false).ratio(1.5).ratioStr("3：2").build()).getString("pic_path")).collect(Collectors.toList());
//            JSONArray array = new JSONArray();
//            JSONObject jsonObject1 = new JSONObject();
//            jsonObject1.put("article_id", articleIds.get(0));
//            jsonObject1.put("banner_img_url", picPaths.get(0));
//            jsonObject1.put("banner_id", 31);
//            jsonObject1.put("banner_select", "banner1");
//            JSONObject jsonObject2 = new JSONObject();
//            jsonObject2.put("article_id", articleIds.get(1));
//            jsonObject2.put("banner_img_url", picPaths.get(1));
//            jsonObject2.put("banner_id", 87);
//            jsonObject2.put("banner_select", "banner2");
//            JSONObject jsonObject3 = new JSONObject();
//            jsonObject3.put("article_id", articleIds.get(2));
//            jsonObject3.put("banner_img_url", picPaths.get(2));
//            jsonObject3.put("banner_id", 88);
//            jsonObject3.put("banner_select", "banner3");
//            JSONObject jsonObject4 = new JSONObject();
//            jsonObject4.put("article_id", articleIds.get(3));
//            jsonObject4.put("banner_img_url", picPaths.get(3));
//            jsonObject4.put("banner_id", 89);
//            jsonObject4.put("banner_select", "banner4");
//            JSONObject jsonObject5 = new JSONObject();
//            jsonObject5.put("article_id", articleIds.get(4));
//            jsonObject5.put("banner_img_url", picPaths.get(4));
//            jsonObject5.put("banner_id", 90);
//            jsonObject5.put("banner_select", "banner5");
//            array.add(jsonObject1);
//            array.add(jsonObject2);
//            array.add(jsonObject3);
//            array.add(jsonObject4);
//            array.add(jsonObject5);
////            articleIds.add(articleIds.get(2));
////            articleIds.add(articleIds.get(2));
//            EditScene.builder().list(array).adName("首页banner").adType("BANNER").build().invoke(visitor);
//            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
//            JSONArray list = AppletBannerScene.builder().adType("BANNER").build().invoke(visitor).getJSONArray("list");
//            List<Long> appletArticleIds = list.stream().map(e -> (JSONObject) e).map(e -> e.getLong("article_id")).collect(Collectors.toList());
//            CommonUtil.checkResultPlus("pc端文章为：", appletArticleIds, "applet端文章为：", articleIds.subList(0, 5));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("内容运营--banner--填写banner1-banner5的内容");
//        }
//    }

//    ------------------------------------------------------Ins小程序-----------------------------------------------------------------------------------------------------------------
    //小程序附近门店
//    @Test()
//    public void nearshop() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
//            JSONArray list = wx.nearshop(null,"赢识",116.29845,39.95933).getJSONArray("list");
//            for (int i=0;i<list.size();i++){
//                String shop_name = list.getJSONObject(i).getString("shop_name");
//                Preconditions.checkArgument(shop_name.contains("赢识"),"期待门店包含赢识，实际"+shop_name);
//            }
//        } catch (AssertionError | Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("小程序附近门店");
//        }
//    }
//
//    //小程序用户反馈
//    @Test()
//    public void userfeedback() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
//            JSONArray querylist = wx.queryAll().getJSONArray("list");
//            int feedback_type_id = querylist.getJSONObject(0).getInteger("feedback_type_id");
//            int code = wx.submitFeedback(feedback_type_id,5,"自动化提交用户反馈").getInteger("code");
//            Preconditions.checkArgument(code==1000,"期待状态码1000，实际"+code);
//            xd.login("storedemo@winsense.ai","b0581aa73b04d9fe6e3057a613e6f363");
//            int total = md.feedbackList(null,null,1,10).getInteger("total");
//            int a = total-1;
//            int id = md.feedbackList(null,null,1,100).getJSONArray("list").getJSONObject(a).getInteger("id");
//            md.addGift(id,"阿斯顿马丁");
//            String name = md.feedbackList(null,null,1,100).getJSONArray("list").getJSONObject(a).getString("user_name");
//            String feedback_gift = md.feedbackList(null,null,1,100).getJSONArray("list").getJSONObject(a).getString("feedback_gift");
//
//            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
//            JSONArray feedlist = wx.awardFeedback(null).getJSONArray("list");
//            String appletname = feedlist.getJSONObject(0).getString("user_name");
//            String appletgift = feedlist.getJSONObject(0).getString("feedback_gift");
//            Preconditions.checkArgument(name.equals(appletname)&&feedback_gift.equals(appletgift),"pc的用户昵称是"+name+"添加的礼物是"+feedback_gift+"小程序接受到用户昵称是"+appletname+"接受的礼物是"+appletgift);
//            xd.login("storedemo@winsense.ai","b0581aa73b04d9fe6e3057a613e6f363");
//            int code1 = md.feedback_delete(id).getInteger("code");
//            Preconditions.checkArgument(code1==1000,"期待状态码1000，实际"+code1);
//        } catch (AssertionError | Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("小程序用户反馈");
//        }
//    }
//    //小程序用户反馈异常情况
//    @Test()
//    public void userfeedbackerror() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
//            JSONArray querylist = wx.queryAll().getJSONArray("list");
//            int feedback_type_id = querylist.getJSONObject(0).getInteger("feedback_type_id");
//
//            //不添加反馈类型
//            JSONObject res = wx.submitFeedback(null,5,"自动化提交用户反馈");
//            Preconditions.checkArgument(res.getString("message").equals("反馈类型ID不能为空"),"期待反馈类型ID不能为空，实际"+res.getString("message"));
////            int code =  wx.submitFeedback(null,5,"自动化提交用户反馈").getInteger("code");
////            Preconditions.checkArgument(code==1001,"期待状态码1000，实际"+code);
////            Preconditions.checkArgument(res.getInteger("code")==1001,"期待状态码1000，实际"+res.getInteger("code"));
//
//
//
//
//
////            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
////            int total = md.feedbackList(null,null,1,10).getInteger("total");
////            int a = total-1;
////            int id = md.feedbackList(null,null,1,10).getJSONArray("list").getJSONObject(a).getInteger("id");
////            int code1 = md.feedback_delete(id).getInteger("code");
////            Preconditions.checkArgument(code1==1000,"期待状态码1000，实际"+code1);
//        } catch (AssertionError | Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("小程序用户反馈");
//        }
//    }
}
