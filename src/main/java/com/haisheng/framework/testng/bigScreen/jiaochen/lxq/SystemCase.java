package com.haisheng.framework.testng.bigScreen.jiaochen.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.config.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.jiaoChenInfo;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.PublicParm;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.pcCreateGoods;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author : lxq
 * @date :  2020/11/24
 */

public class SystemCase extends TestCaseCommon implements TestCaseStd {


    ScenarioUtil jc = ScenarioUtil.getInstance();
    jiaoChenInfo info = new jiaoChenInfo();
    PublicParm pp = new PublicParm();
    String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
    CommonConfig commonConfig = new CommonConfig();

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "吕雪晴";


        commonConfig.referer = EnumTestProduce.JC_DAILY.getReferer();
        commonConfig.product = EnumTestProduce.JC_DAILY.name();

        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JC_DAILY.getDesc() + commonConfig.checklistQaOwner);

        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = "-1";
        commonConfig.roleId = "603";
//        commonConfig.shopId = "46439";
        beforeClassInit(commonConfig);

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

        jc.pcLogin("13114785236", "000000");
    }

    /**
     *    --------------------- V1.0 --------------------------
     */

    /**
     * PC 品牌管理-系统测试
     */


    //品牌--正常
    @Test //ok
    public void addBrand_name1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int code = jc.addBrandNotChk(info.stringone, info.getLogo()).getInteger("code");
            Preconditions.checkArgument(code == 1000, "状态码期待1000，实际" + code);

            //删除品牌
            Long id = jc.brandPage(1, 10, "", "").getJSONArray("list").getJSONObject(0).getLong("id");
            jc.delBrand(id);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
//            saveData("PC【品牌管理】，创建品牌，名称1个字");
        }

    }

    @Test //ok
    public void addBrand_name10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int code = jc.addBrandNotChk(info.stringten, info.getLogo()).getInteger("code");
            Preconditions.checkArgument(code == 1000, "状态码期待1000，实际" + code);

            //删除品牌
            Long id = jc.brandPage(1, 10, "", "").getJSONArray("list").getJSONObject(0).getLong("id");
            jc.delBrand(id);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建品牌，名称10个字");
        }

    }

    @Test //ok
    public void editBrand_name() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建一个品牌
            String name1 = info.stringsix;
            String name2 = info.stringsix + "aaa";
            jc.addBrand(name1, info.getLogo());
            //获取创建的品牌id
            Long id = jc.brandPage(1, 10, "", "").getJSONArray("list").getJSONObject(0).getLong("id");
            //修改这个品牌的名字
            jc.editBrand(id, name2, info.getLogo());
            //根据id查询，名字为name2
            JSONArray arr = jc.brandPage(1, 100, "", "").getJSONArray("list");
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if (obj.getLong("id") == id) {
                    Preconditions.checkArgument(obj.getString("name").equals(name2), "修改前名字是" + name1 + "，期望修改为" + name2 + "，实际修改后为" + obj.getString("name"));
                }
            }

            //删除品牌
            jc.delBrand(id);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建品牌后进行修改");
        }

    }

    //品牌--异常
    @Test //ok bug5364 已解决
    public void addBrand_nameerr() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = "12345aA啊！@1";
            int code = jc.addBrandNotChk(name, info.getLogo()).getInteger("code");
            Preconditions.checkArgument(code == 1001, "状态码期待1001，实际" + code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建品牌，名称11个字");
        }

    }

    //品牌车系--正常

    @Test(dataProvider = "CAR_STYLE") // OK bug5362 已解决
    public void addCarStyle(String manufacturer, String name, String online_time) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int code = jc.addCarStyleNotChk(info.BrandID, manufacturer, name, online_time).getInteger("code");
            Preconditions.checkArgument(code == 1000, "创建车系：生产商 " + manufacturer + ", 车系 " + name + ", 上线日期" + online_time + "状态码" + code);

            //删除品牌车系
            Long id = jc.carStylePage(1, 1, info.BrandID, "").getJSONArray("list").getJSONObject(0).getLong("id");
            jc.delCarStyle(id);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车系");
        }
    }

    @Test //ok bug 5369 已修复
    public void editCarStyle() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //创建车系
            String manufacturer = "旧生产商"+Integer.toString((int)((Math.random()*9+1)*100));
            String name = "旧车系"+Integer.toString((int)((Math.random()*9+1)*100));
            String online_time = dt.getHistoryDate(0);
            jc.addCarStyle(info.BrandID, manufacturer, name, online_time);
            //获取车系id
            Long id = jc.carStylePage(1, 1, info.BrandID, name).getJSONArray("list").getJSONObject(0).getLong("id");
            //修改车系
            String manufacturer1 = "新生产商"+Integer.toString((int)((Math.random()*9+1)*100));
            String name1 = "新车系"+Integer.toString((int)((Math.random()*9+1)*100));
            String online_time1 = dt.getHistoryDate(-2);
            jc.editCarStyle(id, info.BrandID, manufacturer1, name1, online_time1);
            //查看修改结果

            JSONArray arr = jc.carStylePage(1, 30, info.BrandID, "").getJSONArray("list");
            for (int i = 0 ; i < arr.size();i++){
                JSONObject obj = arr.getJSONObject(i);
                if (obj.getLong("id").longValue() == id){
                    String search_manufacturer1 = obj.getString("manufacturer");
                    String search_name1 = obj.getString("name");
                    String search_online_time1 = obj.getString("online_time");

                    Preconditions.checkArgument(search_manufacturer1.equals(manufacturer1), "修改前生产商=" + manufacturer + "，期望修改为" + manufacturer1 + "，实际修改后为" + search_manufacturer1);
                    Preconditions.checkArgument(search_name1.equals(name1), "修改前车系=" + name + "，期望修改为" + name1 + "，实际修改后为" + search_name1);
                    Preconditions.checkArgument(search_online_time1.equals(online_time1), "修改前上线时间=" + online_time + "，期望修改为" + online_time1 + "，实际修改后为" + search_online_time1);

                }
            }


            //删除品牌车系
            jc.delCarStyle(id);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，修改车系");
        }
    }

    @Test  //ok
    public void addOneCarStyleinTwoModel() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //创建品牌1
            Long brandid1 = info.getBrandID(5);
            //创建品牌2
            Long brandid2 = info.getBrandID(6);

            //创建车系
            String manufacturer = "生产商重复";
            String name = "车系重复";
            String online_time = dt.getHistoryDate(0);
            jc.addCarStyle(brandid1, manufacturer, name, online_time);

            int code = jc.addCarStyleNotChk(brandid2, manufacturer, name, online_time).getInteger("code");
            Preconditions.checkArgument(code == 1000, "期待状态码1000，实际" + code);

            //删除品牌
            jc.delBrand(brandid1);
            jc.delBrand(brandid2);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，不同品牌下创建相同车系，期待成功");
        }
    }


    @DataProvider(name = "CAR_STYLE")
    public Object[] carStyle() {
        return new String[][]{
                {info.stringone, info.stringone, dt.getHistoryDate(0)},
                {info.stringfifty, info.stringfifty, dt.getHistoryDate(-1)},
                {info.stringsix, info.stringsix, dt.getHistoryDate(1)},
        };
    }

    //品牌车系--异常
    @Test(dataProvider = "CAR_STYLEERR") // OK bug 5371 已解决
    public void addCarStyleErr(String manufacturer, String name, String online_time, String yz) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject obj = jc.addCarStyleNotChk(info.BrandID, manufacturer, name, online_time);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, yz + "期待状态码1001，实际" + code + ",提示语：" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车系，生产商/车系51字");
        }
    }

    @DataProvider(name = "CAR_STYLEERR")
    public Object[] carStyle_err() {
        return new String[][]{
                {info.stringfifty1, info.stringone, dt.getHistoryDate(0), "生产商51个字"},
                {info.stringfifty, info.stringfifty1, dt.getHistoryDate(-1), "车系51个字"},

        };
    }

    @Test //ok
    public void addTwoCarStyleinOneModel() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //创建品牌1
            Long brandid1 = info.getBrandID(5);

            //创建车系
            String manufacturer = "生产商重复";
            String name = "车系重复";
            String online_time = dt.getHistoryDate(0);

            jc.addCarStyle(brandid1, manufacturer, name, online_time);

            JSONObject obj = jc.addCarStyleNotChk(brandid1, manufacturer, name, online_time);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待状态码1001，实际" + code + ",提示语：" + message);


            //删除品牌
            jc.delBrand(brandid1);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，相同品牌下创建相同车系，期待失败");
        }
    }

    @Test //bug 5376 提示语不正确
    public void addCarStyleinNotExistModel() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //创建品牌1
            Long brandid1 = info.getBrandID(5);

            //删除品牌
            jc.delBrand(brandid1);

            //添加车系
            String manufacturer = "自动化" + System.currentTimeMillis();
            String name = "自动化" + System.currentTimeMillis();
            String online_time = dt.getHistoryDate(0);
            JSONObject obj = jc.addCarStyleNotChk(brandid1, manufacturer, name, online_time);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "实际状态码" + code + ", 提示语为：" + message);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，在某品牌下创建车系时，品牌被删除，期待失败");
        }
    }


    //品牌车系车型 --正常
    @Test(dataProvider = "CAR_MODEL") //ok
    public void addCarModel(String name, String year, String status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int code = jc.addCarModelNotChk(info.BrandID, info.CarStyleID, name, year, status).getInteger("code");
            Preconditions.checkArgument(code == 1000, "创建车系：车型名称 " + name + ", 年款 " + year + ", 预约状态" + status + "状态码" + code);

            //删除品牌车系
            Long id = jc.carModelPage(1, 1, info.BrandID, info.CarStyleID, name, "", "").getJSONArray("list").getJSONObject(0).getLong("id");
            jc.delCarModel(id);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车型");
        }
    }

    @DataProvider(name = "CAR_MODEL")
    public Object[] carModel() {
        return new String[][]{
                {"x", "1", "ENABLE"},
                {info.stringfifty, "2000年", "ENABLE"},
                {info.stringsix, "12345啊啊啊啊啊！@#qweQWER", "DISABLE"},
        };
    }

    @Test
    public void editCarModel() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //创建车型
            String name1 = "旧车型名称" + System.currentTimeMillis();
            String year1 = "2000年";
            String status1 = "ENABLE";
            jc.addCarModel(info.BrandID, info.CarStyleID, name1, year1, status1);
            //获取车系id
            int size = jc.carModelPage(1, 1, info.BrandID, info.CarStyleID, name1, "", "").getInteger("total");
            Long id = jc.carModelPage(1, size, info.BrandID, info.CarStyleID, name1, "", "").getJSONArray("list").getJSONObject(size - 1).getLong("id");
            System.out.println(id + "---------");
            //修改车型
            String name2 = "新车型名称" + System.currentTimeMillis();
            String year2 = "2020年";
            String status2 = "DISABLE";
            jc.editCarModel(id, info.BrandID, info.CarStyleID, name2, year2, status2);
            //查看修改结果
            String search_name2 = "";
            String search_year2 = "";
            String search_status2 = "";
            int size1 = jc.carModelPage(1, 1, info.BrandID, info.CarStyleID, "", "", "").getInteger("total");
            JSONArray arr = jc.carModelPage(1, size1, info.BrandID, info.CarStyleID, "", "", "").getJSONArray("list");
            for (int i = size1 - 1; i > 0; i--) {
                JSONObject obj = arr.getJSONObject(i);
                System.out.println(obj + "-----------------");
                if (obj.getLong("id").longValue() == id.longValue()) {

                    search_name2 = obj.getString("name");
                    search_year2 = obj.getString("year");
                    search_status2 = obj.getString("status");
                }
            }


            Preconditions.checkArgument(search_name2.equals(name2), "修改前车型名称=" + name1 + "，期望修改为" + name2 + "，实际修改后为" + search_name2);
            Preconditions.checkArgument(search_year2.equals(year2), "修改前年款=" + year1 + "，期望修改为" + year2 + "，实际修改后为" + search_year2);
            Preconditions.checkArgument(search_status2.equals(status2), "修改前状态=" + status1 + "，期望修改为" + status2 + "，实际修改后为" + search_status2);

            //删除品牌车系车型
            jc.delCarModel(id);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，修改车型");
        }
    }

    //品牌车系车型 --异常
    @Test //OK bug 5389 已解决
    public void addCarModel_err() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String year = "1998年";
            String status = "ENABLE";
            JSONObject obj = jc.addCarModelNotChk(info.BrandID, info.CarStyleID, info.stringfifty1, year, status);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待状态码1001，实际" + code + "， 提示语：" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车型， 名称51字");
        }
    }

    //品牌车系车型 --异常
    @Test
    public void addCarModel_err3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String year = "12345啊啊啊啊啊！@#qweQWERQ";
            String status = "ENABLE";
            JSONObject obj = jc.addCarModelNotChk(info.BrandID, info.CarStyleID, info.stringsix, year, status);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待状态码1001，实际" + code + "， 提示语：" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车型， 年款21个字");
        }
    }

    @Test //ok
    public void addCarModel_err1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //新建品牌
            Long brandid = info.getBrandID(7);
            //新建车系
            Long carStyleId = info.getCarStyleID(brandid, 5);

            //删除车系
            jc.delCarStyle(carStyleId);

            //新建车型
            String name1 = "自动化" + System.currentTimeMillis();
            String year1 = "2019年";
            String status1 = "ENABLE";
            JSONObject obj = jc.addCarModelNotChk(brandid, carStyleId, name1, year1, status1);

            //删除品牌
            jc.delBrand(brandid);

            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "状态码为" + code + ", 提示语" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车型时车系被删除， 期待失败");
        }
    }

    @Test
    public void addCarModel_err2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //新建品牌
            Long brandid = info.getBrandID(7);
            //新建车系
            Long carStyleId = info.getCarStyleID(brandid, 5);

            //删除品牌
            jc.delBrand(brandid);
            //新建车型
            String name1 = "自动化" + System.currentTimeMillis();
            String year1 = "1009年";
            String status1 = "ENABLE";
            JSONObject obj = jc.addCarModelNotChk(brandid, carStyleId, name1, year1, status1);

            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "状态码为" + code + ", 提示语" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车型时品牌被删除， 期待失败");
        }
    }


    /**
     *    PC 门店管理-系统测试
     */


//    @Test //ok
//    public void addshop_rephone() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            String sale_tel = info.phone;
//            String service_tel = info.phone;
//
//            JSONArray arr = new JSONArray();
//            arr.add(info.BrandID);
//
//            jc.addShop(info.getLogo(),info.stringsix,info.stringsix,arr,info.district_code,info.stringsix,sale_tel,service_tel,
//                    129.8439,42.96805, "DISABLE","DISABLE");
//            int code = jc.addShopNotChk(info.getLogo(),info.stringsix+"1",info.stringsix+"1",arr,info.district_code,info.stringsix,sale_tel,service_tel,
//                    129.8439,42.96805, "DISABLE","DISABLE").getInteger("code");
//            Preconditions.checkArgument(code==1000,"期待状态码1000，实际"+ code);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【门店管理】，新建门店销售电话/售后电话与其他门店重复");
//        }
//    }
//
//    //门店管理--异常
//    @Test(dataProvider = "SHOPERR")
//    public void addshopErr(String simple_name, String name, String district_code, String adddress, String sale_tel, String service_tel,
//                        String longitude, String latitude, String appointment_status,String washing_status,String a) {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONArray arr = new JSONArray();
//            arr.add(info.BrandID);
//            int code = jc.addShopNotChk(info.getLogo(),simple_name,name,arr,district_code,adddress,sale_tel,service_tel,Double.valueOf(longitude),
//                    Double.valueOf(latitude),appointment_status,washing_status).getInteger("code");
//            Preconditions.checkArgument(code==1001,a+"期待状态码1001，实际"+ code);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【门店管理】，新建门店，参数不规范");
//        }
//    }
//
//    @DataProvider(name = "SHOPERR")
//    public  Object[] shop_err() {
//
//        return new String[][]{
//                {info.stringten+"1", info.stringone,info.district_code,info.stringone, info.phone,info.phone,"129.8439","42.96805","ENABLE","ENABLE","门店简称11字"},
//                {info.stringone, info.stringfifty1,info.district_code,info.stringfifty, info.phone,info.phone,"129.8439","42.96805","ENABLE","DISABLE","门店全称1字"},
//                {info.stringten, info.stringfifty,info.district_code,info.stringfifty1, info.phone,info.phone,"129.8439","42.96805","DISABLE","DISABLE","详细地址51字"},
////                {info.stringten, info.stringone,info.district_code,info.stringten, "11111111111",info.phone,"129.8439","42.96805","DISABLE","ENABLE","销售手机号11111111111"},
////                {info.stringone, info.stringfifty,info.district_code,info.stringten, "111111111111",info.phone,"129.8439","42.96805","DISABLE","DISABLE","销售手机号12位"},
////                {info.stringone, info.stringfifty,info.district_code,info.stringten, "1111111111",info.phone,"129.8439","42.96805","DISABLE","DISABLE","销售手机号10位"},
////                {info.stringten, info.stringone,info.district_code,info.stringten, info.phone,"11111111111","129.8439","42.96805","DISABLE","ENABLE","售后手机号11111111111"},
////                {info.stringone, info.stringfifty,info.district_code,info.stringten, info.phone,"111111111111","129.8439","42.96805","DISABLE","DISABLE","售后手机号12位"},
////                {info.stringone, info.stringfifty,info.district_code,info.stringten, info.phone,"1111111111","129.8439","42.96805","DISABLE","DISABLE","售后手机号10位"},
//                {info.stringone, info.stringfifty,info.district_code,info.stringten, info.phone,info.phone,"1298439","42.96805","DISABLE","DISABLE","经度1298439"},
//                {info.stringone, info.stringfifty,info.district_code,info.stringten, info.phone,info.phone,"129.8439","4296805","DISABLE","DISABLE","纬度4296805"},
//
//
//        };
//    }
//
//


    /**
     * PC 内容运营-系统测试
     */


    //新建文章
    @Test(dataProvider = "ARTICLE") //ok
    public void addArticle(String title, String pic_type, String content, String label) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray pic_list1 = new JSONArray();
            //pic_list1.add("general_temp/9c6fbc65-0f1f-4341-9892-1f1052b6aa04");
            pic_list1.add(info.getLogo());

            JSONArray pic_list2 = new JSONArray();
            pic_list2.add("");
            pic_list2.add("");
            pic_list2.add("");
            JSONObject obj = jc.addArticleNotChk(title, pic_type, pic_list1, content, label, "ARTICEL", null, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null);
            int code = obj.getInteger("code");
            Long id = obj.getJSONObject("data").getLong("id");
            //关闭文章
            jc.changeArticleStatus(id);
            //删除文章
            jc.delArticle(id);


            Preconditions.checkArgument(code == 1000, "期待1000，实际" + code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【内容运营】，新建文章1张图");
        }
    }

    @DataProvider(name = "ARTICLE") //要补充
    public Object[] article() {
        return new String[][]{
                {"1234", "ONE_BIG", info.stringone, "RED_PAPER"},
                {info.stringten, "ONE_BIG", info.stringfifty, "PREFERENTIAL"},
                {info.string20, "ONE_LEFT", info.stringten, "BARGAIN"},
                {info.stringten, "ONE_LEFT", info.stringlong, "WELFARE"},
                {info.stringsix, "ONE_LEFT", info.stringlong, "GIFT"},

        };
    }


    //@Test ID不一致
    public void ArticleTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建文章
            JSONObject obj = info.newArtical();
            Long id = obj.getLong("id");
            //置顶
            jc.topArticle(id);
            //登陆小程序查看
            jc.appletLoginToken(EnumAppletToken.JC_LXQ_DAILY.getToken());
            Long search_list = jc.appletArticleList(null, null).getJSONArray("list").getJSONObject(0).getLong("list");

            Preconditions.checkArgument(id == search_list, "置顶后不在小程序首位");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【内容运营】，置顶文章");
        }
    }


    /**
     * PC 页面级权限用例
     */

    @Test(dataProvider = "ROLE")
    public void addallRole(JSONArray idlist, List namelist, String phone, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Collections.sort(namelist);

            //创建角色和账号
            JSONObject obj = creatRoleAndAccount(idlist, namelist, phone);
            //角色id
            String roleId_str = obj.getString("roleid");
            //账号id
            String accountid = obj.getString("accountid");

            //账号登陆展示的页面权限
            JSONArray resources = jc.loginPC(phone, "000000").getJSONArray("resources");
            List login_auth = new ArrayList();
            for (int i = 0; i < resources.size(); i++) {
                login_auth.add(resources.getJSONObject(i).getString("resource_code"));
            }
            Collections.sort(login_auth);

            Preconditions.checkArgument(Iterators.elementsEqual(namelist.iterator(), login_auth.iterator()), mess + "不一致");

            //删除账号
            creatRoleAndAccount(roleId_str, accountid);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建不同角色的账号，校验页面权限");
        }
    }


    @DataProvider(name = "ROLE")
    public Object[] role() {
        return new Object[][]{
//                {info.allauth_id,info.allauth_list,"13402050010","全部页面权限"},
                {info.daoruauth_id, info.daoruauth_list, "13402050010", "导入记录权限所有页面；数据权限=全部；主体=品牌；无功能权限"},
                {info.jiedaiauth_id, info.jiedaiauth_list, "13402050011", "接待管理所有页面；数据权限=个人；主体类型权限=门店；全部功能权限"},
                {info.kehu12auth_id, info.kehu12auth_list, "13402050011", "客户管理+销售客户tab+售后客户tab；数据权限=全部；主体类型权限=集团；无功能权限"},
                {info.kehu123auth_id, info.kehu123auth_list, "13402050011", "客户管理全部页面；数据权限=全部；主体类型权限=集团；无功能权限"},
                {info.yuyue12auth_id, info.yuyue12auth_list, "13402050011", "预约管理+预约看板tab+预约记录tab；数据权限=全部；主体类型权限=门店；功能权限=售后接待+预约保养分配+预约应答人"},
                {info.xitong123auth_id, info.xitong123auth_list, "13402050011", "门店管理+品牌管理+品牌删除；数据权限=全部；主体类型权限=集团；无功能权限"},
                {info.xitong45auth_id, info.xitong45auth_list, "13402050010", "角色管理+员工管理；数据权限=全部；主体类型权限=区域；无功能权限"},
//                {info.kaquan45auth_id,info.kaquan45auth_list,"13402050014","卡券管理+核销人员tab+核销记录tab；数据权限=全部；主体类型权限=门店；无功能权限"},
//                {info.kaquan123auth_id,info.kaquan123auth_list,"13402050014","卡券管理+卡券表单tab+发卡记录tab+卡券申请页面；数据权限=全部；主体类型权限=品牌；无功能权限"},
                {info.taocannoauth_id, info.taocannoauth_list, "13402050017", "套餐管理+套餐表单tab+套餐购买记录tab+无确认支付按钮；数据权限=全部；主体类型权限=门店；无功能权限"},
                {info.taocanauth_id, info.taocanauth_list, "13402050017", "套餐管理+套餐表单tab+套餐购买记录tab+有确认支付按钮；数据权限=全部；主体类型权限=门店；无功能权限"},
//                {info.nxauth_id,info.nxauth_list,"13402050016","内容运营+消息管理；数据权限=全部；主体类型权限=集团；无功能权限"},

        };
    }

    /**
     *功能权限 -售后接待/预约保养分配/预约应答人/提醒接收人
     */

    /**
     *    PC 有接待管理页面，无售后接待功能权限，无法在app/PC进行接待 角色1
     */
//    @Ignore //前端置灰按钮，后端未做校验
//    @Test
//    public void recWithoutrole() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            JSONArray idlist= info.jiedai234auth_id;
//            List namelist=info.jiedai234auth_list;
//            String phone = "13412010069";
//
//            //创建角色和账号
//            JSONObject obj = creatRoleAndAccount(idlist,namelist,phone);
//            //角色id
//            String roleId_str = obj.getString("roleid");
//            //账号id
//            String accountid =  obj.getString("accountid");
//
//            //登陆PC，接待
//            jc.loginPC(phone,"000000");
//            int code = jc.pcManageReception("吉A123456",false).getInteger("code");
//            //登陆app，接待
//            jc.appLogin(phone,"000000");
//            int code2= jc.appReceptionAdmitcode("吉A123456").getInteger("code");
//
//            //删除账号
//            creatRoleAndAccount(roleId_str,accountid);
//
//            Preconditions.checkArgument(code==1001,"PC接待，状态码期待1001，实际"+code);
//            Preconditions.checkArgument(code2==1001,"APP接待，状态码期待1001，实际"+code);
//
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("有接待管理页面，无售后接待功能权限，在app/PC进行接待");
//        }
//    }

    /**
     *    PC 有售后客户页面，无售后接待功能权限，无法在PC售后客户导入工单 角色2
     */

    /**
     *    PC 有接待管理页面，无预约保养分配功能权限，小程序预约时服务顾问下拉无此人 角色3
     */


    /**
     *数据权限 -全部/个人
     */

    /**
     *    全部： 能看全部数据
     *    接待管理页-接待人 / 销售客户页-销售顾问 / 售后客户-客户经理 / 预约记录-客户经理 / 卡券表单-创建者 / 发卡记录-发券者 / 核销记录-核销账号 / 套餐管理-创建者 / 套餐购买记录-推荐人账号 / 卡券申请-申请人 /
     *    导入记录-操作员账号
     */

    /**
     * 个人： 能看自己的数据
     * 接待管理页-接待人 / 销售客户页-销售顾问 / 售后客户-客户经理 / 预约记录-客户经理 / 卡券表单-创建者 / 发卡记录-发券者 / 核销记录-核销账号 / 套餐管理-创建者 / 套餐购买记录-推荐人账号 / 卡券申请-申请人 /
     * 导入记录-操作员账号
     */


    public JSONObject creatRoleAndAccount(JSONArray idlist, List namelist, String phone) {
        JSONObject obj = new JSONObject();
        //新建角色
        String name = "zdh" + Integer.toString((int) (Math.random() * 100000));
        jc.roleAdd(name, idlist);
        //查询角色id
        String roleId_str = jc.organizationRolePage(name, 1, 10).getJSONArray("list").getJSONObject(0).getString("id");


        //创建账号

        jc.addAccount("zh" + Integer.toString((int) (Math.random() * 100000)), phone, Long.parseLong(roleId_str), name);
        String accountid = jc.pcStaffPage("", 1, 1).getJSONArray("list").getJSONObject(0).getString("id");

        obj.put("roleid", roleId_str);
        obj.put("accountid", accountid);
        return obj;
    }

    public void creatRoleAndAccount(String roleid, String accountid) {
        jc.pcLogin("13114785236", "000000");
        //删除账号
        jc.organizationAccountDelete(accountid);
        //删除角色
        jc.organizationidRoleDelete(roleid);
    }


    /**
     *    --------------------- V2.0 2020-01-20 --------------------------
     */

    /**
     * PC  商品管理 - 商品品类
     */

    //2021-01-22
    @Test
    public void categoryFilter1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = jc.categoryPage(1, 100, null, info.first_category, null, null).getJSONArray("list");
            JSONArray second = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);

                if (obj.getString("category_level").equals("二级品类")) {
                    second.add(obj.getString("category_name"));
                    String parent = obj.getString("parent_category");
                    Preconditions.checkArgument(parent.equals(info.first_category_chin), "筛选" + info.first_category_chin + "结果中的二级品类包含上级品类为" + parent);
                }
            }
            for (int j = 0; j < list.size(); j++) {
                JSONObject obj = list.getJSONObject(j);
                if (obj.getString("category_level").equals("三级品类")) {
                    String parent = obj.getString("parent_category");
                    Preconditions.checkArgument(second.contains(parent), "三级品类" + obj.getString("category_name") + "对应的二级品类" + parent + "不属于该一级品类");
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】根据一级品类单项筛选,结果期待为该品类下的全部二级/三级品类");
        }
    }

    @Test
    public void categoryFilter2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = jc.categoryPage(1, 100, null, info.first_category, info.second_category, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String parent = obj.getString("parent_category");
                String level = obj.getString("category_level");
                Preconditions.checkArgument(parent.equals(info.second_category_chin), "筛选" + info.second_category_chin + "结果包含上级品类为" + parent);
                Preconditions.checkArgument(level.equals("三级品类"), "结果包含" + level);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】根据一级品类和二级品类筛选,结果期待为该品类下的全部三级品类");
        }
    }

    @Test
    public void categoryFilter3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = jc.categoryPage(1, 100, true, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                Boolean status = obj.getBoolean("category_status");
                String name = obj.getString("category_name");
                Preconditions.checkArgument(status == true, "搜索状态为true，商品" + name + "的状态为false");
            }

            JSONArray list2 = jc.categoryPage(1, 100, false, null, null, null).getJSONArray("list");
            for (int i = 0; i < list2.size(); i++) {
                JSONObject obj = list2.getJSONObject(i);
                Boolean status = obj.getBoolean("category_status");
                String name = obj.getString("category_name");
                Preconditions.checkArgument(status == false, "搜索状态为false，商品" + name + "的状态为true");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】根据品类状态进行筛选");
        }
    }

    @Test
    public void categoryShow() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = jc.categoryPage(1, 50, null, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                int id = obj.getInteger("id");
                Preconditions.checkArgument(obj.containsKey("category_pic"), "品类" + id + "无品类图");
                Preconditions.checkArgument(obj.containsKey("category_name"), "品类" + id + "无品类名称");
                Preconditions.checkArgument(obj.containsKey("category_level"), "品类" + id + "无品类级别");
                Preconditions.checkArgument(obj.containsKey("num"), "品类" + id + "无商品数量");
                Preconditions.checkArgument(obj.containsKey("category_status"), "品类" + id + "无品类状态");
                Preconditions.checkArgument(obj.containsKey("last_modify_time"), "品类" + id + "无最新修改时间");
                Preconditions.checkArgument(obj.containsKey("modify_sale_name"), "品类" + id + "无修改人");
                if (!obj.getString("category_level").equals("一级品类")) {
                    Preconditions.checkArgument(obj.containsKey("parent_category"), "品类" + id + "无上级品类");
                }

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】列表中展示项校验");
        }
    }


    //2021-01-25
    @Test(dataProvider = "NAME")
    public void categoryAddFirst(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONObject obj = info.newFirstCategory(name);
            int code = obj.getInteger("code");
            Long id = obj.getLong("id");
            Preconditions.checkArgument(code == 1000, "新建状态码期待1000，实际" + code);

            //禁用品类
            jc.categoryChgStatus(id, false);

            //编辑品类-更换图片
            String logo2 = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");
            int code2 = jc.categoryEdit(false, id, name, "FIRST_CATEGORY", "", logo2).getInteger("code");
            Preconditions.checkArgument(code2 == 1000, "编辑重新上传图片状态码期待1000，实际" + code2);

            //编辑品类-不更换图片

            int code3 = jc.categoryEdit(false, id, name, "FIRST_CATEGORY", "", null).getInteger("code");
            Preconditions.checkArgument(code3 == 1000, "编辑不传图片状态码期待1000，实际" + code3);

            //删除启用品类
            jc.categoryDel(id, true);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】新建/编辑一级品类,品类名称1/5/10个字");
        }
    }

    @DataProvider(name = "NAME")
    public Object[] name() {
        return new String[]{
                "啊啊啊2",
                "12345",
                "1Aa啊！@#，嗷嗷",

        };
    }

    @Test
    public void categoryAddFirstErr1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject obj = info.newFirstCategory("12345678901");
            int code = obj.getInteger("code");
            Preconditions.checkArgument(code == 1001, "状态码期待1001，实际" + code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】新建一级品类,品类名称11个字");
        }
    }

    @Test(dataProvider = "NAME")
    public void categoryAddSecond(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //存在的一级品类
            Long idone = info.newFirstCategory(name).getLong("id");
            //新建二级品类
            int code = jc.categoryCreate(false, name, "SECOND_CATEGORY", Long.toString(idone), jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path"), null).getInteger("code");
            Preconditions.checkArgument(code == 1000, "期待成功，实际" + code);
            Long id = jc.categoryPage(1, 10, null, null, null, null).getJSONArray("list").getJSONObject(0).getLong("id");
            jc.categoryDel(id, false);
            jc.categoryDel(idone, true);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】新建二级品类,所属一级品类存在");
        }
    }

    @Test
    public void categoryAddSecondErr() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");
            int code = jc.categoryCreate(false, "name", "SECOND_CATEGORY", "99999", logo, null).getInteger("code");
            Preconditions.checkArgument(code == 1001, "状态码期待1001，实际" + code);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】新建二级品类,所属一级品类不存在");
        }
    }

    @Test(dataProvider = "CATEGORYID")
    public void categoryAdd2OR3Err(String level, String fatherid) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = info.stringsix + "aa";

            Long id = jc.categoryCreate(false, name, level, fatherid, info.getLogo(), null).getJSONObject("data").getLong("id");

            int code = jc.categoryCreate(false, name, level, fatherid, info.getLogo(), null).getInteger("code");
            Preconditions.checkArgument(code == 1001, "状态码期待1001，实际" + code);

            //删除品类
            jc.categoryDel(id, true);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】新建二级/三级品类,品类名称重复");
        }
    }

    @DataProvider(name = "CATEGORYID")
    public Object[] categroyid() {
        return new String[][]{
                {"SECOND_CATEGORY", Long.toString(info.first_category)},
                {"THIRD_CATEGORY", Long.toString(info.second_category)},

        };
    }

    @Test
    public void categoryAddSecondErr2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //存在的一级品类
            String nameone = "1-" + Integer.toString((int) ((Math.random() * 9 + 1) * 100));
            Long idone = info.newFirstCategory(nameone).getLong("id");
            //新建二级品类
            int code = jc.categoryCreate(false, nameone, "SECOND_CATEGORY", Long.toString(idone), jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path"), null).getInteger("code");
            Preconditions.checkArgument(code == 1000, "期待成功，实际" + code);
            Long id = jc.categoryPage(1, 10, null, null, null, null).getJSONArray("list").getJSONObject(0).getLong("id");
            jc.categoryDel(id, false);
            jc.categoryDel(idone, true);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】新建二级品类,品类名称与已存在的一级品类重复");
        }
    }


    @Test
    public void categoryAddErr() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");
            //不填写品类名称
            int code = jc.categoryCreate(false, null, "FIRST_CATEGORY", "", logo, null).getInteger("code");
            Preconditions.checkArgument(code == 1001, "不填写品类名称,状态码期待1001，实际" + code);


            //不填写所属分类

            int code2 = jc.categoryCreate(false, "不填写所属品类", "SECOND_CATEGORY", "", logo, null).getInteger("code");
            Preconditions.checkArgument(code2 == 1001, "不填写所属品类,状态码期待1001，实际" + code);

            //不选择logo
            int code3 = jc.categoryCreate(false, "不选择logo", "FIRST_CATEGORY", "", null, null).getInteger("code");
            Preconditions.checkArgument(code3 == 1001, "不选择logo,状态码期待1001，实际" + code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】新建品类,不填写必填项");
        }
    }

    @Test
    public void categoryStop1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建一级品类
            Long id = info.newFirstCategory("品类" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000))).getLong("id");
            //停用
            jc.categoryChgStatus(id, false);
            //删除
            int code = jc.categoryDel(id, false).getInteger("code");
            Preconditions.checkArgument(code == 1000, "删除失败，状态码" + code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】停用/删除无商品使用&无下级品类的品类,期待成功");
        }
    }

    @Test
    public void categoryStop2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建一级品类
            Long id = info.newFirstCategory("1品类" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000))).getLong("id");
            //新建一个二级品类
            Long secondid = info.newSecondCategory("2品类" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), Long.toString(id)).getLong("id");
            //停用
            jc.categoryChgStatus(secondid, false);
            jc.categoryChgStatus(id, false);

            //删除
            int code = jc.categoryDel(id, false).getInteger("code");
            Preconditions.checkArgument(code == 1000, "删除失败，状态码" + code);

            jc.categoryDel(secondid, true);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】停用/删除无商品使用&下级品类状态为停用的品类,期待成功");
        }
    }

    @Test
    public void categoryStop3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建一级品类
            Long id = info.newFirstCategory("1品类" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000))).getLong("id");
            //新建一个二级品类
            Long secondid = info.newSecondCategory("2品类" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), Long.toString(id)).getLong("id");
            //启用
            jc.categoryChgStatus(secondid, true);
            //停用
            int code = jc.categoryChgStatus(id, false, false).getInteger("code");
            Preconditions.checkArgument(code == 1001, "停用期待1001，实际" + code);

            //删除
            int code1 = jc.categoryDel(id, false).getInteger("code");
            Preconditions.checkArgument(code1 == 1001, "删除期待1001，实际" + code1);

            jc.categoryChgStatus(secondid, false);
            jc.categoryDel(secondid, true);
            jc.categoryDel(id, true);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】停用/删除无商品使用&下级品类状态为[启用]的品类,期待失败");
        }
    }


    @Test
    public void categoryDel1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建一级品类
            Long id = info.newFirstCategory("1品类" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000))).getLong("id");
            //绑定规格
            Long speId = jc.specificationsCreate("规格" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), id, null, null, true).getLong("id");

            //删除
            int code1 = jc.categoryDel(id, false).getInteger("code");
            Preconditions.checkArgument(code1 == 1001, "删除期待1001，实际" + code1);


            jc.specificationsChgStatus(speId, false);
            jc.specificationsDel(speId);
            jc.categoryDel(id, true);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】停用有规格绑定的品类,期待失败");
        }
    }

    @Test
    public void categoryStart1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建一级品类
            Long id = info.newFirstCategory("1品类" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000))).getLong("id");

            //启用
            int code = jc.categoryChgStatus(id, true, false).getInteger("code");
            Preconditions.checkArgument(code == 1000, "状态码" + code);

            //删除
            jc.categoryDel(id, true);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】启用无上级品类的品类（一级品类），期待成功");
        }
    }

    @Test
    public void categoryStart2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建一级品类
            Long id = info.newFirstCategory("1品类" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000))).getLong("id");
            //启用
            jc.categoryChgStatus(id, true);

            //新建二级品类
            Long secondid = info.newSecondCategory("2品类" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), Long.toString(id)).getLong("id");
            //启用
            int code1 = jc.categoryChgStatus(secondid, true, false).getInteger("code");
            Preconditions.checkArgument(code1 == 1000, "状态码" + code1);


            //删除
            jc.categoryDel(secondid, true);
            jc.categoryDel(id, true);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】启用 有上级品类&上级品类状态为启用的品类，期待成功");
        }
    }

    @Test
    public void categoryStart3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建一级品类
            Long id = info.newFirstCategory("1品类" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000))).getLong("id");
            //启用
            jc.categoryChgStatus(id, true);

            //新建二级品类
            Long secondid = info.newSecondCategory("2品类" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), Long.toString(id)).getLong("id");
            //停用一级品类
            jc.categoryChgStatus(secondid, false);
            jc.categoryChgStatus(id, false);

            //启用二级
            int code1 = jc.categoryChgStatus(secondid, true, false).getInteger("code");
            Preconditions.checkArgument(code1 == 1000, "状态码" + code1);


            //删除
            jc.categoryDel(secondid, true);
            jc.categoryDel(id, true);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】启用 有上级品类&上级品类状态为停用的品类，期待成功");
        }
    }


    //商品品牌
    @Test(dataProvider = "BRANDNAME")
    public void brandFilter1(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = jc.BrandPage(1, 50, name, null).getJSONArray("list");

            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String searchname = obj.getString("brand_name");
                Preconditions.checkArgument(searchname.contains(name), "搜索结果包含" + searchname);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品牌】根据品牌名称搜索");
        }
    }

    @DataProvider(name = "BRANDNAME")
    public Object[] brandName() {
        return new String[]{
                "1",
//                "aA",
//                "!@#$%^&*(-",
//                "自动化",
//                "之家",
//                "测试",
//                "2021",

        };
    }

    @Test
    public void brandFilter2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = jc.BrandPage(1, 50, null, true).getJSONArray("list");

            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                Boolean searchstatus = obj.getBoolean("brand_status");
                Preconditions.checkArgument(searchstatus == true, "搜索结果中" + obj.getString("brand_name") + "的状态为" + searchstatus);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品牌】搜索状态为启用的品牌");
        }
    }

    @Test
    public void brandFilter3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = jc.BrandPage(1, 50, null, false).getJSONArray("list");

            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                Boolean searchstatus = obj.getBoolean("brand_status");
                Preconditions.checkArgument(searchstatus == false, "搜索结果中" + obj.getString("brand_name") + "的状态为" + searchstatus);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品牌】搜索状态为停用的品牌");
        }
    }

    @Test
    public void brandShow() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = jc.BrandPage(1, 50, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                int id = obj.getInteger("id");
                Preconditions.checkArgument(obj.containsKey("brand_pic"), "品牌" + id + "品牌logo");
                Preconditions.checkArgument(obj.containsKey("brand_name"), "品牌" + id + "无品牌名称");
                Preconditions.checkArgument(obj.containsKey("brand_description"), "品牌" + id + "无品牌简介");
                Preconditions.checkArgument(obj.containsKey("brand_status"), "品牌" + id + "无品牌状态");
                Preconditions.checkArgument(obj.containsKey("num"), "品牌" + id + "无商品数量");
                Preconditions.checkArgument(obj.containsKey("last_modify_time"), "品牌" + id + "无最新修改时间");
                Preconditions.checkArgument(obj.containsKey("modify_sale_name"), "品牌" + id + "无修改人");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品牌】列表中展示项校验");
        }
    }


    @Test(dataProvider = "BRANDADD") //1005 带id
    public void brandAdd(String name, String desc, String a) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int bef = jc.BrandPage(1, 10, null, null).getInteger("total");
            int code = jc.BrandCreat(false, null, name, desc, info.getLogo()).getInteger("code");
            JSONObject obj = jc.BrandPage(1, 1, null, null).getJSONArray("list").getJSONObject(0);
            Long id = obj.getLong("id");
            //判断品牌状态
            Boolean status = obj.getBoolean("brand_status");
            //判断列表数量
            int after = jc.BrandPage(1, 10, null, null).getInteger("total");
            int add = after - bef;
            //判断新建商品时品牌下拉列表
            Thread.sleep(500);
            JSONArray brandlist = jc.BrandList().getJSONArray("list");
            Long listid = brandlist.getJSONObject(brandlist.size() - 1).getLong("id");
            jc.BrandDel(id, true);

            Preconditions.checkArgument(code == 1000, a + "状态码期待1000，实际" + code);
            Preconditions.checkArgument(add == 1, "新建后列表增加了" + add);
            Preconditions.checkArgument(status == true, "新增品牌状态期待为开启，实际为" + status);
            Preconditions.checkArgument(listid.longValue() == id.longValue(), "创建商品时的品牌下拉框未增加对应的品牌");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品牌】新建品牌");
        }
    }

    @DataProvider(name = "BRANDADD")
    public Object[] brandAdd1() {
        return new String[][]{
                {"a", "a", "品牌名称1个字简介1个字"},
                {info.getString(6), info.getString(6), "品牌名称6个字简介6个字"},
                {info.getString(20), info.getString(50), "品牌名称20个字简介50个字"},
        };
    }

    @Test(dataProvider = "BRANDADDERR")
    public void brandAddErr(String name, String desc, String a) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int code = jc.BrandCreat(false, null, name, desc, info.getLogo()).getInteger("code");
            Preconditions.checkArgument(code == 1001, a + "状态码期待1001，实际" + code);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品牌】新建品牌");
        }
    }

    @DataProvider(name = "BRANDADDERR")
    public Object[] brandAdd1Err() {
        return new String[][]{

                {"123456789012345678901", info.stringsix, "品牌名称21个字简介6个字"},
                {"A1！@啊", "12345678901234567890123456789012345678901234567890一", "品牌名称5个字简介51个字"},
        };
    }


    @Test
    public void brandAddErr1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String name = System.currentTimeMillis() + "重复";
            Long id = jc.BrandCreat(false, null, name, name, info.getLogo()).getJSONObject("data").getLong("id");

            int code = jc.BrandCreat(false, null, name, name, info.getLogo()).getInteger("code");
            Preconditions.checkArgument(code == 1001, "状态码期待1001，实际" + code);
            jc.BrandDel(id, true);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品牌】新建品牌,品牌名称与已存在的重复");
        }
    }

    @Test
    public void brandAddErr2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //不填写名称
            int code = jc.BrandCreat(false, null, null, "品牌描述", info.getLogo()).getInteger("code");
            Preconditions.checkArgument(code == 1001, "不填写名称期待1001，实际" + code);

//            //不填写描述 bug 7808
            int code1 = jc.BrandCreat(false, null, "null", null, info.getLogo()).getInteger("code");
            Preconditions.checkArgument(code1 == 1001, "不填写描述期待1001，实际" + code1);

            //不上传图片bug 7808
            int code2 = jc.BrandCreat(false, null, "pp" + Integer.toString((int) ((Math.random() * 9 + 1) * 100)), "品牌描述", null).getInteger("code");
            Preconditions.checkArgument(code2 == 1001, "不上传图片期待1001，实际" + code2);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品牌】新建品牌,不填写必填项");
        }
    }

    @Test
    public void brandStop1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {


            Long id = info.newGoodBrand().getLong("id");
            int code = jc.BrandChgStatus(id, false, false).getInteger("code");
            Preconditions.checkArgument(code == 1000, "状态码" + code);

            jc.BrandDel(id, true);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品牌】停用 无商品使用&状态=启用的品牌，期待成功");
        }
    }

    @Test
    public void brandStart1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {


            Long id = info.newGoodBrand().getLong("id");
            jc.BrandChgStatus(id, false, true);

            int code = jc.BrandChgStatus(id, true, false).getInteger("code");
            Preconditions.checkArgument(code == 1000, "状态码" + code);

            jc.BrandDel(id, true);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品牌】启用 无商品使用&状态=停用的品牌，期待成功");
        }
    }

    @Test(dataProvider = "BRANDSTATUS")
    public void brandDel1(String status, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Long id = info.newGoodBrand().getLong("id");
            jc.BrandChgStatus(id, Boolean.valueOf(status), true);

            int code = jc.BrandDel(id, false).getInteger("code");
            Preconditions.checkArgument(code == 1000, "删除状态=" + mess + "失败，状态码" + code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品牌】删除 无商品使用的品牌，期待成功");
        }
    }

    @DataProvider(name = "BRANDSTATUS")
    public Object[] brandstatus() {
        return new String[][]{

                {"true", "启用"},
                {"false", "停用"},
        };
    }

    //商品规格
    @Test
    public void specificationsStop1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long id = info.newSpecificition();
            int code = jc.specificationsChgStatus(id, false, false).getInteger("code");
            Preconditions.checkArgument(code == 1000, "停用无商品使用的规格,状态码" + code);

            int code1 = jc.specificationsChgStatus(id, true, false).getInteger("code");
            Preconditions.checkArgument(code1 == 1000, "启用停用的规格，期待成功实际" + code1);

            jc.specificationsChgStatus(id, false);
            jc.specificationsDel(id);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品规格】停用无商品使用的规格，期待成功");
        }
    }

    @Test
    public void specificationsDel1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long id = info.newSpecificition();


            //删除启用的规格，应失败（ 前端限制，这里注释掉）
//            jc.specificationsChgStatus(id,true,true);
//            int code1 = jc.specificationsDel(id,false).getInteger("code");
//            Preconditions.checkArgument(code1==1001,"删除启用的规格，期待失败实际"+code1);
            //删除停用的规格，应成功
            jc.specificationsChgStatus(id, false);
            int code2 = jc.specificationsDel(id, false).getInteger("code");
            Preconditions.checkArgument(code2 == 1000, "删除停用的规格，期待成功实际" + code2);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品规格】删除各种状态的&无商品使用的规格");
        }
    }


    //商品管理
    //2021-01-27
    @Test(dataProvider = "BRANDNAME")
    public void goodFilter1(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.goodsManagePage(1, 50, name, null, null, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String searchname = obj.getString("goods_name");
                Preconditions.checkArgument(searchname.contains(name), "搜索" + name + ", 结果中包含" + searchname);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品管理】根据筛选栏-商品名称搜索");
        }
    }


    @Test
    public void goodFilter2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //todo
            JSONArray list = jc.BrandList().getJSONArray("list");
            if (list.size() > 0) {
                Long id = list.getJSONObject(0).getLong("id");
                String brandname = list.getJSONObject(0).getString("brand_name");
                JSONArray goodlist = jc.goodsManagePage(1, 10, null, id, null, null, null, null).getJSONArray("list");
                for (int i = 0; i < goodlist.size(); i++) {
                    JSONObject obj = goodlist.getJSONObject(i);
                    String search = obj.getString("belongs_brand");
                    Preconditions.checkArgument(search.equals(brandname), "搜索" + brandname + ", 结果包含" + search);
                }
            }
            //要去下拉框的接口
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品管理】根据筛选栏-商品品牌搜索");
        }
    }

    @Test(dataProvider = "Status")
    public void goodFilter3(String status, String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray goodlist = jc.goodsManagePage(1, 10, null, null, status, null, null, null).getJSONArray("list");
            for (int i = 0; i < goodlist.size(); i++) {
                JSONObject obj = goodlist.getJSONObject(i);
                String search = obj.getString("goods_status_name");
                Preconditions.checkArgument(search.equals(name), "搜索" + name + ", 结果包含" + search);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品管理】根据筛选栏-商品状态搜索");
        }
    }

    @DataProvider(name = "Status")
    public Object[] searchStatus() {
        return new String[][]{
                {"UP", "上架"},
                {"DOWN", "下架"},
        };
    }

    //@Test
    public void goodFilter4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = jc.categoryTree().getJSONArray("list");
            if (list.size() > 0) {
                int id = list.getJSONObject(0).getInteger("category_id");

                //todo 没补充完
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品管理】根据一级品类搜索");
        }
    }

    //@Test
    public void goodShow() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.goodsManagePage(1, 50, null, null, null, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                Preconditions.checkArgument(obj.containsKey("goods_pic"), "未展示商品图片");
                Preconditions.checkArgument(obj.containsKey("goods_name"), "未展示商品名称");
                Preconditions.checkArgument(obj.containsKey("belongs_brand"), "未展示所属品牌");
                Preconditions.checkArgument(obj.containsKey("goods_category"), "未展示商品分类");
                Preconditions.checkArgument(obj.containsKey("price"), "未展示市场价");
                Preconditions.checkArgument(obj.containsKey("goods_status"), "未展示商品状态");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品管理】列表展示项校验");
        }
    }

    // @Test
    public void goodUp() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.goodsChgStatus(info.goods_id, "true", true); //启用商品
            //查看商品列表该商品状态
            JSONArray list = jc.goodsManagePage(1, 10, info.goods_name, null, null, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                if (obj.getLong("id") == info.goods_id) {
                    Preconditions.checkArgument(obj.getString("goods_status_name").equals("启用"), "启用后列表商品状态不是启用");
                }
            }
            //查看小程序是否有该商品

            JSONArray appletlist = jc.appletMallCommidityList(100, null, null, null, true).getJSONArray("list");
            Boolean isexist = false;
//            for (int j = 0 ; j < appletlist.size();j++){
//                JSONObject obj = list.getJSONObject(j);
//                if (obj.getInteger("id")==info.goods_id){
//                    isexist = true;
//                    break;
//                }
//            }
            Preconditions.checkArgument(isexist == true, "小程序未展示该商品");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品管理】上架存在的商品");
        }
    }

    //@Test
    public void goodUp2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int code = jc.goodsChgStatus(info.goods_id, "true", false).getInteger("code"); //启用商品

            Preconditions.checkArgument(code == 1001, "状态码期待1001，实际" + code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品管理】A删除商品，B不刷新页面启用商品");
        }
    }

    //@Test
    public void goodDown() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.goodsChgStatus(info.goods_id, "false", true); //启用商品
            //查看商品列表该商品状态
            JSONArray list = jc.goodsManagePage(1, 10, info.goods_name, null, null, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                if (obj.getLong("id") == info.goods_id) {
                    Preconditions.checkArgument(obj.getString("goods_status_name").equals("下架"), "下架后列表商品状态不是停用");
                }
            }
//            //查看小程序是否有该商品
//
//            JSONArray appletlist = jc.appletMallCommidityList(100,null,null,null,true).getJSONArray("list");
//            Boolean isexist = false;
//            for (int j = 0 ; j < appletlist.size();j++){
//                JSONObject obj = list.getJSONObject(j);
//                if (obj.getInteger("id")==info.goods_id){
//                    isexist = true;
//                    break;
//                }
//            }
//            Preconditions.checkArgument(isexist==true,"小程序展示了该商品");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品管理】下架商品");
        }
    }

    @Test
    public void goodEdit1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pcCreateGoods er = new pcCreateGoods();
            er.checkcode = false;
            er.id = 9999L;

            int code = jc.editGoodMethod(er).getInteger("code");
            Preconditions.checkArgument(code == 1001, "状态码期待1001，实际" + code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品管理】编辑中的商品被删除");
        }
    }


    @Test(dataProvider = "GOOD")
    public void goodAdd(String goodname, String goodDesc, String gooddetail, String price, String spename, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建一个一二三级品类
            String name = "品类";
            JSONObject obj = info.newFirstCategory(name + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)));
            String idone = obj.getString("id");
            JSONObject objtwo = info.newSecondCategory(name + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), idone);
            String idtwo = objtwo.getString("id");
            JSONObject objthree = info.newThirdCategory(name + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), idtwo);
            String idthree = objthree.getString("id");

            //新建规格

            String speItemName = "规格参数" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000));

            //新建品牌
            String brandid = info.newGoodBrand("pp" + System.currentTimeMillis(), "pp说明" + System.currentTimeMillis()).getString("id");

            Long speId = jc.specificationsCreate(spename, Long.parseLong(idone), null, null, true).getLong("id");
            //新建规格参数
            JSONObject objItem = new JSONObject();
            objItem.put("specifications_item", speItemName);
            JSONArray spearray = new JSONArray();
            spearray.add(objItem);
            jc.specificationsEdit(spename, Long.parseLong(idone), spearray, speId, true);
            Long speItemId = jc.specificationsDetail(speId, 1, 10).getJSONArray("specifications_list").getJSONObject(0).getLong("specifications_id");

            //新建商品


            pcCreateGoods good = new pcCreateGoods();
            good.first_category = Long.parseLong(idone);
            good.second_category = Long.parseLong(idtwo);
            good.third_category = Long.parseLong(idthree);
            good.goods_brand = Long.parseLong(brandid);

            //新建商品
            String select_specifications_str =
                    "[" +
                            "{" +
                            "\"specifications_id\":" + speId + "," +
                            "\"specifications_name\":\"" + spename + "\"," +
                            "\"specifications_list\":[" +
                            "{\"specifications_detail_id\":" + speItemId + "," +
                            "\"specifications_detail_name\":\"" + speItemName + "\"" +
                            "}]}]";
            JSONArray select_specifications = JSONArray.parseArray(select_specifications_str); //所选规格
            String goods_specifications_list_str = "[" +
                    "{" +
                    "\"first_specifications\":" + speItemId + "," +
                    "\"first_specifications_name\":\"" + speItemName + "\"," +

                    "\"head_pic\":\"" + info.getLogo() + "\"," +
                    "\"price\":69.98" +
                    "}]";
            JSONArray goods_specifications_list = JSONArray.parseArray(goods_specifications_list_str);
            good.select_specifications = select_specifications;
            good.goods_specifications_list = goods_specifications_list;
            good.checkcode = false;
            good.goods_name = goodname;
            good.goods_description = goodDesc;
            good.price = price;
            good.goods_detail = gooddetail;
            good.goods_pic_list = good.getPicone();
            JSONObject objnew = jc.createGoodMethod(good);
            int code = objnew.getInteger("code");
            Preconditions.checkArgument(code == 1000, mess + "状态码" + code);
            Long goodid = objnew.getJSONObject("data").getLong("id");


            //删除商品
            jc.deleteGoodMethod(goodid);
            //关闭->删除规格
            jc.specificationsChgStatus(speId, false);
            jc.specificationsDel(speId);
            //删除品类
            jc.categoryDel(Long.parseLong(idthree), true);
            jc.categoryDel(Long.parseLong(idtwo), true);
            jc.categoryDel(Long.parseLong(idone), true);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品管理】创建商品,一个规格一个参数");
        }
    }

    @DataProvider(name = "GOOD")
    public Object[] good() { //商品名称 商品描述  商品详情 市场价 规格名称 描述
        return new String[][]{
                {"a", "a", "123456789sdxfcghvjbknlm,@#$%^&*JHGFDs事事顺遂", "0.00", "a", "商品名称1个字 商品描述1个字 市场价0.00 规格名称1个字"},
                {"12345Q~!a啊不嘈杂啊67890", "12345Q~!a啊不嘈杂啊6789012345Q~!a啊不嘈杂啊67890", "123456789sdxf123456789sdxfcghvjbknlm,@#$%^&*JHGFDs事事顺遂123456789sdxfcghvjbknlm,@#$%^&*JHGFDs事事顺遂123456789sdxfcghvjbknlm,@#$%^&*JHGFDs事事顺遂123456789sdxfcghvjbknlm,@#$%^&*JHGFDs事事顺遂123456789sdxfcghvjbknlm,@#$%^&*JHGFDs事事顺遂123456789sdxfcghvjbknlm,@#$%^&*JHGFDs事事顺遂cghvjbknlm,@#$%^&*JHGFDs事事顺遂", "100000000.00", "a啊123～！@90", "商品名称20个字 商品描述40个字 市场价100000000.00 规格名称10个字"},
                {"12Q~!a啊不嘈", "12Q~!a啊不嘈", "1", "99.99", "a啊23～", "商品名称10个字 商品描述10个字 市场价99.99 规格名称5个字"},

        };
    }

    @Test
    public void goodAddMore() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建一个一二三级品类
            String name = "品类";
            JSONObject obj = info.newFirstCategory(name + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)));
            String idone = obj.getString("id");
            JSONObject objtwo = info.newSecondCategory(name + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), idone);
            String idtwo = objtwo.getString("id");
            JSONObject objthree = info.newThirdCategory(name + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), idtwo);
            String idthree = objthree.getString("id");


            //新建品牌
            String brandid = info.newGoodBrand("pp" + System.currentTimeMillis(), "pp说明" + System.currentTimeMillis()).getString("id");

            //新建规格
            String spename1 = "1规格" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000));
            String spename2 = "2规格" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000));

            Long speId = jc.specificationsCreate(spename1, Long.parseLong(idone), null, null, true).getLong("id"); //第一个规格 有10个参数
            Long speId2 = jc.specificationsCreate(spename2, Long.parseLong(idone), null, null, true).getLong("id"); //第二个规格 有1个参数
            //新建规格1的10个参数
            JSONArray spearray = new JSONArray();
            for (int i = 1; i < 11; i++) {
                JSONObject objItem = new JSONObject();
                objItem.put("specifications_item", "speItemName" + i);
                spearray.add(objItem);
            }

            jc.specificationsEdit(spename1, Long.parseLong(idone), spearray, speId, true);
            Long speItemId = jc.specificationsDetail(speId, 1, 10).getJSONArray("specifications_list").getJSONObject(0).getLong("specifications_id");

            //新建规格2的一个参数
            JSONObject objItem1 = new JSONObject();
            objItem1.put("specifications_item", "speItemName11");
            JSONArray spearray2 = new JSONArray();
            spearray2.add(objItem1);
            jc.specificationsEdit(spename2, Long.parseLong(idone), spearray2, speId2, true);
            Long speItemId2 = jc.specificationsDetail(speId2, 1, 10).getJSONArray("specifications_list").getJSONObject(0).getLong("specifications_id");

            //新建商品

            pcCreateGoods good = new pcCreateGoods();
            good.first_category = Long.parseLong(idone);
            good.second_category = Long.parseLong(idtwo);
            good.third_category = Long.parseLong(idthree);
            good.goods_brand = Long.parseLong(brandid);

            //新建商品

            JSONArray select_specifications = new JSONArray();

            JSONObject spe1obj = new JSONObject();
            spe1obj.put("specifications_id", speId);
            spe1obj.put("specifications_name", spename1);
            //规格1的信息
            JSONArray spe1list = new JSONArray();
            for (int i = 0; i < 10; i++) {
                JSONObject speclistobj = jc.specificationsDetail(speId, 1, 10).getJSONArray("specifications_list").getJSONObject(i);
                Long specifications_id = speclistobj.getLong("specifications_id");
                String specifications_item = speclistobj.getString("specifications_item");
                JSONObject everyspeclist = new JSONObject();
                everyspeclist.put("specifications_detail_id", specifications_id);
                everyspeclist.put("specifications_detail_name", specifications_item);
                spe1list.add(everyspeclist);
            }
            spe1obj.put("specifications_list", spe1list);
            //规格2的信息
            JSONObject spe2obj = new JSONObject();
            spe2obj.put("specifications_id", speId2);
            spe2obj.put("specifications_name", spename2);

            JSONArray spe2list = new JSONArray();
            JSONObject everyspeclist = new JSONObject();
            everyspeclist.put("specifications_detail_id", speItemId2);
            everyspeclist.put("specifications_detail_name", "speItemName11");
            spe2list.add(everyspeclist);
            spe2obj.put("specifications_list", spe2list);

            select_specifications.add(spe1obj);
            select_specifications.add(spe2obj);


            //参数组合
            JSONArray goods_specifications_list = new JSONArray();
            for (int j = 0; j < 10; j++) {
                JSONObject speclistobj = jc.specificationsDetail(speId, 1, 10).getJSONArray("specifications_list").getJSONObject(j);
                Long specifications_id = speclistobj.getLong("specifications_id");
                String specifications_item = speclistobj.getString("specifications_item");
                JSONObject everygoodspeclist = new JSONObject();
                everygoodspeclist.put("first_specifications", specifications_id);
                everygoodspeclist.put("first_specifications_name", specifications_item);
                everygoodspeclist.put("second_specifications", speItemId2);
                everygoodspeclist.put("second_specifications_name", "speItemName11");
                goods_specifications_list.add(everygoodspeclist);
            }


            good.select_specifications = select_specifications;
            good.goods_specifications_list = goods_specifications_list;
            good.checkcode = false;

            JSONObject objnew = jc.createGoodMethod(good);
            int code = objnew.getInteger("code");
            Preconditions.checkArgument(code == 1000, "状态码" + code);
            Long goodid = objnew.getJSONObject("data").getLong("id");


            //删除商品
            jc.deleteGoodMethod(goodid);
            //关闭->删除规格
            jc.specificationsChgStatus(speId, false);
            jc.specificationsDel(speId);
            jc.specificationsChgStatus(speId2, false);
            jc.specificationsDel(speId2);
            //删除品类
            jc.categoryDel(Long.parseLong(idthree), true);
            jc.categoryDel(Long.parseLong(idtwo), true);
            jc.categoryDel(Long.parseLong(idone), true);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品管理】创建商品,多个规格多参数");
        }
    }


    /**
     * 积分中心
     */

    @Test(dataProvider = "exchangeType")
    public void exchangeFilter1(String type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.exchangePage(1, 50, null, type, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                Preconditions.checkArgument(obj.getString("exchange_type").equals(type), "结果包含" + obj.getString("exchange_type_name"));
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分兑换】根据积分兑换类型筛选");
        }
    }

    @DataProvider(name = "exchangeType")
    public Object[] exchangeType() {
        return new String[]{
                "FICTITIOUS",// 虚拟
                "REAL", // 实物
        };
    }

    @Test(dataProvider = "exchangeStatus")
    public void exchangeFilter2(String status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.exchangePage(1, 50, null, null, status).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                Preconditions.checkArgument(obj.getString("status").equals(status), "结果包含" + obj.getString("status_name"));
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分兑换】根据状态筛选");
        }
    }

    @DataProvider(name = "exchangeStatus")
    public Object[] exchangeStatus() {
        return new String[]{
                "NOT_START",//未开始
                "WORKING", // 进行中
                "CLOSE", // 已关闭
                "EXPIRED", // 已过期

        };
    }

    @Test(dataProvider = "BRANDNAME")
    public void exchangeFilter3(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.exchangePage(1, 50, name, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                Preconditions.checkArgument(obj.getString("goods_name").contains(name), "结果包含" + obj.getString("goods_name"));
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分兑换】根据商品名称筛选");
        }
    }


    @Test
    public void exchangeDetailFilter1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = jc.exchangeDetail(1, 1, null, null, null, null, null).getJSONArray("list").getJSONObject(0).getString("exchange_customer_name");
            JSONArray list = jc.exchangeDetail(1, 50, null, name, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String searchname = obj.getString("exchange_customer_name");
                Preconditions.checkArgument(searchname.contains(name), "搜索结果包含" + searchname);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分明细】根据存在的兑换客户全称筛选");
        }
    }

    @Test(dataProvider = "exchange_type")
    public void exchangeDetailFilter2(String exchange_type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.exchangeDetail(1, 50, null, null, exchange_type, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String searchname = obj.getString("exchange_type");
                Preconditions.checkArgument(searchname.equals(exchange_type), "搜索结果包含" + searchname);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分明细】根据存在的兑换类型筛选");
        }
    }

    @DataProvider(name = "exchange_type")
    public Object[] exchange_type() {
        return new String[]{
                "ADD",
                "MINUS",

        };
    }

    @Test(dataProvider = "exchange_time")
    public void exchangeDetailFilter3(String start, String end) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.exchangeDetail(1, 50, null, null, null, start, end).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String searchtime = obj.getString("operate_time");
                Preconditions.checkArgument(searchtime.contains(start), "搜索" + start + "，结果包含" + searchtime);

            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分明细】根据兑换时间筛选");
        }
    }

    @DataProvider(name = "exchange_time")
    public Object[] exchange_time() {
        return new String[][]{
                {dt.getHistoryDate(0), dt.getHistoryDate(0)},
                {dt.getHistoryDate(-2), dt.getHistoryDate(-2)},

        };
    }

    @Test
    public void exchangeDetailShow() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.exchangeDetail(1, 50, null, null, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                Preconditions.checkArgument(obj.containsKey("exchange_customer_name"), obj.getString("phone") + "未展示客户名称");
                Preconditions.checkArgument(obj.containsKey("phone"), obj.getString("exchange_customer_name") + "未展示客户手机号");
                Preconditions.checkArgument(obj.containsKey("exchange_type"), obj.getString("exchange_customer_name") + "未展示兑换类型");
                Preconditions.checkArgument(obj.containsKey("stock_detail"), obj.getString("exchange_customer_name") + "未展示库存明细");
                Preconditions.checkArgument(obj.containsKey("operate_time"), obj.getString("exchange_customer_name") + "未展示操作时间");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分明细】列表展示项校验");
        }
    }

    @Test
    public void exchangeDetailOrder() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List time = new ArrayList();


            JSONArray list = jc.exchangeDetail(1, 50, null, null, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String date = obj.getString("operate_time") + ":000";
                time.add(dt.dateToTimestamp1(date));
            }
            List timecopy = time;
            Collections.sort(time);
            Preconditions.checkArgument(Iterators.elementsEqual(time.iterator(), timecopy.iterator()), "未倒序排列");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分明细】列表根据兑换时间倒序排列");
        }
    }

    //2021-02-02
    @Test
    public void exchangeOrder() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List time = new ArrayList();


            JSONArray list = jc.exchangeOrder(1, 50, null, null, null, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String date = obj.getString("exchange_time") + ":000";
                time.add(dt.dateToTimestamp1(date));
            }
            List timecopy = time;
            Collections.sort(time);
            Preconditions.checkArgument(Iterators.elementsEqual(time.iterator(), timecopy.iterator()), "未倒序排列");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分订单】列表根据兑换时间倒序排列");
        }
    }

    @Test(dataProvider = "BRANDNAME")
    public void exchangeOrderFilter1(String orderid) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List time = new ArrayList();


            JSONArray list = jc.exchangeOrder(1, 50, orderid, null, null, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String search = obj.getString("order_id");
                Preconditions.checkArgument(search.contains(orderid), "搜索" + orderid + ",结果包含" + search);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分订单】根据订单ID筛选");
        }
    }

    @Test(dataProvider = "BRANDNAME")
    public void exchangeOrderFilter2(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.exchangeOrder(1, 50, null, null, null, null, name, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String search = obj.getString("member_name");
                Preconditions.checkArgument(search.contains(name), "搜索" + name + ",结果包含" + search);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分订单】根据会员名称筛选");
        }
    }

    @Test(dataProvider = "BRANDNAME")
    public void exchangeOrderFilter3(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.exchangeOrder(1, 50, null, null, null, null, null, name).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String search = obj.getString("goods_name");
                Preconditions.checkArgument(search.contains(name), "搜索" + name + ",结果包含" + search);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分订单】根据商品名称筛选");
        }
    }

    @Test(dataProvider = "exchange_time")
    public void exchangeOrderFilter4(String start, String end) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.exchangeOrder(1, 50, null, start, end, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String searchtime = obj.getString("exchange_time");
                Preconditions.checkArgument(searchtime.contains(start), "搜索" + start + "，结果包含" + searchtime);

            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分订单】根据兑换时间筛选");
        }
    }

    @Test(dataProvider = "exchangeOrderStatus")
    public void exchangeOrderFilter5(String status, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.exchangeOrder(1, 50, null, null, null, status, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String searchtime = obj.getString("order_status_name");
                Preconditions.checkArgument(searchtime.equals(mess), "搜索" + mess + "，结果包含" + searchtime);

            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分订单】根据状态筛选");
        }
    }

    @DataProvider(name = "exchangeOrderStatus")
    public Object[] exchangeOrderStatus() {
        return new String[][]{
                {"WAITING", "待发货"},
                {"CANCELED", "已取消"},
                {"SEND", "待收货"},
                {"FINISHED", "已完成"},


        };
    }


    //@Test(dataProvider = "export")
    public void ExportAll(String url, String mess) {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            commonConfig.shopId = "46439";
            //导出
            int code = jc.recExport(url).getInteger("code");
            Preconditions.checkArgument(code == 1000, mess + "导出状态码为" + code);
            Thread.sleep(800);
            String status = jc.exportListFilterManage("-1", "1", "1", null, null).getJSONArray("list").getJSONObject(0).getString("status_name");

            Preconditions.checkArgument(status.equals("导出完成"), mess + " " + status);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            commonConfig.shopId = "-1";
            saveData("导出");
        }
    }

    @DataProvider(name = "export")
    public Object[] export() {
        return new String[][]{

                {"/jiaochen/pc/customer-manage/pre-sale-customer/page/export", "销售客户"},
                {"/jiaochen/pc/customer-manage/after-sale-customer/page/export", "售后客户"},
                {"/jiaochen/pc/customer-manage/wechat-customer/page/export", "小程序客户"},
                {"/jiaochen/pc/voucher-manage/voucher-form/export", "优惠券管理"},
                {"/jiaochen/pc/voucher-manage/verification-people/export", "核销人员"},
                {"/jiaochen/pc/package-manage/buy-package-record/export", "套餐购买记录"},
                {"/jiaochen/pc/operation/article/export", "文章列表"},
                {"/jiaochen/pc/activity/manage/export", "活动列表"},
                {"/jiaochen/pc/voucher/apply/export", "优惠券申请"},
                {"/jiaochen/pc/shop/export", "门店管理"},
                {"/jiaochen/pc/brand/export", "品牌管理"},
                {"/jiaochen/pc/role/export", "角色管理"},
                {"/jiaochen/pc/staff/export", "员工管理"},
                {"/jiaochen/pc/record/import-record/export", "导入记录"},
                {"/jiaochen/pc/record/export-record/export", "导出记录"},
                {"/jiaochen/pc/record/push-msg/export", "消息记录"},
                {"/jiaochen/pc/record/login-record/export", "登陆记录"},
                {"/jiaochen/pc/manage/rescue/export", "道路救援"},
                {"/jiaochen/pc/vip-marketing/wash-car-manager/export", "洗车管理"},
                {"/jiaochen/pc/vip-marketing/wash-car-manager/adjust-number/export", "调整次数"},
                {"/jiaochen/pc/vip-marketing/sign_in_config/change-record/export", "签到积分变更记录"},
                {"/jiaochen/pc/integral-center/exchange/export", "积分兑换"},
                {"/jiaochen/pc/integral-center/exchange-detail/export", "积分明细"},
                {"/jiaochen/pc/integral-center/exchange-order/export", "积分订单"},
                {"/jiaochen/pc/integral-mall/goods-manage/export", "商品管理"},
                {"/jiaochen/pc/manage/maintain/car-model/export", "保养配置"},
                {"/jiaochen/pc/customer-manage/pre-sale-customer/buy-car/page/export", "成交记录"},
                {"/jiaochen/pc/pre-sales-reception/export", "销售接待记录"},
                {"/jiaochen/pc/reception-manage/record/export", "售后接待管理"},
                {"/jiaochen/pc/customer-manage/loss-customer/page/export", "流失客户管理"},
                {"/jiaochen/pc/voucher/apply/export", "优惠券审批"},
                {"XXXXXXXX", "保养车系配置"},
                {"/jiaochen/pc/consult-management/online-experts-page-list-export", "在线专家列表"},
                {"/jiaochen/pc/consult-management/dedicated-service-page-list-export", "专属服务列表"},

        };
    }

    @Test
    public void Exportweixiu() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONObject obj = jc.afterSleCustomerManage("1", "10").getJSONArray("list").getJSONObject(0);
            String carid = obj.getString("car_id");
            String shopid = obj.getString("shop_id");

            //导出
            int code = jc.weixiuExport(carid, shopid).getInteger("code");
            Preconditions.checkArgument(code == 1000, "状态码为" + code);
            Thread.sleep(800);
            String status = jc.exportListFilterManage("-1", "1", "1", null, null).getJSONArray("list").getJSONObject(0).getString("status_name");

            Preconditions.checkArgument(status.equals("导出完成"), status);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出维修记录");
        }
    }

    @Test(dataProvider = "exportVourcher")
    public void ExportAll1(String url, String mess) {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            String id = jc.oucherFormVoucherPage(null, "1", "10").getJSONArray("list").getJSONObject(0).getString("voucher_id");
            //导出
            int code = jc.vourcherExport(url, id).getInteger("code");
            Preconditions.checkArgument(code == 1000, mess + "导出状态码为" + code);
            Thread.sleep(800);
            String status = jc.exportListFilterManage("-1", "1", "1", null, null).getJSONArray("list").getJSONObject(0).getString("status_name");

            Preconditions.checkArgument(status.equals("导出完成"), mess + " " + status);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出");
        }
    }

    @DataProvider(name = "exportVourcher")
    public Object[] exportVourcher() {
        return new String[][]{ // 单弄 活动报名记录、车系列表、车型列表

                {"/jiaochen/pc/voucher-manage/change-record/export", "优惠券变更记录"},
                {"/jiaochen/pc/voucher-manage/voucher-invalid-page/export", "作废记录"},
                {"/jiaochen/pc/voucher-manage/additional-record/export", "增发记录"},
                {"/jiaochen/pc/voucher-manage/send-record/export", "领取记录"},
                {"/jiaochen/pc/voucher-manage/verification-record/export", "核销记录"},

        };
    }

    @Test
    public void ExportAll2() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            String id = "";
            JSONArray array = jc.activityPage(1, 50).getJSONArray("list");
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                if (obj.getInteger("activity_type") == 2) {// 招募活动 类型是2
                    id = obj.getString("id");
                }
            }
            //导出
            int code = jc.activityExport(id).getInteger("code");
            Preconditions.checkArgument(code == 1000, "导出状态码为" + code);
            Thread.sleep(800);
            String status = jc.exportListFilterManage("-1", "1", "1", null, null).getJSONArray("list").getJSONObject(0).getString("status_name");

            Preconditions.checkArgument(status.equals("导出完成"), status);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出活动报名记录");
        }
    }

    @Test
    public void ExportAll3() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            //导出
            int code = jc.carStyleExport(info.BrandID).getInteger("code");
            Preconditions.checkArgument(code == 1000, "导出状态码为" + code);
            Thread.sleep(800);
            String status = jc.exportListFilterManage("-1", "1", "1", null, null).getJSONArray("list").getJSONObject(0).getString("status_name");

            Preconditions.checkArgument(status.equals("导出完成"), status);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出车系列表");
        }
    }

    @Test
    public void ExportAll4() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            //导出
            int code = jc.carModelExport(info.BrandID, info.CarStyleID).getInteger("code");
            Preconditions.checkArgument(code == 1000, "导出状态码为" + code);
            Thread.sleep(800);
            String status = jc.exportListFilterManage("-1", "1", "1", null, null).getJSONArray("list").getJSONObject(0).getString("status_name");

            Preconditions.checkArgument(status.equals("导出完成"), status);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出车型列表");
        }
    }


    @Test
    public void show() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            VisitorProxy visitor = new VisitorProxy(EnumTestProduce.JC_DAILY);

            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.INVALIDED).buildVoucher().getVoucherId();

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("卡券用法示例");
        }
    }

    //@Test
    public void pic() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
            String base64 = new ImageUtil().getImageBinary(filePath);
            String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("卡券用法示例");
        }
    }

    //@Test
    public void enuma() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            jc.pcEnuMap();

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("通用枚举");
        }
    }

    @Test(priority = 7)
    public void zzzzzdel() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //删除规格
            JSONArray list = jc.specificationsPage(1, 50, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                if (obj.getString("first_category").contains("品类") && obj.getString("first_category").length() == 7 && obj.getInteger("num") == 0) {
                    Long id = obj.getLong("id");
                    jc.specificationsDel(id, false);
                }

            }

            //删除品牌
            JSONArray list2 = jc.BrandPage(1, 50, "pp", null).getJSONArray("list");
            for (int j = 0; j < list2.size(); j++) {
                JSONObject obj = list2.getJSONObject(j);
                if (obj.getString("brand_name").length() == 15) {
                    Long id = obj.getLong("id");
                    jc.BrandDel(id, false);
                }
            }

            //删除品类
            JSONArray list3 = jc.categoryPage(1, 50, null, null, null, null).getJSONArray("list");
            for (int j = 0; j < list3.size(); j++) {
                JSONObject obj = list3.getJSONObject(j);
                if (obj.getString("category_name").length() == 7 && obj.getString("category_name").contains("品类")) {
                    Long id = obj.getLong("id");
                    jc.categoryChgStatus(id, false, false);
                    jc.categoryDel(id, false);
                }
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("删除规格品牌品类");
        }
    }

    @Test(dataProvider = "CSTMINFO")
    public void newPotentialCustomer(String name, String phone, String type, String sex, String mess, String chk) {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            int bef = jc.preSleCustomerManage(null, "1", "1", null, null).getInteger("total");
            Long shop_id = info.oneshopid;
            Long car_style_id = jc.styleList(shop_id).getJSONArray("list").getJSONObject(0).getLong("style_id");
            Long car_model_id = jc.modelList(car_style_id).getJSONArray("list").getJSONObject(0).getLong("model_id");
            String salesId = jc.saleList(shop_id, "PRE").getJSONArray("list").getJSONObject(0).getString("sales_id");
            int code = jc.createPotentialCstm(name, phone, type, sex, car_style_id, car_model_id, shop_id, salesId, false).getInteger("code");
            int after = jc.preSleCustomerManage(null, "1", "1", null, null).getInteger("total");
            if (chk.equals("false")) {
                Preconditions.checkArgument(code == 1001, mess + "期待失败，实际" + code);
            } else {
                int sum = after - bef;
                Preconditions.checkArgument(code == 1000, mess + "期待创建成功，实际" + code);
                Preconditions.checkArgument(sum == 1, mess + "期待创建成功列表+1，实际增加" + sum);
                int code2 = jc.createPotentialCstm(name, phone, type, sex, car_style_id, car_model_id, shop_id, salesId, false).getInteger("code");
                Preconditions.checkArgument(code2 == 1001, "使用列表中存在的手机号期待创建失败，实际" + code);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建潜客");
        }
    }

    @DataProvider(name = "CSTMINFO")
    public Object[] customerInfo() {
        return new String[][]{ // 姓名 手机号 类型 性别  提示语 正常/异常

                {"我", "1382172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), "PERSON", "0", "姓名一个字", "true"},
                {info.stringfifty, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), "CORPORATION", "1", "姓名50个字", "true"},
                {info.stringsix, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 100)), "CORPORATION", "1", "手机号10位", "false"},
                {info.stringsix, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), "CORPORATION", "1", "手机号12位", "false"},
                {info.stringfifty + "1", "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), "CORPORATION", "1", "姓名51位", "false"},

        };
    }

    @Test
    public void newPotentialCustomerErr() {

        logger.logCaseStart(caseResult.getCaseName());
        try {


            Long shop_id = info.oneshopid;
            Long car_style_id = jc.styleList(shop_id).getJSONArray("list").getJSONObject(0).getLong("style_id");
            Long car_model_id = jc.modelList(car_style_id).getJSONArray("list").getJSONObject(0).getLong("model_id");
            String salesId = jc.saleList(shop_id, "PRE").getJSONArray("list").getJSONObject(0).getString("sales_id");
            String name = "name" + System.currentTimeMillis();
            String phone = "1391172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000));
            String type = "PERSON";
            String sex = "0";
            //不填写姓名
            int code = jc.createPotentialCstm(null, phone, type, sex, car_style_id, car_model_id, shop_id, salesId, false).getInteger("code");
            Preconditions.checkArgument(code == 1001, "不填写姓名期待失败，实际" + code);

            //不填写手机号
            int code1 = jc.createPotentialCstm(name, null, type, sex, car_style_id, car_model_id, shop_id, salesId, false).getInteger("code");
            Preconditions.checkArgument(code1 == 1001, "不填写手机号期待失败，实际" + code);

            //不填写类型
            int code2 = jc.createPotentialCstm(name, phone, null, sex, car_style_id, car_model_id, shop_id, salesId, false).getInteger("code");
            Preconditions.checkArgument(code2 == 1001, "不填写车主类型期待失败，实际" + code);

            //不填写性别
            int code3 = jc.createPotentialCstm(name, phone, type, null, car_style_id, car_model_id, shop_id, salesId, false).getInteger("code");
            Preconditions.checkArgument(code3 == 1001, "不填写性别期待失败，实际" + code);

            //不填写意向车型 bug 7866
            int code4 = jc.createPotentialCstm(name, phone, type, sex, null, null, shop_id, salesId, false).getInteger("code");
            Preconditions.checkArgument(code4 == 1001, "不填写意向车型期待失败，实际" + code);

            //不填写所属门店
//            int code5 = jc.createPotentialCstm(name,phone,type,sex,car_style_id,car_model_id,null,salesId,false).getInteger("code");
//            Preconditions.checkArgument(code5==1001,"不填写所属门店期待失败，实际"+code);

            //不填写所属销售 bug 7866
//            int code6 = jc.createPotentialCstm(name,phone,type,sex,car_style_id,car_model_id,shop_id,null,false).getInteger("code");
//            Preconditions.checkArgument(code6==1001,"不填写所属销售期待失败，实际"+code6);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建潜客");
        }
    }

    @Test(dataProvider = "CSTMINFO")
    public void newCstmRecord(String name, String phone, String type, String sex, String mess, String chk) {

        logger.logCaseStart(caseResult.getCaseName());
        try {


            Long shop_id = info.oneshopid;
            Long car_style_id = jc.styleList(shop_id).getJSONArray("list").getJSONObject(0).getLong("style_id");
            Long car_model_id = jc.modelList(car_style_id).getJSONArray("list").getJSONObject(0).getLong("model_id");
            String salesId = jc.saleList(shop_id, "PRE").getJSONArray("list").getJSONObject(0).getString("sales_id");


            if (chk.equals("false")) {
                int code = jc.createCstm(name, phone, type, sex, car_style_id, car_model_id, shop_id, salesId, dt.getHistoryDate(0), "ASDFUGGDSF12" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), false).getInteger("code");
                Preconditions.checkArgument(code == 1001, mess + "期待失败，实际" + code);
            } else {
                int code1 = jc.createCstm(name, info.donephone, type, sex, car_style_id, car_model_id, shop_id, salesId, dt.getHistoryDate(0), "ASDFUGGDSF02" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), false).getInteger("code");
                Preconditions.checkArgument(code1 == 1000, mess + "期待创建成功，实际" + code1);

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建成交记录");
        }
    }

    @Test
    public void newCstmRecordErr() {

        logger.logCaseStart(caseResult.getCaseName());
        try {


            Long shop_id = info.oneshopid;
            Long car_style_id = jc.styleList(shop_id).getJSONArray("list").getJSONObject(0).getLong("style_id");
            Long car_model_id = jc.modelList(car_style_id).getJSONArray("list").getJSONObject(0).getLong("model_id");
            String salesId = jc.saleList(shop_id, "PRE").getJSONArray("list").getJSONObject(0).getString("sales_id");
            String name = "name" + System.currentTimeMillis();
            String phone = info.donephone;
            String type = "PERSON";
            String sex = "0";


//            int code = jc.createCstm(name,phone,type,sex,car_model_id,shop_id,salesId,dt.getHistoryDate(1),"ASDFUGGDSF12"+Integer.toString((int)((Math.random()*9+1)*10000)),false).getInteger("code");
//            Preconditions.checkArgument(code==1001,"购车日期大于当前时间期待失败，实际"+code);

            int code1 = jc.createCstm(name, phone, type, sex, car_style_id, car_model_id, shop_id, salesId, dt.getHistoryDate(-1), "ASDFUGGDSF1" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), false).getInteger("code");
            Preconditions.checkArgument(code1 == 1001, "底盘号16位期待失败，实际" + code1);

            int code2 = jc.createCstm(name, phone, type, sex, car_style_id, car_model_id, shop_id, salesId, dt.getHistoryDate(-1), "ASDFUGGDSF111" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), false).getInteger("code");
            Preconditions.checkArgument(code2 == 1001, "底盘号18位期待失败，实际" + code2);

            int code3 = jc.createCstm(name, info.phone, type, sex, car_style_id, car_model_id, shop_id, salesId, dt.getHistoryDate(-1), "ASDFUGGDSF11" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), false).getInteger("code");
            Preconditions.checkArgument(code3 == 1001, "手机号未注册小程序期待失败，实际" + code3);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建成交记录异常条件");
        }
    }


}
