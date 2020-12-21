package com.haisheng.framework.testng.bigScreen.jiaochenonline.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import org.testng.annotations.*;

import java.lang.reflect.Method;


/**
 * @author : lxq
 * @date :  2020/11/24
 */

public class SystemCaseOnline extends TestCaseCommon implements TestCaseStd {

    ScenarioUtilOnline jc = ScenarioUtilOnline.getInstance();
    jiaoChenInfoOnline info = new jiaoChenInfoOnline();

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     *
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = "吕雪晴";
        commonConfig.produce = EnumProduce.JC.name();
        commonConfig.referer = EnumRefer.JIAOCHEN_REFERER_DAILY.getReferer();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_ONLINE.getName() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.JIAOCHEN_ONLINE.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);

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

        jc.pcLogin("15711200001","000000");
    }


    /**
     *    PC 品牌管理-系统测试
     */


    //品牌--正常
    @Test
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

    @Test
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

    @Test
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
    @Test
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

    @Test(dataProvider = "CAR_STYLE")
    public void addCarStyle(String manufacturer, String name, String online_time) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int code = jc.addCarStyleNotChk(info.BrandIDOnline, manufacturer,  name,  online_time).getInteger("code");
            Preconditions.checkArgument(code==1000,"创建车系：生产商 "+ manufacturer + ", 车系 "+ name + ", 上线日期"+ online_time +"状态码"+code);

            //删除品牌车系
            Long id = jc.carStylePage(1,1,info.BrandIDOnline,"").getJSONArray("list").getJSONObject(0).getLong("id");
            jc.delCarStyle(id);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车系");
        }
    }

    @Test
    public void editCarStyle() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //创建车系
            String manufacturer = "旧生产商";
            String name= "旧车系";
            String online_time= dt.getHistoryDate(0);
            jc.addCarStyle(info.BrandIDOnline, manufacturer,  name,  online_time);
            //获取车系id
            Long id = jc.carStylePage(1,1,info.BrandIDOnline,name).getJSONArray("list").getJSONObject(0).getLong("id");
            //修改车系
            String manufacturer1 = "新生产商";
            String name1 = "新车系";
            String online_time1= dt.getHistoryDate(-2);
            jc.editCarStyle(id,info.BrandIDOnline,manufacturer1,name1,online_time1);
            //查看修改结果
            JSONObject obj = jc.carStylePage(1,30,info.BrandIDOnline,"").getJSONArray("list").getJSONObject(0);
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

    @Test
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
    @Test(dataProvider = "CAR_STYLEERR")
    public void addCarStyleErr(String manufacturer, String name, String online_time,String yz) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject obj = jc.addCarStyleNotChk(info.BrandIDOnline, manufacturer,  name,  online_time);
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

    @Test
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

    @Test
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
    @Test(dataProvider = "CAR_MODEL")
    public void addCarModel(String name, String year, String status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int code = jc.addCarModelNotChk(info.BrandIDOnline, info.CarStyleIDOnline,  name,year,  status).getInteger("code");
            Preconditions.checkArgument(code==1000,"创建车系：车型名称 "+ name + ", 年款 "+ year + ", 预约状态"+ status +"状态码"+code);

            //删除品牌车系
            Long id = jc.carModelPage(1,1,info.BrandIDOnline, info.CarStyleIDOnline,name,"","").getJSONArray("list").getJSONObject(0).getLong("id");
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
            jc.addCarModel(info.BrandIDOnline, info.CarStyleIDOnline,  name1,year1,  status1);
            //获取车系id
            int size = jc.carModelPage(1,1,info.BrandIDOnline, info.CarStyleIDOnline,name1,"","").getInteger("total");
            Long id = jc.carModelPage(1,size,info.BrandIDOnline, info.CarStyleIDOnline,name1,"","").getJSONArray("list").getJSONObject(size-1).getLong("id");
            System.out.println(id+"---------");
            //修改车型
            String name2 = "新车型名称"+System.currentTimeMillis();
            String year2= "2020年";
            String status2 = "DISABLE";
            jc.editCarModel(id,info.BrandIDOnline, info.CarStyleIDOnline,  name2,year2,  status2);
            //查看修改结果
            String search_name2 = "";
            String search_year2 = "";
            String search_status2 = "";
            int size1 = jc.carModelPage(1,1,info.BrandIDOnline,info.CarStyleIDOnline,"","","").getInteger("total");
            JSONArray arr = jc.carModelPage(1,size1,info.BrandIDOnline,info.CarStyleIDOnline,"","","").getJSONArray("list");
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
    @Test
    public void addCarModel_err() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String year = "1998年";
            String status = "ENABLE";
            JSONObject obj = jc.addCarModelNotChk(info.BrandIDOnline, info.CarStyleIDOnline,  info.stringfifty1,year,  status);
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
            JSONObject obj = jc.addCarModelNotChk(info.BrandIDOnline, info.CarStyleIDOnline,  info.stringsix,year,  status);
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
    @Ignore //创建太多门店 注视掉
    @Test(dataProvider = "SHOP")
    public void addshop(String simple_name, String name, String district_code, String adddress, String sale_tel, String service_tel,
                        String longitude, String latitude, String appointment_status,String washing_status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arr = new JSONArray();
            arr.add(info.BrandIDOnline);
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


    @Ignore //创建太多门店 注视掉
    @Test
    public void addshop_rephone() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String sale_tel = info.phone;
            String service_tel = info.phone;

            JSONArray arr = new JSONArray();
            arr.add(info.BrandIDOnline);

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
            arr.add(info.BrandIDOnline);
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

    @Test
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
    @Test(dataProvider = "ARTICLE")
    public void addArticle(String title, String pic_type,  String content, String label) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray pic_list1 =new JSONArray();
            pic_list1.add("general_temp/fd19d80a-bbff-45dc-8d02-36548ad2c43e");

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
    @Test(dataProvider = "ACTIVITY")
    public void addActivity(String title, String pic_type,  String content, String label,String start,String end,String quota,String address,String maintain,String voucher,String type, String day) {
        logger.logCaseStart(caseResult.getCaseName());
        try {


            JSONArray pic_list2 =new JSONArray();
            pic_list2.add("general_temp/fd19d80a-bbff-45dc-8d02-36548ad2c43e");
            pic_list2.add("general_temp/fd19d80a-bbff-45dc-8d02-36548ad2c43e");
            pic_list2.add("general_temp/fd19d80a-bbff-45dc-8d02-36548ad2c43e");
            JSONArray vou_list =new JSONArray();
            vou_list.add(jc.pcVoucherList().getJSONArray("list").getJSONObject(0).getInteger("id"));

            if (day.equals(0)){
                JSONObject obj = jc.addArticleNotChk(title,pic_type,pic_list2,content,label,"ACTIVITY",start,end,start,
                        end,Integer.valueOf(quota),address,Boolean.valueOf(maintain),Boolean.valueOf(voucher),vou_list,
                        type,start,end,null);
                int code = obj.getInteger("code");
                Long id = obj.getJSONObject("data").getLong("id");
                //关闭活动
                jc.changeArticleStatus(id);
                Preconditions.checkArgument(code==1000,"期待1000，实际"+ code);
            }
            else {
                JSONObject obj = jc.addArticleNotChk(title,pic_type,pic_list2,content,label,"ACTIVITY",start,end,start,
                        end,Integer.valueOf(quota),address,Boolean.valueOf(maintain),Boolean.valueOf(voucher),vou_list,
                        type,null,null,Integer.valueOf(day));
                int code = obj.getInteger("code");
                Long id = obj.getJSONObject("data").getLong("id");
                //关闭活动
                jc.changeArticleStatus(id);
                Preconditions.checkArgument(code==1000,"期待1000，实际"+ code);
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【内容运营】，新建活动3张图");
        }
    }
    @DataProvider(name = "ACTIVITY")
    public  Object[] activity() {
        return new String[][]{
                {"1234", "THREE",info.stringone, "RED_PAPER",dt.getHistoryDate(0),dt.getHistoryDate(100),"1","啊","false","true","SIGN_UP","0"},
                {info.stringten, "THREE",info.stringfifty, "PREFERENTIAL",dt.getHistoryDate(0),dt.getHistoryDate(110),"100",info.stringfifty,"true","true","SIGN_UP","1"},
                {info.string20, "THREE",info.stringten, "BARGAIN",dt.getHistoryDate(0),dt.getHistoryDate(365),"100000000",info.stringsix,"true","true","SIGN_UP","2000"},
                {info.stringten, "THREE",info.stringlong, "WELFARE",dt.getHistoryDate(0),dt.getHistoryDate(364),"999",info.stringten,"false","true","ARTICLE_BUTTON","1000"},
                {info.stringsix, "THREE",info.stringlong, "GIFT",dt.getHistoryDate(0),dt.getHistoryDate(62),"10000",info.stringfifty,"false","true","ARTICLE_BUTTON","50"}
        };
    }




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

}
