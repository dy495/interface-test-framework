package com.haisheng.framework.testng.bigScreen.jiaochen.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumRefer;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.jiaoChenInfo;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.PublicParm;
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


/**
 * @author : lxq
 * @date :  2020/12/18
 */

public class DataCase extends TestCaseCommon implements TestCaseStd {

    ScenarioUtil jc = ScenarioUtil.getInstance();
    jiaoChenInfo info = new jiaoChenInfo();
    PublicParm pp = new PublicParm();

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
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
        commonConfig.referer = EnumRefer.JIAOCHEN_REFERER_DAILY.getReferer();
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
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        jc.pcLogin("15711300001","000000");
    }




    @Test
    public void addshop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String simple_name="简称"+Integer.toString((int)(Math.random()*100000));
            String name="全称"+System.currentTimeMillis();
            String district_code="222402";
            String address="修改前的地址";
            String sale_tel="13400000001";
            String service_tel="13499999990";
            String longitude="129.8439";
            String latitude="42.96805";
            String appointment_status="DISABLE";
            String washing_status="DISABLE";

            JSONArray arr = new JSONArray();
            arr.add(info.BrandID);

            //获取门店列表数
            int bef = jc.shopPage(1,1,"").getInteger("total");
            jc.addShop(info.logo,simple_name,name,arr,district_code,address,sale_tel,service_tel,Double.valueOf(longitude),
                    Double.valueOf(latitude),appointment_status,washing_status);
            int after = jc.shopPage(1,1,"").getInteger("total");
            JSONObject obj = jc.shopPage(1,1,name).getJSONArray("list").getJSONObject(0);
            String simple_name1=obj.getString("simple_name");
            String name1=obj.getString("name");
            String district_code1=obj.getString("district_code");
            String address1=obj.getString("address");
            String sale_tel1=obj.getString("sale_tel");
            String service_tel1=obj.getString("service_tel");
            String longitude1=obj.getString("longitude");
            String latitude1=obj.getString("latitude");

            int num = after - bef;
            Preconditions.checkArgument(num==1,"创建门店，门店列表增加了"+num);
            Preconditions.checkArgument(simple_name.equals(simple_name1),"门店简称，创建时="+simple_name+",列表中="+simple_name1);
            Preconditions.checkArgument(name.equals(name1),"门店全称，创建时="+name+",列表中="+name1);
            Preconditions.checkArgument(district_code.equals(district_code1),"门店所属城市，创建时="+district_code+",列表中="+district_code1);
            Preconditions.checkArgument(address.equals(address1),"门店详细地址，创建时="+address+",列表中="+address1);
            Preconditions.checkArgument(sale_tel.equals(sale_tel1),"门店销售电话，创建时="+sale_tel+",列表中="+sale_tel1);
            Preconditions.checkArgument(service_tel.equals(service_tel1),"门店售后电话，创建时="+service_tel+",列表中="+service_tel1);
            Preconditions.checkArgument(longitude.equals(longitude1),"门店纬度，创建时="+longitude+",列表中="+longitude1);
            Preconditions.checkArgument(latitude.equals(latitude1),"门店经度，创建时="+latitude+",列表中="+latitude1);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【门店管理】，新建门店, 列表数量+1，与新建时信息一致");
        }
    }

    @Test
    public void editshop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String simple_name="简称"+Integer.toString((int)(Math.random()*100000));
            String name="自动化用的门店-不要改";
            String district_code="222402";
            String address="地址"+System.currentTimeMillis();
            String sale_tel="1380000"+Integer.toString((int)((Math.random()*9+1)*1000));
            String service_tel="1389999"+Integer.toString((int)((Math.random()*9+1)*1000));
            String longitude="129."+Integer.toString((int)(Math.random()*10000));
            String latitude="42."+Integer.toString((int)(Math.random()*100000));
            String appointment_status="DISABLE";
            String washing_status="DISABLE";

            JSONArray arr = new JSONArray();
            arr.add(info.BrandID);

            //修改前获取门店列表数
            int bef = jc.shopPage(1,1,"").getInteger("total");

            //每次修改固定shop
            jc.editShop(47007L,info.logo,simple_name,name,arr,district_code,address,sale_tel,service_tel,Double.valueOf(longitude),
                    Double.valueOf(latitude),appointment_status,washing_status);
            int after = jc.shopPage(1,1,"").getInteger("total");
            int num = after - bef;


            JSONObject obj = jc.shopPage(1,1,name).getJSONArray("list").getJSONObject(0);
            String simple_name1=obj.getString("simple_name");
            String name1=obj.getString("name");
            String district_code1=obj.getString("district_code");
            String address1=obj.getString("address");
            String sale_tel1=obj.getString("sale_tel");
            String service_tel1=obj.getString("service_tel");
            String longitude1=obj.getString("longitude");
            String latitude1=obj.getString("latitude");



            Preconditions.checkArgument(simple_name.equals(simple_name1),"门店简称，修改为"+simple_name+",列表中="+simple_name1);
            Preconditions.checkArgument(name.equals(name1),"门店全称，修改为"+name+",列表中="+name1);
            Preconditions.checkArgument(district_code.equals(district_code1),"门店所属城市，修改为"+district_code+",列表中="+district_code1);
            Preconditions.checkArgument(address.equals(address1),"门店详细地址，修改为"+address+",列表中="+address1);
            Preconditions.checkArgument(sale_tel.equals(sale_tel1),"门店销售电话，修改为"+sale_tel+",列表中="+sale_tel1);
            Preconditions.checkArgument(service_tel.equals(service_tel1),"门店售后电话，修改为"+service_tel+",列表中="+service_tel1);
            Preconditions.checkArgument(longitude.equals(longitude1),"门店纬度，修改为"+longitude+",列表中="+longitude1);
            Preconditions.checkArgument(latitude.equals(latitude1),"门店经度，修改为"+latitude+",列表中="+latitude1);


            Preconditions.checkArgument(num==0,"修改门店，门店列表增加了"+num);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【门店管理】，修改门店, 列表数量+0，与修改后信息一致");
        }
    }

    @Test
    public void addDelBrandChkNum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //创建前品牌数
            int bef = jc.brandPage(1,10,"","").getInteger("total");
            jc.addBrand(info.stringone,info.logo);
            Long id = jc.brandPage(1,10,"","").getJSONArray("list").getJSONObject(0).getLong("id");
            //创建后品牌数
            int afteradd = jc.brandPage(1,10,"","").getInteger("total");
            jc.editBrand(id,info.stringsix,info.logo);
            //修改后品牌数
            int afteredit = jc.brandPage(1,10,"","").getInteger("total");
            //删除品牌
            jc.delBrand(id);
            //删除后品牌数
            int afterdel = jc.brandPage(1,10,"","").getInteger("total");

            int add = afteradd - bef; //1
            int del = afteradd - afterdel; //1
            Preconditions.checkArgument(add==1,"新建后，列表增加"+add);
            Preconditions.checkArgument(afteradd==afteredit,"编辑后，列表数量改变");
            Preconditions.checkArgument(del==1,"删除后，列表减少"+del);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建品牌，列表+1；删除品牌，列表-1；修改品牌，列表+0");
        }

    }

    @Test
    public void addDelCarStyle() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int bef = jc.carStylePage(1,1,info.BrandID,"").getInteger("total");
            //创建车系
            String manufacturer = "旧生产商";
            String name= "旧车系";
            String online_time= dt.getHistoryDate(0);
            jc.addCarStyle(info.BrandID, manufacturer,  name,  online_time);

            //获取车系id
            Long id = jc.carStylePage(1,1,info.BrandID,name).getJSONArray("list").getJSONObject(0).getLong("id");

            int afteradd = jc.carStylePage(1,1,info.BrandID,"").getInteger("total");

            //修改车系
            String manufacturer1 = "新生产商";
            String name1 = "新车系";
            String online_time1= dt.getHistoryDate(-2);
            jc.editCarStyle(id,info.BrandID,manufacturer1,name1,online_time1);

            int afteredit = jc.carStylePage(1,1,info.BrandID,"").getInteger("total");

            //删除品牌车系
            jc.delCarStyle(id);
            int afterdel = jc.carStylePage(1,1,info.BrandID,"").getInteger("total");

            int add = afteradd - bef; //1
            int del = afteradd - afterdel; //1
            Preconditions.checkArgument(add==1,"新建后，列表增加"+add);
            Preconditions.checkArgument(afteradd==afteredit,"编辑后，列表数量改变");
            Preconditions.checkArgument(del==1,"删除后，列表减少"+del);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，新建车系，列表+1；删除车系，列表-1；编辑车系，列表+0");
        }
    }

    @Test
    public void addDelCarModel() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int bef = jc.carModelPage(1,1,info.BrandID, info.CarStyleID,"","","").getInteger("total");
            //创建车型
            String name1 = "旧车型名称"+System.currentTimeMillis();
            String year1= "2000年";
            String status1 = "ENABLE";
            jc.addCarModel(info.BrandID, info.CarStyleID,  name1,year1,  status1);
            //获取车系id
            int size = jc.carModelPage(1,1,info.BrandID, info.CarStyleID,name1,"","").getInteger("total");
            Long id = jc.carModelPage(1,size,info.BrandID, info.CarStyleID,name1,"","").getJSONArray("list").getJSONObject(size-1).getLong("id");

            int afteradd = jc.carModelPage(1,1,info.BrandID, info.CarStyleID,"","","").getInteger("total");

            //修改车型
            String name2 = "新车型名称"+System.currentTimeMillis();
            String year2= "2020年";
            String status2 = "DISABLE";
            jc.editCarModel(id,info.BrandID, info.CarStyleID,  name2,year2,  status2);
            int afteredit = jc.carModelPage(1,1,info.BrandID, info.CarStyleID,"","","").getInteger("total");

            //删除品牌车系车型
            jc.delCarModel(id);
            int afterdel = jc.carModelPage(1,1,info.BrandID, info.CarStyleID,"","","").getInteger("total");
            int add = afteradd - bef; //1
            int del = afteradd - afterdel; //1
            Preconditions.checkArgument(add==1,"新建后，列表增加"+add);
            Preconditions.checkArgument(afteradd==afteredit,"编辑后，列表数量改变");
            Preconditions.checkArgument(del==1,"删除后，列表减少"+del);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车型，列表数+1；删除车型，列表数-1；编辑车型，列表数+0");
        }
    }

    @Test
    public void addDelBrandChkApplet() {
        logger.logCaseStart(caseResult.getCaseName());
        try {


            jc.appletLoginToken(pp.appletTocken);
            int bef = jc.appletBrandList().getJSONArray("list").size();

            jc.pcLogin("15711300001","000000");
            jc.addBrand(info.stringone,info.logo);
            Long id = jc.brandPage(1,10,"","").getJSONArray("list").getJSONObject(0).getLong("id");

            jc.appletLoginToken(pp.appletTocken);
            int afteradd = jc.appletBrandList().getJSONArray("list").size();
            //删除品牌
            jc.pcLogin("15711300001","000000");
            jc.delBrand(id);

            jc.appletLoginToken(pp.appletTocken);
            int afterdel = jc.appletBrandList().getJSONArray("list").size();


            jc.pcLogin("15711300001","000000");
            int add = afteradd - bef;//1
            int del = afteradd - afterdel;//1
            Preconditions.checkArgument(add==1,"创建后列表增加"+add);
            Preconditions.checkArgument(del==1,"删除后列表减少"+add);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建品牌，小程序列表+1；删除品牌，小程序列表-1");
        }

    }

    @Test
    public void addDelCarStyleChkApplet() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appletLoginToken(pp.appletTocken);
            int bef = jc.appletCarStyleList(info.BrandID).getJSONArray("list").size();

            jc.pcLogin("15711300001","000000");
            //创建车系
            String manufacturer = "旧生产商";
            String name= "旧车系";
            String online_time= dt.getHistoryDate(0);
            jc.addCarStyle(info.BrandID, manufacturer,  name,  online_time);
            //获取车系id
            Long id = jc.carStylePage(1,1,info.BrandID,name).getJSONArray("list").getJSONObject(0).getLong("id");

            jc.appletLoginToken(pp.appletTocken);
            int afteradd = jc.appletCarStyleList(info.BrandID).getJSONArray("list").size();

            jc.pcLogin("15711300001","000000");
            //删除品牌车系
            jc.delCarStyle(id);

            jc.appletLoginToken(pp.appletTocken);
            int afterdel = jc.appletCarStyleList(info.BrandID).getJSONArray("list").size();


            jc.pcLogin("15711300001","000000");
            int add = afteradd - bef; //1
            int del = afteradd - afterdel; //1
            Preconditions.checkArgument(add==1,"新建后，列表增加"+add);

            Preconditions.checkArgument(del==1,"删除后，列表减少"+del);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，新建车系，小程序列表+1；删除车系，小程序列表-1");
        }
    }

    @Test
    public void addDelCarModelChkApplet() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appletLoginToken(pp.appletTocken);
            int bef = jc.appletCarModelList(info.BrandID,info.CarStyleID).getJSONArray("list").size();

            jc.pcLogin("15711300001","000000");
            //创建车型
            String name1 = "旧车型名称"+System.currentTimeMillis();
            String year1= "2000年";
            String status1 = "ENABLE";
            jc.addCarModel(info.BrandID, info.CarStyleID,  name1,year1,  status1);
            //获取车系id
            int size = jc.carModelPage(1,1,info.BrandID, info.CarStyleID,name1,"","").getInteger("total");
            Long id = jc.carModelPage(1,size,info.BrandID, info.CarStyleID,name1,"","").getJSONArray("list").getJSONObject(size-1).getLong("id");

            jc.appletLoginToken(pp.appletTocken);
            int afteradd = jc.appletCarModelList(info.BrandID,info.CarStyleID).getJSONArray("list").size();

            jc.pcLogin("15711300001","000000");
            //删除品牌车系车型
            jc.delCarModel(id);

            jc.appletLoginToken(pp.appletTocken);
            int afterdel = jc.appletCarModelList(info.BrandID,info.CarStyleID).getJSONArray("list").size();

            jc.pcLogin("15711300001","000000");
            int add = afteradd - bef; //1
            int del = afteradd - afterdel; //1
            Preconditions.checkArgument(add==1,"新建后，列表增加"+add);

            Preconditions.checkArgument(del==1,"删除后，列表减少"+del);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车型，小程序列表数+1；删除车型，小程序列表数-1");
        }
    }


    @Test
    public void a(){
        jc.appletLoginToken(pp.appletTocken);
        jc.appletCarModelList(info.BrandID,info.CarStyleID);
    }


}
