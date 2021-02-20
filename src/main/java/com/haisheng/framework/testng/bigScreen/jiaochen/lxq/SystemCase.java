package com.haisheng.framework.testng.bigScreen.jiaochen.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.jiaoChenInfo;
import com.haisheng.framework.testng.bigScreen.jiaochen.lxq.create.pcCreateExchangeGoods;
import com.haisheng.framework.testng.bigScreen.jiaochen.lxq.create.submitOrder;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.voucher.VoucherGenerator;
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
    jiaoChenInfo info = new  jiaoChenInfo();
    PublicParm pp = new PublicParm();
    String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";

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


        commonConfig.referer = EnumTestProduce.JIAOCHEN_DAILY.getReferer();
        commonConfig.product = EnumTestProduce.JIAOCHEN_DAILY.name();

        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getDesc() + commonConfig.checklistQaOwner);

        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = "-1";
        commonConfig.roleId="603";
        //commonConfig.shopId = "45973";
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

        jc.pcLogin("13114785236","000000");
    }

    /**
     *    --------------------- V1.0 --------------------------
     */

    /**
     *    PC 品牌管理-系统测试
     */


    //品牌--正常
    @Test //ok
    public void addBrand_name1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int code = jc.addBrandNotChk(info.stringone,info.getLogo()).getInteger("code");
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

            int code = jc.addBrandNotChk(info.stringten,info.getLogo()).getInteger("code");
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
            jc.addBrand(name1,info.getLogo());
            //获取创建的品牌id
            Long id = jc.brandPage(1,10,"","").getJSONArray("list").getJSONObject(0).getLong("id");
            //修改这个品牌的名字
            jc.editBrand(id,name2,info.getLogo());
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
            int code = jc.addBrandNotChk(name,info.getLogo()).getInteger("code");
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

//
//    //门店管理--正常
//    @Test(dataProvider = "SHOP")
//    public void addshop(String simple_name, String name, String district_code, String adddress, String sale_tel, String service_tel,
//                        String longitude, String latitude, String appointment_status,String washing_status) {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONArray arr = new JSONArray();
//            arr.add(info.BrandID);
//            int code = jc.addShopNotChk(info.getLogo(),simple_name,name,arr,district_code,adddress,sale_tel,service_tel,Double.valueOf(longitude),
//                    Double.valueOf(latitude),appointment_status,washing_status).getInteger("code");
//            Preconditions.checkArgument(code==1000,"期待状态码1000，实际"+ code);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【门店管理】，新建门店");
//        }
//    }
//    @DataProvider(name = "SHOP")
//    public  Object[] shop() {
//
//        return new String[][]{
////                {info.stringone, info.stringone,info.district_code,info.stringone, info.phone,info.phone,"129.8439","42.96805","ENABLE","ENABLE"}, //一个字符太少了 注视掉 每次需要更改
////                {info.stringone, info.stringten,info.district_code,info.stringfifty, info.phone,info.phone,"129.8439","42.96805","ENABLE","DISABLE"},
////                {info.stringten, info.stringone,info.district_code,info.stringten, info.phone,info.phone,"129.8439","42.96805","DISABLE","ENABLE"},
//                {info.stringten, info.stringfifty,info.district_code,info.stringone, info.phone,info.phone,"129.8439","42.96805","DISABLE","DISABLE"},
////                {info.stringone, info.stringfifty,info.district_code,info.stringten, info.phone,info.phone,"129.8439","42.96805","DISABLE","DISABLE"},
//
//        };
//    }
//
//
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
//    @Test //ok
//    public void addshoperr1() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            String sale_tel = info.phone;
//            String service_tel = info.phone;
//
//            JSONArray arr = new JSONArray();
//            arr.add(System.currentTimeMillis());
//
//            int code = jc.addShopNotChk(info.getLogo(),info.stringsix,info.stringsix,arr,info.district_code,info.stringsix,sale_tel,service_tel,
//                    129.8439,42.96805, "DISABLE","DISABLE").getInteger("code");
//            Preconditions.checkArgument(code==1001,"期待状态码1001，实际"+ code);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【门店管理】，新建门店时品牌不存在");
//        }
//    }
//


    /**
     *    PC 内容运营-系统测试
     */


    //新建文章
    @Test(dataProvider = "ARTICLE") //ok
    public void addArticle(String title, String pic_type,  String content, String label) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray pic_list1 =new JSONArray();
            //pic_list1.add("general_temp/9c6fbc65-0f1f-4341-9892-1f1052b6aa04");
            pic_list1.add(info.getLogo());

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
            //删除文章
            jc.delArticle(id);


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
                {info.stringsix, "ONE_LEFT",info.stringlong, "GIFT"},

        };
    }


    //@Test
    public void ArticleTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建文章
            JSONObject obj = info.newArtical();
            Long id = obj.getLong("id");
            //置顶
            jc.topArticle(id);
            //登陆小程序查看
            jc.appletLoginToken(pp.appletTocken);
            Long search_list = jc.appletArticleList(null,null).getJSONArray("list").getJSONObject(0).getLong("list");

            Preconditions.checkArgument(id==search_list,"置顶后不在小程序首位");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【内容运营】，置顶文章");
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
//                {info.allauth_id,info.allauth_list,"13402050010","全部页面权限"},
                {info.daoruauth_id,info.daoruauth_list,"13402050010","导入记录权限所有页面；数据权限=全部；主体=品牌；无功能权限"},
                {info.jiedaiauth_id,info.jiedaiauth_list,"13402050011","接待管理所有页面；数据权限=个人；主体类型权限=门店；全部功能权限"},
                {info.kehu12auth_id,info.kehu12auth_list,"13402050011","客户管理+销售客户tab+售后客户tab；数据权限=全部；主体类型权限=集团；无功能权限"},
                {info.kehu123auth_id,info.kehu123auth_list,"13402050012","客户管理全部页面；数据权限=全部；主体类型权限=集团；无功能权限"},
                {info.yuyue12auth_id,info.yuyue12auth_list,"13402050012","预约管理+预约看板tab+预约记录tab；数据权限=全部；主体类型权限=门店；功能权限=售后接待+预约保养分配+预约应答人"},
                {info.xitong123auth_id,info.xitong123auth_list,"13402050013","门店管理+品牌管理+品牌删除；数据权限=全部；主体类型权限=集团；无功能权限"},
                {info.xitong45auth_id,info.xitong45auth_list,"13402050013","角色管理+员工管理；数据权限=全部；主体类型权限=区域；无功能权限"},
//                {info.kaquan45auth_id,info.kaquan45auth_list,"13402050014","卡券管理+核销人员tab+核销记录tab；数据权限=全部；主体类型权限=门店；无功能权限"},
//                {info.kaquan123auth_id,info.kaquan123auth_list,"13402050014","卡券管理+卡券表单tab+发卡记录tab+卡券申请页面；数据权限=全部；主体类型权限=品牌；无功能权限"},
                {info.taocannoauth_id,info.taocannoauth_list,"13402050015","套餐管理+套餐表单tab+套餐购买记录tab+无确认支付按钮；数据权限=全部；主体类型权限=门店；无功能权限"},
                {info.taocanauth_id,info.taocanauth_list,"13402050015","套餐管理+套餐表单tab+套餐购买记录tab+有确认支付按钮；数据权限=全部；主体类型权限=门店；无功能权限"},
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


        //创建账号

        jc.addAccount("zh"+Integer.toString((int)(Math.random()*100000)),phone,Long.parseLong(roleId_str),name);
        String accountid = jc.pcStaffPage("",1,1).getJSONArray("list").getJSONObject(0).getString("id");

        obj.put("roleid",roleId_str);
        obj.put("accountid",accountid);
        return obj;
    }

    public void creatRoleAndAccount(String roleid, String accountid) {
        jc.pcLogin("13114785236","000000");
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

           JSONArray list = jc.categoryPage(1,100,null,info.first_category,null,null).getJSONArray("list");
            JSONArray second = new JSONArray();
           for (int i=0;i <list.size();i++){
               JSONObject obj = list.getJSONObject(i);

               if (obj.getString("category_level").equals("二级品类")){
                   second.add(obj.getString("category_name"));
                   String parent = obj.getString("parent_category");
                   Preconditions.checkArgument(parent.equals(info.first_category_chin),"筛选"+info.first_category_chin+"结果中的二级品类包含上级品类为"+parent);
               }
           }
            for (int j=0;j <list.size();j++){
                JSONObject obj = list.getJSONObject(j);
                if (obj.getString("category_level").equals("三级品类")){
                    String parent = obj.getString("parent_category");
                    Preconditions.checkArgument(second.contains(parent),"三级品类"+obj.getString("category_name")+"对应的二级品类"+parent+"不属于该一级品类");
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

            JSONArray list = jc.categoryPage(1,100,null,info.first_category,info.second_category,null).getJSONArray("list");
            for (int i=0;i <list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                String parent = obj.getString("parent_category");
                String level = obj.getString("category_level");
                Preconditions.checkArgument(parent.equals(info.second_category_chin),"筛选"+info.second_category_chin+"结果包含上级品类为"+parent);
                Preconditions.checkArgument(level.equals("三级品类"),"结果包含"+level);
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

            JSONArray list = jc.categoryPage(1,100,true,null,null,null).getJSONArray("list");
            for (int i=0;i <list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                Boolean status = obj.getBoolean("category_status");
                String name = obj.getString("category_name");
                Preconditions.checkArgument(status==true,"搜索状态为true，商品"+name+"的状态为false");
            }

            JSONArray list2 = jc.categoryPage(1,100,false,null,null,null).getJSONArray("list");
            for (int i=0;i <list2.size();i++){
                JSONObject obj = list2.getJSONObject(i);
                Boolean status = obj.getBoolean("category_status");
                String name = obj.getString("category_name");
                Preconditions.checkArgument(status==false,"搜索状态为false，商品"+name+"的状态为true");
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

            JSONArray list = jc.categoryPage(1,50,null,null,null,null).getJSONArray("list");
            for (int i=0;i <list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                int id = obj.getInteger("id");
                Preconditions.checkArgument(obj.containsKey("category_pic"),"品类"+id+"无品类图");
                Preconditions.checkArgument(obj.containsKey("category_name"),"品类"+id+"无品类名称");
                Preconditions.checkArgument(obj.containsKey("category_level"),"品类"+id+"无品类级别");
                Preconditions.checkArgument(obj.containsKey("num"),"品类"+id+"无商品数量");
                Preconditions.checkArgument(obj.containsKey("category_status"),"品类"+id+"无品类状态");
                Preconditions.checkArgument(obj.containsKey("last_modify_time"),"品类"+id+"无最新修改时间");
                Preconditions.checkArgument(obj.containsKey("modify_sale_name"),"品类"+id+"无修改人");
                if (!obj.getString("category_level").equals("一级品类")){
                    Preconditions.checkArgument(obj.containsKey("parent_category"),"品类"+id+"无上级品类");
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

    //@Test
    public void categoryOperate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.categoryPage(1,100,null,null,null,null).getJSONArray("list");
            for (int i=0;i <list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                int id = obj.getInteger("id");
                int num = obj.getInteger("num");
                if (num > 0 ){
                    Boolean is_edit = obj.getBoolean("is_edit");
                    Boolean is_delete = obj.getBoolean("is_delete");
                    Preconditions.checkArgument(is_edit==false,"品类"+id+"应不可编辑");
                    Preconditions.checkArgument(is_delete==false,"品类"+id+"应不可删除");
                }
                Boolean status = obj.getBoolean("category_status");

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】列表中不同状态可操作的按钮校验");
        }
    }

    //@Test
    public void categoryFalse1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.categoryChgStatus(info.first_category,false);
            JSONArray list = jc.categoryPage(1,50,null,info.first_category,null,null).getJSONArray("list");
            for (int i=0;i <list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                int id = obj.getInteger("id");
                Boolean status = obj.getBoolean("category_status");
                Preconditions.checkArgument(status==false,"停用后品类"+id+"的状态为"+status);
            }

            jc.categoryChgStatus(info.first_category,true);
            JSONArray list2 = jc.categoryPage(1,50,null,info.first_category,null,null).getJSONArray("list");
            for (int i=0;i <list2.size();i++){
                JSONObject obj = list2.getJSONObject(i);
                int id = obj.getInteger("id");
                Boolean status = obj.getBoolean("category_status");
                Preconditions.checkArgument(status==false,"启用后品类"+id+"的状态为"+status);
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】停用一级品类，期待所属的二级三级全部停用/品类下拉列表不展示该品类；再次启用，下属品类仍为停用状态");
        }
    }


    //@Test
    public void categoryFalse2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.categoryChgStatus(info.second_category,false);
            JSONArray list = jc.categoryPage(1,50,null,info.first_category,info.second_category,null).getJSONArray("list");
            for (int i=0;i <list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                int id = obj.getInteger("id");
                Boolean status = obj.getBoolean("category_status");
                Preconditions.checkArgument(status==false,"停用后品类"+id+"的状态为"+status);
            }

            jc.categoryChgStatus(info.second_category,true);

            JSONArray list2 = jc.categoryPage(1,50,null,info.first_category,info.second_category,null).getJSONArray("list");
            for (int i=0;i <list2.size();i++){
                JSONObject obj = list2.getJSONObject(i);
                int id = obj.getInteger("id");
                Boolean status = obj.getBoolean("category_status");
                Preconditions.checkArgument(status==false,"启用后品类"+id+"的状态为"+status);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】停用二级品类，期待所属的三级全部停用/品类下拉列表不展示该品类；再次启用二级品类，下属品类状态仍为停用");
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
            Preconditions.checkArgument(code==1000,"新建状态码期待1000，实际"+code);

            //禁用品类
            jc.categoryChgStatus(id,false);

            //编辑品类-更换图片
            String logo2 = jc.pcFileUpload(new ImageUtil().getImageBinary(filePath),true,null).getString("pic_path");
            int code2 = jc.categoryEdit(false,id,name,"FIRST_CATEGORY","",logo2).getInteger("code");
            Preconditions.checkArgument(code2==1000,"编辑重新上传图片状态码期待1000，实际"+code2);

            //编辑品类-不更换图片

            int code3 = jc.categoryEdit(false,id,name+"1","FIRST_CATEGORY","",null).getInteger("code");
            Preconditions.checkArgument(code3==1000,"编辑不传图片状态码期待1000，实际"+code3);

            //删除启用品类
            jc.categoryDel(id,1,1,true);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】新建/编辑一级品类,品类名称1/5/10个字");
        }
    }

    @DataProvider(name = "NAME")
    public  Object[] name() {
        return new String[]{
                "啊啊啊2",
//                "12345",
//                "1Aa啊！@#，嗷嗷",

        };
    }

    @Test
    public void categoryAddFirstErr1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject obj = info.newFirstCategory("12345678901");
            int code = obj.getInteger("code");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+code);
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
            //启用一级品类
            jc.categoryChgStatus(info.first_category,true);


            JSONObject obj1 = info.newSecondCategory(name);
            int code = obj1.getInteger("code");
            Long id =obj1.getLong("id");

            //停用一级品类
            jc.categoryChgStatus(info.first_category,false);
            JSONObject obj2 = info.newSecondCategory(name);
            int code2 = obj2.getInteger("code");
            Long id2 =obj2.getLong("id");

            Preconditions.checkArgument(code==1000,"一级品类状态=开启，状态码为"+code);
            Preconditions.checkArgument(code2==1000,"一级品类状态=关闭，状态码为"+code);
            //删除停用品类
            jc.categoryDel(id,1,1,true);
            jc.categoryDel(id2,1,1,true);


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

            String logo = jc.pcFileUpload(new ImageUtil().getImageBinary(filePath),true,null).getString("pic_path");
            int code = jc.categoryCreate(false,"name","SECOND_CATEGORY","99999",logo,null).getInteger("code");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+code);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】新建二级品类,所属一级品类不存在");
        }
    }

    //@Test(dataProvider =  "CATEGORYID")
    public void categoryAdd2OR3Err(String level, String fatherid) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = info.stringsix +"aa";

            Long id = System.currentTimeMillis();
            Long id2 = id+ 9000;
            jc.categoryCreate(false,name,level,fatherid,info.getLogo(),id);

            int code = jc.categoryCreate(false,name,level,fatherid,info.getLogo(),id2).getInteger("code");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+code);

            //删除品类
            jc.categoryDel(id,1,1,true);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】新建二级/三级品类,品类名称重复");
        }
    }
    @DataProvider(name = "CATEGORYID")
    public  Object[] categroyid() {
        return new String[][]{
                {"SECOND_CATEGORY",Long.toString(info.first_category)},
                {"THIRD_CATEGORY",Long.toString(info.second_category)},

        };
    }

    //@Test
    public void categoryAddSecondErr2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Long id =  System.currentTimeMillis();
            int code = jc.categoryCreate(false,info.first_category_chin,"二级品类",Long.toString(info.first_category),info.getLogo(),id).getInteger("code");
            Preconditions.checkArgument(code==1000,"状态码期待1000，实际"+code);

            //删除品类
            jc.categoryDel(id,1,1,true);


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

            String logo = jc.pcFileUpload(new ImageUtil().getImageBinary(filePath),true,null).getString("pic_path");
            //不填写品类名称
            int code = jc.categoryCreate(false,null,"FIRST_CATEGORY","",logo,null).getInteger("code");
            Preconditions.checkArgument(code==1001,"不填写品类名称,状态码期待1001，实际"+code);


            //不填写所属分类

            int code2 = jc.categoryCreate(false,"不填写所属品类","SECOND_CATEGORY","",logo,null).getInteger("code");
            Preconditions.checkArgument(code2==1001,"不填写所属品类,状态码期待1001，实际"+code);

            //不选择logo
            int code3 = jc.categoryCreate(false,"不选择logo","FIRST_CATEGORY","",null,null).getInteger("code");
            Preconditions.checkArgument(code3==1001,"不选择logo,状态码期待1001，实际"+code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品类】新建品类,不填写必填项");
        }
    }


    //商品品牌
    @Test(dataProvider = "BRANDNAME")
    public void brandFilter1(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = jc.BrandPage(1,50,name,null).getJSONArray("list");

            for (int i=0;i <list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                String searchname = obj.getString("brand_name");
                Preconditions.checkArgument(searchname.contains(name),"搜索结果包含"+searchname);
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
    public  Object[] brandName() {
        return new String[]{
                "1",
                "aA",
                "!@#$%^&*(-",
                "自动化",
                "之家",
                "测试",

        };
    }

    @Test
    public void brandFilter2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = jc.BrandPage(1,50,null,true).getJSONArray("list");

            for (int i=0;i <list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                Boolean searchstatus = obj.getBoolean("brand_status");
                Preconditions.checkArgument(searchstatus==true,"搜索结果中"+obj.getString("brand_name")+"的状态为"+searchstatus);
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

            JSONArray list = jc.BrandPage(1,50,null,false).getJSONArray("list");

            for (int i=0;i <list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                Boolean searchstatus = obj.getBoolean("brand_status");
                Preconditions.checkArgument(searchstatus==false,"搜索结果中"+obj.getString("brand_name")+"的状态为"+searchstatus);
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

            JSONArray list = jc.BrandPage(1,50,null,null).getJSONArray("list");
            for (int i=0;i <list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                int id = obj.getInteger("id");
                Preconditions.checkArgument(obj.containsKey("brand_pic"),"品牌"+id+"品牌logo");
                Preconditions.checkArgument(obj.containsKey("brand_name"),"品牌"+id+"无品牌名称");
                Preconditions.checkArgument(obj.containsKey("brand_description"),"品牌"+id+"无品牌简介");
                Preconditions.checkArgument(obj.containsKey("brand_status"),"品牌"+id+"无品牌状态");
                Preconditions.checkArgument(obj.containsKey("num"),"品牌"+id+"无商品数量");
                Preconditions.checkArgument(obj.containsKey("last_modify_time"),"品牌"+id+"无最新修改时间");
                Preconditions.checkArgument(obj.containsKey("modify_sale_name"),"品牌"+id+"无修改人");

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
    public void brandAdd(String name, String desc,String a) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long id = System.currentTimeMillis();
            int code = jc.BrandCreat(false,id,name,desc,info.getLogo()).getInteger("code");
            Preconditions.checkArgument(code==1000,a+"状态码期待1000，实际"+code);

            jc.BrandDel(id,1,1,true);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品牌】新建品牌");
        }
    }

    @DataProvider(name = "BRANDADD")
    public Object[] brandAdd1(){
        return new String[][] {
                {"1","1","品牌名称1个字简介1个字"},
//                {info.stringsix,info.stringsix,"品牌名称6个字简介6个字"},
//                {"zh这是20位！@#的说的是发发简称11",info.stringfifty,"品牌名称20个字简介50个字"},
        };
    }

    //@Test(dataProvider = "BRANDADDERR")
    public void brandAddErr(String name, String desc,String a) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long id = System.currentTimeMillis();
            int code = jc.BrandCreat(false,id,name,desc,info.getLogo()).getInteger("code");
            Preconditions.checkArgument(code==1001,a+"状态码期待1001，实际"+code);

            jc.BrandDel(id,1,1,true);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品牌】新建品牌");
        }
    }

    @DataProvider(name = "BRANDADDERR")
    public Object[] brandAdd1Err(){
        return new String[][] {

                {"123456789012345678901",info.stringsix,"品牌名称21个字简介6个字"},
                {"A1！@啊","12345678901234567890123456789012345678901234567890一","品牌名称5个字简介51个字"},
        };
    }


    //@Test
    public void brandAddErr1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long id = System.currentTimeMillis();
            String name = System.currentTimeMillis() + "重复";
            jc.BrandEdit(false,id,name,name,info.getLogo());
            int code = jc.BrandEdit(false,id,name,name,info.getLogo()).getInteger("code");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+code);

            jc.BrandDel(id,1,1,true);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品品牌】新建品牌,品牌名称与已存在的重复");
        }
    }



    //商品管理
    //2021-01-27
    @Test(dataProvider = "BRANDNAME")
    public void goodFilter1(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.goodsManagePage(1,50,name,null,null,null,null,null).getJSONArray("list");
            for (int i = 0 ; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                String searchname = obj.getString("goods_name");
                Preconditions.checkArgument(searchname.contains(name),"搜索"+name+", 结果中包含"+searchname);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品管理】根据筛选栏-商品名称搜索");
        }
    }


    //@Test
    public void goodFilter2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //todo
            //要去下拉框的接口
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品管理】根据筛选栏-商品品牌搜索");
        }
    }

    //@Test
    public void goodFilter3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //todo
            //要去下拉框的接口
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品管理】根据筛选栏-商品状态搜索");
        }
    }

    //@Test
    public void goodFilter4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = jc.categoryTree().getJSONArray("list");
            if (list.size()>0){
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
            JSONArray list = jc.goodsManagePage(1,50,null,null,null,null,null,null).getJSONArray("list");
            for (int i = 0 ; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                Preconditions.checkArgument(obj.containsKey("goods_pic"),"未展示商品图片");
                Preconditions.checkArgument(obj.containsKey("goods_name"),"未展示商品名称");
                Preconditions.checkArgument(obj.containsKey("belongs_brand"),"未展示所属品牌");
                Preconditions.checkArgument(obj.containsKey("goods_category"),"未展示商品分类");
                Preconditions.checkArgument(obj.containsKey("price"),"未展示市场价");
                Preconditions.checkArgument(obj.containsKey("goods_status"),"未展示商品状态");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品管理】列表展示项校验");
        }
    }

    //@Test
    public void goodUp() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.goodsChgStatus(info.goods_id,"true",true); //启用商品
            //查看商品列表该商品状态
            JSONArray list = jc.goodsManagePage(1,10,info.goods_name,null,null,null,null,null).getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                if (obj.getLong("id")==info.goods_id){
                    Preconditions.checkArgument(obj.getString("goods_status_name").equals("启用"),"启用后列表商品状态不是启用");
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
//            Preconditions.checkArgument(isexist==true,"小程序未展示该商品");

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
            int code = jc.goodsChgStatus(info.goods_id,"true",false).getInteger("code"); //启用商品

            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+code);

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
            jc.goodsChgStatus(info.goods_id,"false",true); //启用商品
            //查看商品列表该商品状态
            JSONArray list = jc.goodsManagePage(1,10,info.goods_name,null,null,null,null,null).getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                if (obj.getLong("id")==info.goods_id){
                    Preconditions.checkArgument(obj.getString("goods_status_name").equals("下架"),"下架后列表商品状态不是停用");
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
            pcCreateGoods er=new pcCreateGoods();
            er.checkcode=false;
            er.id=9999L;

            int code = jc.editGoodMethod(er).getInteger("code");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【商品管理】编辑中的商品被删除");
        }
    }

    //TODO 创建商品的各种长度


    /**
     * 积分中心
     */

    //积分兑换

    //todo 积分兑换筛选栏 ok
    //@Test(dataProvider = "exchangeType")
    public void exchangeFilter1(String type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.exchangePage(1,50,null,type,null).getJSONArray("list");
            for (int i = 0 ; i < list.size(); i++){
                JSONObject obj = list.getJSONObject(i);
                Preconditions.checkArgument(obj.getString("exchange_type").equals(type),"结果包含"+obj.getString("exchange_type_name"));
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
    public Object[] exchangeType(){
        return new String[]{
                "FICTITIOUS",// 虚拟
                "REAL", // 实物
        };
    }

    //@Test(dataProvider = "exchangeStatus")
    public void exchangeFilter2(String status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.exchangePage(1,50,null,null,status).getJSONArray("list");
            for (int i = 0 ; i < list.size(); i++){
                JSONObject obj = list.getJSONObject(i);
                Preconditions.checkArgument(obj.getString("status").equals(status),"结果包含"+obj.getString("status_name"));
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
    public Object[] exchangeStatus(){
        return new String[]{
                "NOT_START",//未开始
                "WORKING", // 进行中
                "CLOSE", // 已关闭
                "EXPIRED", // 已过期

        };
    }

    //@Test(dataProvider = "BRANDNAME")
    public void exchangeFilter3(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.exchangePage(1,50,name,null,null).getJSONArray("list");
            for (int i = 0 ; i < list.size(); i++){
                JSONObject obj = list.getJSONObject(i);
                Preconditions.checkArgument(obj.getString("goods_name").contains(name),"结果包含"+obj.getString("goods_name"));
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分兑换】根据商品名称筛选");
        }
    }

    //2021-01-28 todo 积分兑换列表操作

    //@Test(dataProvider = "exchangeStatus")
    public void exchangeTop(String status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int id = info.getStatusGoodId(status);
            if (id!=-1){
                int code = jc.exchangeGoodTop(id,false).getInteger("code");
                Preconditions.checkArgument(code==1000,"置顶"+status+"状态码"+code);
                //列表置顶
                int listID = jc.exchangePage(1,1,null,null,null).getJSONArray("list").getJSONObject(0).getInteger("id");
                Preconditions.checkArgument(listID==id,status+"置顶后未再列表首位");
                //状态为进行中的话 小程序要置顶
                if (status.equals("WORKING")){
                    Preconditions.checkArgument(info.showInApplet(id,true) == true,"商品"+id+"小程序未展示/未置顶");
                }
            }
            else {
                Preconditions.checkArgument(1==0,"无"+status+"商品，case跳过");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分兑换】置顶各状态的积分兑换商品");
        }
    }

    //@Test
    public void exchangeOpenCLOSE() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int id = info.getStatusGoodId("CLOSE");
            if (id!=-1){
                //开启
                int code = jc.exchangeGoodChgStatus(id,true,false).getInteger("code");
                Preconditions.checkArgument(code==1000,"状态码期待1000，实际"+code);
                //列表中状态=进行中，为了方便的获取状态 先置顶
                jc.exchangeGoodTop(id,true);
                String status = jc.exchangePage(1,1,null,null,null).getJSONArray("list").getJSONObject(0).getString("status");
                Preconditions.checkArgument(status.equals("WORKING"),"开启后状态为"+status);
                //小程序-人气推荐展示此商品
                Preconditions.checkArgument(info.showInApplet(id,false)==true,"小程序未展示此积分兑换品");

            }
            else {
                Preconditions.checkArgument(1==0,"无已关闭商品，case跳过");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分兑换】开启 状态为已关闭的积分兑换商品");
        }
    }

    //@Test(dataProvider = "exchangeStatus2")
    public void exchangeOpenNOTShow(String status1) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int id = info.getStatusGoodId(status1);
            if (id!=-1){
                //开启
                int code = jc.exchangeGoodChgStatus(id,true,false).getInteger("code");
                Preconditions.checkArgument(code==1000,"状态码期待1000，实际"+code);
                //列表中状态=原状态，为了方便的获取状态 先置顶
                jc.exchangeGoodTop(id,true);
                String status = jc.exchangePage(1,1,null,null,null).getJSONArray("list").getJSONObject(0).getString("status");
                Preconditions.checkArgument(status.equals(status1),"开启后状态为"+status);
                //小程序-人气推荐不展示此商品
                Preconditions.checkArgument(info.showInApplet(id,false)==false,"小程序展示了此积分兑换品");

            }
            else {
                Preconditions.checkArgument(1==0,"无"+status1+"商品，case跳过");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分兑换】开启 状态为"+status1+"的积分兑换商品");
        }
    }
    @DataProvider(name = "exchangeStatus2")
    public Object[] exchangeStatus2(){
        return new String[]{
                "NOT_START",//未开始
                "EXPIRED", // 已过期

        };
    }

    //@Test(dataProvider = "exchangeStatus2")
    public void exchangeCloseNOTShow(String status1) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int id = info.getStatusGoodId(status1);
            if (id!=-1){
                //关闭
                int code = jc.exchangeGoodChgStatus(id,false,false).getInteger("code");
                Preconditions.checkArgument(code==1000,"状态码期待1000，实际"+code);
                //列表中状态=原状态，为了方便的获取状态 先置顶
                jc.exchangeGoodTop(id,true);
                String status = jc.exchangePage(1,1,null,null,null).getJSONArray("list").getJSONObject(0).getString("status");
                Preconditions.checkArgument(status.equals(status1),"关闭后状态为"+status);
                //小程序-人气推荐不展示此商品
                Preconditions.checkArgument(info.showInApplet(id,false)==false,"小程序展示了此积分兑换品");

            }
            else {
                Preconditions.checkArgument(1==0,"无"+status1+"商品，case跳过");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分兑换】关闭 状态为"+status1+"的积分兑换商品");
        }
    }

    //@Test
    public void exchangeCloseWorking() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int id = info.getStatusGoodId("WORKING");
            if (id!=-1){
                //关闭
                int code = jc.exchangeGoodChgStatus(id,false,false).getInteger("code");
                Preconditions.checkArgument(code==1000,"状态码期待1000，实际"+code);
                //列表中状态=进行中，为了方便的获取状态 先置顶
                jc.exchangeGoodTop(id,true);
                String status = jc.exchangePage(1,1,null,null,null).getJSONArray("list").getJSONObject(0).getString("status");
                Preconditions.checkArgument(status.equals("CLOSE"),"关闭后状态为"+status);
                //小程序-人气推荐不展示此商品
                Preconditions.checkArgument(info.showInApplet(id,false)==false,"小程序展示了此积分兑换品");

            }
            else {
                Preconditions.checkArgument(1==0,"无进行中商品，case跳过");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分兑换】关闭 状态为进行中的积分兑换商品");
        }
    }



    @Test
    public void exchangeDetailFilter1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = jc.exchangeDetail(1,1,null,null,null,null,null).getJSONArray("list").getJSONObject(0).getString("exchange_customer_name");
            JSONArray list = jc.exchangeDetail(1,50,null,name,null,null,null).getJSONArray("list");
            for (int i = 0  ; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                String searchname = obj.getString("exchange_customer_name");
                Preconditions.checkArgument(searchname.contains(name),"搜索结果包含"+searchname);
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
            JSONArray list = jc.exchangeDetail(1,50,null,null,exchange_type,null,null).getJSONArray("list");
            for (int i = 0  ; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                String searchname = obj.getString("exchange_type");
                Preconditions.checkArgument(searchname.equals(exchange_type),"搜索结果包含"+searchname);
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
    public Object[] exchange_type(){
        return new String[]{
                "ADD",
                "MINUS",

        };
    }

    @Test(dataProvider = "exchange_time")
    public void exchangeDetailFilter3(String start, String end) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.exchangeDetail(1,50,null,null,null,start,end).getJSONArray("list");
            for (int i = 0  ; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                String searchname = obj.getString("operate_time");
                //todo 操作时间在选择时间段内

            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分明细】根据存在的兑换类型筛选");
        }
    }

    @DataProvider(name = "exchange_time")
    public Object[] exchange_time(){
        return new String[][]{
                {dt.getHistoryDate(0),dt.getHistoryDate(0)},
                {dt.getHistoryDate(-2),dt.getHistoryDate(-1)},

        };
    }

    @Test
    public void exchangeDetailShow() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.exchangeDetail(1,50,null,null,null,null,null).getJSONArray("list");
            for (int i = 0 ; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                Preconditions.checkArgument(obj.containsKey("exchange_customer_name"),"未展示客户名称");
                Preconditions.checkArgument(obj.containsKey("phone"),"未展示客户手机号");
                Preconditions.checkArgument(obj.containsKey("exchange_type"),"未展示兑换类型");
                Preconditions.checkArgument(obj.containsKey("stock_detail"),"未展示库存明细");
                Preconditions.checkArgument(obj.containsKey("order_code"),"未展示订单号");
                Preconditions.checkArgument(obj.containsKey("operate_time"),"未展示操作时间");
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


            JSONArray list = jc.exchangeDetail(1,50,null,null,null,null,null).getJSONArray("list");
            for (int i = 0 ; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                String date = obj.getString("operate_time")+":000";
                time.add(dt.dateToTimestamp(date));
            }
            List timecopy = time;
            Collections.sort(time);
            Preconditions.checkArgument(Iterators.elementsEqual(time.iterator(),timecopy.iterator()),"未倒序排列");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分明细】列表根据兑换时间倒序排列");
        }
    }



    //积分订单

    //todo 积分订单筛选栏 接口状态要改
    //todo 积分订单确认发货
    //2021-02-02
    @Test
    public void exchangeOrder() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List time = new ArrayList();


            JSONArray list = jc.exchangeOrder(1,50,null,null,null,null,null,null).getJSONArray("list");
            for (int i = 0 ; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                String date = obj.getString("exchange_time")+":000";
                time.add(dt.dateToTimestamp(date));
            }
            List timecopy = time;
            Collections.sort(time);
            Preconditions.checkArgument(Iterators.elementsEqual(time.iterator(),timecopy.iterator()),"未倒序排列");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【积分订单】列表根据兑换时间倒序排列");
        }
    }



    @Test
    public void newFictitiousAndBuy() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            /**
             * 步骤一 新建虚拟积分商品 兑换次数限制=1
             */
            //新建虚拟商品前记录数据
            int PCtotalPre = jc.exchangePage(1,1,null,null,null).getInteger("total");
            jc.appletLoginToken(pp.appletTocken);
            int applettotalPre = jc.appletMallCommidityList(1,null,null,null,null).getInteger("total");

            //新建虚拟积分商品 兑换次数限制=1
            jc.pcLogin("15711300001","000000");
            Long fictitiousId = info.newFictitious();

            //PC积分兑换列表+1
            //小程序积分商城 积分兑换商品+1
            int PCtotalAft = jc.exchangePage(1,1,null,null,null).getInteger("total");
            jc.appletLoginToken(pp.appletTocken);
            int applettotalAft = jc.appletMallCommidityList(1,null,null,null,null).getInteger("total");

            Preconditions.checkArgument(PCtotalAft-PCtotalPre == 1, "新建虚拟积分商品后，PC积分兑换未增加1" );
            Preconditions.checkArgument(applettotalAft-applettotalPre == 1, "新建虚拟积分商品后，小程序积分商城的积分兑换商品未增加1" );


            /**
             * 步骤二 小程序兑换
             */

            info.appletBuyFictitious(fictitiousId);

            /**
             * 步骤三 小程序再次兑换
             */
            //小程序再次兑换 应失败
            int code = jc.appletSubmitExchange(fictitiousId,false).getInteger("code");
            Preconditions.checkArgument(code==1001,"期待兑换失败1001，实际"+code);


            jc.pcLogin("15711300001","000000");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC新建虚拟商品->小程序兑换");
        }
    }

    @Test
    public void newRealAndBuy() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            /**
             * 步骤一 新建品牌、一二三级品类、规格、商品
             */

            //新建品牌
            Long brandID = System.currentTimeMillis();
            jc.BrandCreat(true,brandID,"name"+brandID,"desc"+brandID,info.getLogo());

            //新建一级品类
            Long firstid = System.currentTimeMillis();
            String firstname = "F"+Integer.toString((int)((Math.random()*9+1)*1000));
            jc.categoryCreate(true,firstname,"一级品类",null,info.getLogo(),firstid);
            jc.categoryChgStatus(firstid,true);

            //新建二级品类
            Long secondid = System.currentTimeMillis();
            String secondname = "S"+Integer.toString((int)((Math.random()*9+1)*1000));
            jc.categoryCreate(true,secondname,"二级品类",Long.toString(firstid),info.getLogo(),secondid);
            jc.categoryChgStatus(secondid,true);

            //新建三级品类
            Long thirdid = System.currentTimeMillis();
            String thirdname = "T"+Integer.toString((int)((Math.random()*9+1)*1000));
            jc.categoryCreate(true,thirdname,"三级品类",Long.toString(secondid),info.getLogo(),thirdid);
            jc.categoryChgStatus(thirdid,true);

            //新建规格
            Long speID = System.currentTimeMillis();
            String speName = "规格" + Integer.toString((int)((Math.random()*9+1)*1000));
            String specifications_list_str = "[" +
                    "{" +
                    "\"specifications_item\":\""+dt.getHistoryDate(0) +"\"," +
                    "\"num\":100" +
                    "}" +
                    "]";
            JSONArray specifications_list = JSONArray.parseArray(specifications_list_str);
            Long specifications_detail_id = 1L;// ??
            jc.specificationsCreate(speName,firstid,specifications_list,speID,true);

            //新建商品
            String select_specifications_str =
                    "[" +
                            "{" +
                            "\"specifications_id\":"+ speID+","+
                            "\"specifications_name\":\""+speName +"\","+
                            "\"specifications_list\":[" +
                            "\"specifications_detail_id\":"+specifications_detail_id +"\","+
                            "\"specifications_detail_name\":\""+dt.getHistoryDate(0)+"\""+
                            "}]}]";
            JSONArray select_specifications = JSONArray.parseArray(select_specifications_str); //所选规格
            String goods_specifications_list_str = "[" +
                    "{" +
                    "\"first_specifications\":"+specifications_detail_id+"," +
                    //"\"second_specifications\":"+null+",\n" +
                    "\"head_pic\":\""+info.getLogo()+"\"," +
                    "\"price\":69.98" +
                    "}]";
            JSONArray goods_specifications_list = JSONArray.parseArray(goods_specifications_list_str);
            pcCreateGoods goods = new pcCreateGoods();
            goods.id = System.currentTimeMillis();
            goods.price = "99.99";
            goods.select_specifications = select_specifications;
            goods.goods_specifications_list = goods_specifications_list;
            jc.createGoodMethod(goods);


            /**
             * 步骤二 新建实体积分商品 兑换次数不限
             */

            JSONArray specification_list = new JSONArray(); // 内容要补充
            pcCreateExchangeGoods ex = new pcCreateExchangeGoods();
            ex.chkcode=true;
            ex.id  = System.currentTimeMillis();
            ex.exchange_goods_type = "REAL";
            ex.goods_id = goods.id;
            ex.is_limit=false;
            ex.specification_list = specification_list;
            jc.exchangeGoodCreat(ex);


            /**
             * 步骤三 小程序【积分商城】兑换
             */
            submitOrder or = new submitOrder();
            or.commodity_id = ex.id;
            or.specification_id = specifications_detail_id;
            or.buyer_message = "自动化"+System.currentTimeMillis();
            or.commodity_num = 1;
            or.district_code = info.district_code;
            or.address= "zdh北京市海淀区";
            or.receiver = "zdh";
            or.receive_phone= "13400000001";
            jc.appletSubmitOrder(or);

            //查询订单id
            Long order_id = jc.exchangeOrder(1,1,null,null,null,null,null,null).getJSONArray("list").getJSONObject(0).getLong("id");

            /**
             * 步骤四 PC发货
             */
            jc.confirmShipment(order_id,String.valueOf(System.currentTimeMillis()),true);

            /**
             * 步骤五 小程序【积分兑换订单】再次兑换
             */
            jc.appletSubmitOrder(or);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC新建实体商品积分兑换-> 小程序兑换-> 发货");
        }
    }





















    @Test
    public void show() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Visitor visitor =new Visitor(EnumTestProduce.JIAOCHEN_DAILY);

            Long voucherId=new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.INVALIDED).buildVoucher().getVoucherId();

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
           String logo = jc.pcFileUpload(new ImageUtil().getImageBinary(filePath),true,null).getString("pic_path");

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
}
