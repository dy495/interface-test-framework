package com.haisheng.framework.testng.bigScreen.jiaochen.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.jiaoChenInfo;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
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
    jiaoChenInfo info = new  jiaoChenInfo();

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     *
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "吕雪晴";

        commonConfig.produce = EnumProduce.JC.name();
        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getName() + commonConfig.checklistQaOwner);

        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = "-1";
        beforeClassInit(commonConfig);

    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     *
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);

        jc.pcLogin("15711300001","000000");
    }


    /**
     *    PC 品牌管理-系统测试
     */


    //品牌--正常
    @Test //ok
    public void addBrand_name1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int code = jc.addBrandNotChk(info.stringone,info.logo).getInteger("code");
            Preconditions.checkArgument(code==1000,"状态码期待1000，实际"+code);

            //删除品牌
            Long id = jc.brandPage(1,10,"","").getJSONArray("list").getJSONObject(0).getLong("id");
            jc.delBrand(id);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建品牌，名称1个字");
        }

    }

    @Test //ok
    public void addBrand_name10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int code = jc.addBrandNotChk(info.stringten,info.logo).getInteger("code");
            Preconditions.checkArgument(code==1000,"状态码期待1000，实际"+code);

            //删除品牌
            Long id = jc.brandPage(1,10,"","").getJSONArray("list").getJSONObject(0).getLong("id");
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
            jc.addBrand(name1,info.logo);
            //获取创建的品牌id
            Long id = jc.brandPage(1,10,"","").getJSONArray("list").getJSONObject(0).getLong("id");
            //修改这个品牌的名字
            jc.editBrand(id,name2,info.logo);
            //根据id查询，名字为name2
            JSONArray arr = jc.brandPage(1,100,"","").getJSONArray("list");
            for (int i = 0 ; i < arr.size(); i++){
                JSONObject obj = arr.getJSONObject(i);
                if (obj.getLong("id")==id){
                    Preconditions.checkArgument(obj.getString("name").equals(name2),"修改前名字是"+name1+"，期望修改为"+name2+"，实际修改后为"+obj.getString("name"));
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
            int code = jc.addBrandNotChk(name,info.logo).getInteger("code");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+code);

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

            int code = jc.addCarStyleNotChk(info.BrandID, manufacturer,  name,  online_time).getInteger("code");
            Preconditions.checkArgument(code==1000,"创建车系：生产商 "+ manufacturer + ", 车系 "+ name + ", 上线日期"+ online_time +"状态码"+code);

            //删除品牌车系
            Long id = jc.carStylePage(1,1,info.BrandID,"").getJSONArray("list").getJSONObject(0).getLong("id");
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
            String manufacturer = "旧生产商";
            String name= "旧车系";
            String online_time= dt.getHistoryDate(0);
            jc.addCarStyle(info.BrandID, manufacturer,  name,  online_time);
            //获取车系id
            Long id = jc.carStylePage(1,1,info.BrandID,name).getJSONArray("list").getJSONObject(0).getLong("id");
            //修改车系
            String manufacturer1 = "新生产商";
            String name1 = "新车系";
            String online_time1= dt.getHistoryDate(-2);
            jc.editCarStyle(id,info.BrandID,manufacturer1,name1,online_time1);
            //查看修改结果
            JSONObject obj = jc.carStylePage(1,30,info.BrandID,"").getJSONArray("list").getJSONObject(0);
            String search_manufacturer1 = obj.getString("manufacturer");
            String search_name1 = obj.getString("name");
            String search_online_time1= obj.getString("online_time");

            Preconditions.checkArgument(search_manufacturer1.equals(manufacturer1),"修改前生产商="+manufacturer+"，期望修改为"+manufacturer1+"，实际修改后为"+search_manufacturer1);
            Preconditions.checkArgument(search_name1.equals(name1),"修改前车系="+name+"，期望修改为"+name1+"，实际修改后为"+search_name1);
            Preconditions.checkArgument(search_online_time1.equals(online_time1),"修改前上线时间="+online_time+"，期望修改为"+online_time1+"，实际修改后为"+search_online_time1);


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
            String name= "车系重复";
            String online_time= dt.getHistoryDate(0);
            jc.addCarStyle(brandid1, manufacturer,  name,  online_time);

            int code = jc.addCarStyleNotChk(brandid2, manufacturer,  name,  online_time).getInteger("code");
            Preconditions.checkArgument(code==1000,"期待状态码1000，实际"+code);

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
    public  Object[] carStyle() {
        return new String[][]{
                {info.stringone, info.stringone,dt.getHistoryDate(0)},
                {info.stringfifty, info.stringfifty,dt.getHistoryDate(-1)},
                {info.stringsix, info.stringsix,dt.getHistoryDate(1)},
        };
    }

    //品牌车系--异常
    @Test(dataProvider = "CAR_STYLEERR") // OK bug 5371 已解决
    public void addCarStyleErr(String manufacturer, String name, String online_time,String yz) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject obj = jc.addCarStyleNotChk(info.BrandID, manufacturer,  name,  online_time);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,yz + "期待状态码1001，实际"+code+",提示语："+ message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车系，生产商/车系51字");
        }
    }

    @DataProvider(name = "CAR_STYLEERR")
    public  Object[] carStyle_err() {
        return new String[][]{
                {info.stringfifty1, info.stringone,dt.getHistoryDate(0),"生产商51个字"},
                {info.stringfifty, info.stringfifty1,dt.getHistoryDate(-1),"车系51个字"},

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
            String name= "车系重复";
            String online_time= dt.getHistoryDate(0);

            jc.addCarStyle(brandid1, manufacturer,  name,  online_time);

            JSONObject obj = jc.addCarStyleNotChk(brandid1, manufacturer,  name,  online_time);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"期待状态码1001，实际"+code+",提示语："+ message);


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
            String manufacturer = "自动化"+System.currentTimeMillis();
            String name= "自动化"+System.currentTimeMillis();
            String online_time= dt.getHistoryDate(0);
            JSONObject obj = jc.addCarStyleNotChk(brandid1, manufacturer,  name,  online_time);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"实际状态码"+ code + ", 提示语为："+message);


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

            int code = jc.addCarModelNotChk(info.BrandID, info.CarStyleID,  name,year,  status).getInteger("code");
            Preconditions.checkArgument(code==1000,"创建车系：车型名称 "+ name + ", 年款 "+ year + ", 预约状态"+ status +"状态码"+code);

            //删除品牌车系
            Long id = jc.carModelPage(1,1,info.BrandID, info.CarStyleID,name,"","").getJSONArray("list").getJSONObject(0).getLong("id");
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
    public  Object[] carModel() {
        return new String[][]{
                {info.stringone, "1","ENABLE"},
                {info.stringfifty, "2000年","ENABLE"},
                {info.stringsix, "XX年","DISABLE"},
        };
    }

    @Test
    public void editCarModel() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //创建车型
            String name1 = "旧车型名称"+System.currentTimeMillis();
            String year1= "2000年";
            String status1 = "ENABLE";
            jc.addCarModel(info.BrandID, info.CarStyleID,  name1,year1,  status1);
            //获取车系id
            int size = jc.carModelPage(1,1,info.BrandID, info.CarStyleID,name1,"","").getInteger("total");
            Long id = jc.carModelPage(1,size,info.BrandID, info.CarStyleID,name1,"","").getJSONArray("list").getJSONObject(size-1).getLong("id");
            System.out.println(id+"---------");
            //修改车型
            String name2 = "新车型名称"+System.currentTimeMillis();
            String year2= "2020年";
            String status2 = "DISABLE";
            jc.editCarModel(id,info.BrandID, info.CarStyleID,  name2,year2,  status2);
            //查看修改结果
            String search_name2 = "";
            String search_year2 = "";
            String search_status2 = "";
            int size1 = jc.carModelPage(1,1,info.BrandID,info.CarStyleID,"","","").getInteger("total");
            JSONArray arr = jc.carModelPage(1,size1,info.BrandID,info.CarStyleID,"","","").getJSONArray("list");
            for (int i = size1-1 ; i >0; i--){
                JSONObject obj = arr.getJSONObject(i);
                System.out.println(obj +"-----------------");
                if (obj.getLong("id").longValue()==id.longValue()){

                    search_name2 = obj.getString("name");
                    search_year2 = obj.getString("year");
                    search_status2= obj.getString("status");
                }
            }


            Preconditions.checkArgument(search_name2.equals(name2),"修改前车型名称="+name1+"，期望修改为"+name2+"，实际修改后为"+search_name2);
            Preconditions.checkArgument(search_year2.equals(year2),"修改前年款="+year1+"，期望修改为"+year2+"，实际修改后为"+search_year2);
            Preconditions.checkArgument(search_status2.equals(status2),"修改前状态="+status1+"，期望修改为"+status2+"，实际修改后为"+search_status2);

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
            JSONObject obj = jc.addCarModelNotChk(info.BrandID, info.CarStyleID,  info.stringfifty1,year,  status);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"期待状态码1001，实际"+code+"， 提示语："+message);

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

            String year = "123456";
            String status = "ENABLE";
            JSONObject obj = jc.addCarModelNotChk(info.BrandID, info.CarStyleID,  info.stringsix,year,  status);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"期待状态码1001，实际"+code+"， 提示语："+message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车型， 年款6个字");
        }
    }

    @Test //ok
    public void addCarModel_err1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //新建品牌
            Long brandid = info.getBrandID(7);
            //新建车系
            Long carStyleId = info.getCarStyleID(brandid,5);

            //删除车系
            jc.delCarStyle(carStyleId);

            //新建车型
            String name1 = "自动化"+System.currentTimeMillis();
            String year1= "2019年";
            String status1 = "ENABLE";
            JSONObject obj = jc.addCarModelNotChk(brandid, carStyleId,  name1,year1,  status1);

            //删除品牌
            jc.delBrand(brandid);

            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"状态码为"+code+ ", 提示语"+ message);

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
            Long carStyleId = info.getCarStyleID(brandid,5);

            //删除品牌
            jc.delBrand(brandid);
            //新建车型
            String name1 = "自动化"+System.currentTimeMillis();
            String year1= "1009年";
            String status1 = "ENABLE";
            JSONObject obj = jc.addCarModelNotChk(brandid, carStyleId,  name1,year1,  status1);

            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"状态码为"+code+ ", 提示语"+ message);

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


    //门店管理--正常
    @Test(dataProvider = "SHOP")
    public void addshop(String simple_name, String name, String district_code, String adddress, String sale_tel, String service_tel,
                        String longitude, String latitude, String appointment_status,String washing_status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arr = new JSONArray();
            arr.add(info.BrandID);
            int code = jc.addShopNotChk(info.logo,simple_name,name,arr,district_code,adddress,sale_tel,service_tel,Double.valueOf(longitude),
                    Double.valueOf(latitude),appointment_status,washing_status).getInteger("code");
            Preconditions.checkArgument(code==1000,"期待状态码1000，实际"+ code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【门店管理】，新建门店");
        }
    }
    @DataProvider(name = "SHOP")
    public  Object[] shop() {

        return new String[][]{
//                {info.stringone, info.stringone,info.district_code,info.stringone, info.phone,info.phone,"129.8439","42.96805","ENABLE","ENABLE"}, //一个字符太少了 注视掉 每次需要更改
//                {info.stringone, info.stringten,info.district_code,info.stringfifty, info.phone,info.phone,"129.8439","42.96805","ENABLE","DISABLE"},
//                {info.stringten, info.stringone,info.district_code,info.stringten, info.phone,info.phone,"129.8439","42.96805","DISABLE","ENABLE"},
                {info.stringten, info.stringfifty,info.district_code,info.stringone, info.phone,info.phone,"129.8439","42.96805","DISABLE","DISABLE"},
//                {info.stringone, info.stringfifty,info.district_code,info.stringten, info.phone,info.phone,"129.8439","42.96805","DISABLE","DISABLE"},

        };
    }


    @Test //ok
    public void addshop_rephone() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String sale_tel = info.phone;
            String service_tel = info.phone;

            JSONArray arr = new JSONArray();
            arr.add(info.BrandID);

            jc.addShop(info.logo,info.stringsix,info.stringsix,arr,info.district_code,info.stringsix,sale_tel,service_tel,
                    129.8439,42.96805, "DISABLE","DISABLE");
            int code = jc.addShopNotChk(info.logo,info.stringsix+"1",info.stringsix+"1",arr,info.district_code,info.stringsix,sale_tel,service_tel,
                    129.8439,42.96805, "DISABLE","DISABLE").getInteger("code");
            Preconditions.checkArgument(code==1000,"期待状态码1000，实际"+ code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【门店管理】，新建门店销售电话/售后电话与其他门店重复");
        }
    }

    //门店管理--异常
    @Test(dataProvider = "SHOPERR")
    public void addshopErr(String simple_name, String name, String district_code, String adddress, String sale_tel, String service_tel,
                        String longitude, String latitude, String appointment_status,String washing_status,String a) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arr = new JSONArray();
            arr.add(info.BrandID);
            int code = jc.addShopNotChk(info.logo,simple_name,name,arr,district_code,adddress,sale_tel,service_tel,Double.valueOf(longitude),
                    Double.valueOf(latitude),appointment_status,washing_status).getInteger("code");
            Preconditions.checkArgument(code==1001,a+"期待状态码1001，实际"+ code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【门店管理】，新建门店，参数不规范");
        }
    }

    @DataProvider(name = "SHOPERR")
    public  Object[] shop_err() {

        return new String[][]{
                {info.stringten+"1", info.stringone,info.district_code,info.stringone, info.phone,info.phone,"129.8439","42.96805","ENABLE","ENABLE","门店简称11字"},
                {info.stringone, info.stringfifty1,info.district_code,info.stringfifty, info.phone,info.phone,"129.8439","42.96805","ENABLE","DISABLE","门店全称1字"},
                {info.stringten, info.stringfifty,info.district_code,info.stringfifty1, info.phone,info.phone,"129.8439","42.96805","DISABLE","DISABLE","详细地址51字"},
//                {info.stringten, info.stringone,info.district_code,info.stringten, "11111111111",info.phone,"129.8439","42.96805","DISABLE","ENABLE","销售手机号11111111111"},
//                {info.stringone, info.stringfifty,info.district_code,info.stringten, "111111111111",info.phone,"129.8439","42.96805","DISABLE","DISABLE","销售手机号12位"},
//                {info.stringone, info.stringfifty,info.district_code,info.stringten, "1111111111",info.phone,"129.8439","42.96805","DISABLE","DISABLE","销售手机号10位"},
//                {info.stringten, info.stringone,info.district_code,info.stringten, info.phone,"11111111111","129.8439","42.96805","DISABLE","ENABLE","售后手机号11111111111"},
//                {info.stringone, info.stringfifty,info.district_code,info.stringten, info.phone,"111111111111","129.8439","42.96805","DISABLE","DISABLE","售后手机号12位"},
//                {info.stringone, info.stringfifty,info.district_code,info.stringten, info.phone,"1111111111","129.8439","42.96805","DISABLE","DISABLE","售后手机号10位"},
                {info.stringone, info.stringfifty,info.district_code,info.stringten, info.phone,info.phone,"1298439","42.96805","DISABLE","DISABLE","经度1298439"},
                {info.stringone, info.stringfifty,info.district_code,info.stringten, info.phone,info.phone,"129.8439","4296805","DISABLE","DISABLE","纬度4296805"},


        };
    }

    @Test //ok
    public void addshoperr1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String sale_tel = info.phone;
            String service_tel = info.phone;

            JSONArray arr = new JSONArray();
            arr.add(System.currentTimeMillis());

            int code = jc.addShopNotChk(info.logo,info.stringsix,info.stringsix,arr,info.district_code,info.stringsix,sale_tel,service_tel,
                    129.8439,42.96805, "DISABLE","DISABLE").getInteger("code");
            Preconditions.checkArgument(code==1001,"期待状态码1001，实际"+ code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【门店管理】，新建门店时品牌不存在");
        }
    }



    /**
     *    PC 内容运营-系统测试
     */


    //新建文章
    @Test(dataProvider = "ARTICLE") //ok
    public void addArticle(String title, String pic_type,  String content, String label) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray pic_list1 =new JSONArray();
            pic_list1.add("general_temp/9c6fbc65-0f1f-4341-9892-1f1052b6aa04");

            JSONArray pic_list2 =new JSONArray();
            pic_list2.add("");
            pic_list2.add("");
            pic_list2.add("");
            JSONObject obj = jc.addArticleNotChk(title,pic_type,pic_list1,content,label,"ARTICEL",null,null,null,
                    null,null,null,null,null,null,
                    null,null,null,null);
            int code = obj.getInteger("code");
            Long id = obj.getJSONObject("data").getLong("id");
            //关闭文章
            jc.changeArticleStatus(id);


            Preconditions.checkArgument(code==1000,"期待1000，实际"+ code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【内容运营】，新建文章1张图");
        }
    }
    @DataProvider(name = "ARTICLE") //要补充
    public  Object[] article() {


        return new String[][]{
                {"1234", "ONE_BIG",info.stringone, "RED_PAPER"},
                {info.stringten, "ONE_BIG",info.stringfifty, "PREFERENTIAL"},
                {info.string20, "ONE_LEFT",info.stringten, "BARGAIN"},
                {info.stringten, "ONE_LEFT",info.stringlong, "WELFARE"},
                {info.stringsix, "ONE_LEFT",info.stringten, "GIFT"},

        };
    }

    //新建活动





    @Test
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



    /**
     *    PC 页面级权限用例
     */

    @Test(dataProvider = "ROLE")
    public void addallRole(JSONArray idlist,List namelist,String phone, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Collections.sort(namelist);

            //创建角色和账号
            JSONObject obj = creatRoleAndAccount(idlist,namelist,phone);
            //角色id
            String roleId_str = obj.getString("roleid");
            //账号id
            String accountid =  obj.getString("accountid");

            //账号登陆展示的页面权限
            JSONArray resources = jc.loginPC(phone,"000000").getJSONArray("resources");
            List login_auth = new ArrayList();
            for (int i = 0 ; i < resources.size();i++){
                login_auth.add(resources.getJSONObject(i).getString("resource_code"));
            }
            Collections.sort(login_auth);

            Preconditions.checkArgument(Iterators.elementsEqual(namelist.iterator(),login_auth.iterator()),mess+"不一致");

            //删除账号
            creatRoleAndAccount(roleId_str,accountid);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建不同角色的账号，校验页面权限");
        }
    }


    @DataProvider(name = "ROLE")
    public  Object[] role() {
        return new Object[][]{
                {info.allauth_id,info.allauth_list,"13412010057","全部页面权限"},
                {info.daoruauth_id,info.daoruauth_list,"13412010058","导入记录权限所有页面；数据权限=全部；主体=品牌；无功能权限"},
                {info.jiedaiauth_id,info.jiedaiauth_list,"13412010069","接待管理所有页面；数据权限=个人；主体类型权限=门店；全部功能权限"},
                {info.kehu12auth_id,info.kehu12auth_list,"13412010061","客户管理+销售客户tab+售后客户tab；数据权限=全部；主体类型权限=集团；无功能权限"},
                {info.kehu123auth_id,info.kehu123auth_list,"13412010062","客户管理全部页面；数据权限=全部；主体类型权限=集团；无功能权限"},
                {info.yuyue12auth_id,info.yuyue12auth_list,"13412010063","预约管理+预约看板tab+预约记录tab；数据权限=全部；主体类型权限=门店；功能权限=售后接待+预约保养分配+预约应答人"},
                {info.xitong123auth_id,info.xitong123auth_list,"13412010064","门店管理+品牌管理+品牌删除；数据权限=全部；主体类型权限=集团；无功能权限"},
                {info.xitong45auth_id,info.xitong45auth_list,"13412010065","角色管理+员工管理；数据权限=全部；主体类型权限=区域；无功能权限"},
                {info.kaquan45auth_id,info.kaquan45auth_list,"13412010066","卡券管理+核销人员tab+核销记录tab；数据权限=全部；主体类型权限=门店；无功能权限"},
                {info.kaquan123auth_id,info.kaquan123auth_list,"13412010067","卡券管理+卡券表单tab+发卡记录tab+卡券申请页面；数据权限=全部；主体类型权限=品牌；无功能权限"},
                {info.taocannoauth_id,info.taocannoauth_list,"13412010067","套餐管理+套餐表单tab+套餐购买记录tab+无确认支付按钮；数据权限=全部；主体类型权限=门店；无功能权限"},
                {info.taocanauth_id,info.taocanauth_list,"13412010067","套餐管理+套餐表单tab+套餐购买记录tab+有确认支付按钮；数据权限=全部；主体类型权限=门店；无功能权限"},
                {info.nxauth_id,info.nxauth_list,"13412010067","内容运营+消息管理；数据权限=全部；主体类型权限=集团；无功能权限"},


        };
    }

    /**
     *功能权限 -售后接待/预约保养分配/预约应答人/提醒接收人
     */

    /**
     *    PC 有接待管理页面，无售后接待功能权限，无法在app/PC进行接待 角色1
     */
    @Ignore //前端置灰按钮，后端未做校验
    @Test
    public void recWithoutrole() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray idlist= info.jiedai234auth_id;
            List namelist=info.jiedai234auth_list;
            String phone = "13412010069";

            //创建角色和账号
            JSONObject obj = creatRoleAndAccount(idlist,namelist,phone);
            //角色id
            String roleId_str = obj.getString("roleid");
            //账号id
            String accountid =  obj.getString("accountid");

            //登陆PC，接待
            jc.loginPC(phone,"000000");
            int code = jc.pcManageReception("吉A123456",false).getInteger("code");
            //登陆app，接待
            jc.appLogin(phone,"000000");
            int code2= jc.appReceptionAdmitcode("吉A123456").getInteger("code");

            //删除账号
            creatRoleAndAccount(roleId_str,accountid);

            Preconditions.checkArgument(code==1001,"PC接待，状态码期待1001，实际"+code);
            Preconditions.checkArgument(code2==1001,"APP接待，状态码期待1001，实际"+code);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("有接待管理页面，无售后接待功能权限，在app/PC进行接待");
        }
    }

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
     *    个人： 能看自己的数据
     *    接待管理页-接待人 / 销售客户页-销售顾问 / 售后客户-客户经理 / 预约记录-客户经理 / 卡券表单-创建者 / 发卡记录-发券者 / 核销记录-核销账号 / 套餐管理-创建者 / 套餐购买记录-推荐人账号 / 卡券申请-申请人 /
     *    导入记录-操作员账号
     */




    public JSONObject creatRoleAndAccount(JSONArray idlist,List namelist,String phone) {
        JSONObject obj = new JSONObject();
        //新建角色
        String name = "zdh"+Integer.toString((int)(Math.random()*100000));
        jc.roleAdd(name,idlist);
        //查询角色id
        String roleId_str = jc.organizationRolePage(name,1,10).getJSONArray("list").getJSONObject(0).getString("id");
        JSONArray roleid = new JSONArray();
        roleid.add(roleId_str);

        //创建账号
        JSONArray shoparr = new JSONArray();
        shoparr.add(46194);
        jc.organizationAccountAdd("zdh",phone,roleid,shoparr);
        String accountid = jc.pcStaffPage("",1,1).getJSONArray("list").getJSONObject(0).getString("id");

        obj.put("roleid",roleId_str);
        obj.put("accountid",accountid);
        return obj;
    }

    public void creatRoleAndAccount(String roleid, String accountid) {
        jc.pcLogin("15711300001","000000");
        //删除账号
        jc.organizationAccountDelete(accountid);
        //删除角色
        jc.organizationidRoleDelete(roleid);
    }










}
