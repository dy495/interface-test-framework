package com.haisheng.framework.testng.bigScreen.jiaochenonline.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shop.EditScene;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf.PublicParmOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;


/**
 * @author : lxq
 * @date :  2020/11/24
 */

public class DataCaseOnline extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.JC_ONLINE;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.ALL_JC_ONLINE;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_LXQ_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);

    ScenarioUtilOnline jc = ScenarioUtilOnline.getInstance();
    jiaoChenInfoOnline info = new jiaoChenInfoOnline();
    PublicParmOnline pp = new PublicParmOnline();
    CommonConfig commonConfig = new CommonConfig();

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = "吕雪晴";
        commonConfig.product = EnumTestProduce.JC_DAILY.getAbbreviation();
        commonConfig.referer = EnumTestProduce.JC_ONLINE.getReferer();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JC_ONLINE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumTestProduce.JC_ONLINE.getShopId();
        commonConfig.roleId="395";
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
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);

        jc.pcLogin("15711200001", "000000");
    }


    @Test
    public void editshop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String simple_name = "简称" + Integer.toString((int) (Math.random() * 100000));
            String name = "QA测试门店全称";
            String district_code = "222402";
            String address = "地址" + System.currentTimeMillis();
            String sale_tel = "1380000" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000));
            String service_tel = "1389999" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000));
            String longitude = "129." + Integer.toString((int) (Math.random() * 1000));
            String latitude = "42." + Integer.toString((int) (Math.random() * 10000));
            String appointment_status = "DISABLE";
            String washing_status = "DISABLE";

            JSONArray arr = new JSONArray();
            arr.add(info.BrandIDOnline);

            //修改前获取门店列表数
            int bef = jc.shopPage(1, 1, "").getInteger("total");

            //每次修改固定shop
            int code = EditScene.builder().id(20709L).name(name).simpleName(simple_name).districtCode(district_code).address(address).brandList(arr)
                    .saleTel(sale_tel).serviceTel(service_tel).longitude(Double.valueOf(longitude)).latitude(Double.valueOf(latitude)).avatarPath(info.getLogo()).customerServiceTel(sale_tel).rescueTel(sale_tel)
                    .build().invoke(visitor,false).getInteger("code");
            Preconditions.checkArgument(code==1000,"修改门店，状态码"+code);

            int after = jc.shopPage(1, 1, "").getInteger("total");
            int num = after - bef;


            JSONObject obj = jc.shopPage(1, 1, name).getJSONArray("list").getJSONObject(0);
            String simple_name1 = obj.getString("simple_name");
            String name1 = obj.getString("name");
            String district_code1 = obj.getString("district_code");
            String address1 = obj.getString("address");
            String sale_tel1 = obj.getString("sale_tel");
            String service_tel1 = obj.getString("service_tel");
            String longitude1 = obj.getString("longitude");
            String latitude1 = obj.getString("latitude");


            Preconditions.checkArgument(simple_name.equals(simple_name1), "门店简称，修改为" + simple_name + ",列表中=" + simple_name1);
            Preconditions.checkArgument(name.equals(name1), "门店全称，修改为" + name + ",列表中=" + name1);
            Preconditions.checkArgument(district_code.equals(district_code1), "门店所属城市，修改为" + district_code + ",列表中=" + district_code1);
            Preconditions.checkArgument(address.equals(address1), "门店详细地址，修改为" + address + ",列表中=" + address1);
            Preconditions.checkArgument(sale_tel.equals(sale_tel1), "门店销售电话，修改为" + sale_tel + ",列表中=" + sale_tel1);
            Preconditions.checkArgument(service_tel.equals(service_tel1), "门店售后电话，修改为" + service_tel + ",列表中=" + service_tel1);
            Preconditions.checkArgument(longitude.equals(longitude1), "门店纬度，修改为" + longitude + ",列表中=" + longitude1);
            Preconditions.checkArgument(latitude.equals(latitude1), "门店经度，修改为" + latitude + ",列表中=" + latitude1);


            Preconditions.checkArgument(num == 0, "修改门店，门店列表增加了" + num);

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
            int bef = jc.brandPage(1, 10, "", "").getInteger("total");
            jc.addBrand(info.stringone, info.getLogo());
            Long id = jc.brandPage(1, 10, "", "").getJSONArray("list").getJSONObject(0).getLong("id");
            //创建后品牌数
            int afteradd = jc.brandPage(1, 10, "", "").getInteger("total");
            jc.editBrand(id, info.stringsix, info.getLogo());
            //修改后品牌数
            int afteredit = jc.brandPage(1, 10, "", "").getInteger("total");
            //删除品牌
            jc.delBrand(id);
            //删除后品牌数
            int afterdel = jc.brandPage(1, 10, "", "").getInteger("total");

            int add = afteradd - bef; //1
            int del = afteradd - afterdel; //1
            Preconditions.checkArgument(add == 1, "新建后，列表增加" + add);
            Preconditions.checkArgument(afteradd == afteredit, "编辑后，列表数量改变");
            Preconditions.checkArgument(del == 1, "删除后，列表减少" + del);
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

            int bef = jc.carStylePage(1, 1, info.BrandIDOnline, "").getInteger("total");
            //创建车系
            String manufacturer = "旧生产商";
            String name = "旧车系";
            String online_time = dt.getHistoryDate(0);
            jc.addCarStyle(info.BrandIDOnline, manufacturer, name, online_time);

            //获取车系id
            Long id = jc.carStylePage(1, 1, info.BrandIDOnline, name).getJSONArray("list").getJSONObject(0).getLong("id");

            int afteradd = jc.carStylePage(1, 1, info.BrandIDOnline, "").getInteger("total");

            //修改车系
            String manufacturer1 = "新生产商";
            String name1 = "新车系";
            String online_time1 = dt.getHistoryDate(-2);
            jc.editCarStyle(id, info.BrandIDOnline, manufacturer1, name1, online_time1);

            int afteredit = jc.carStylePage(1, 1, info.BrandIDOnline, "").getInteger("total");

            //删除品牌车系
            jc.delCarStyle(id);
            int afterdel = jc.carStylePage(1, 1, info.BrandIDOnline, "").getInteger("total");

            int add = afteradd - bef; //1
            int del = afteradd - afterdel; //1
            Preconditions.checkArgument(add == 1, "新建后，列表增加" + add);
            Preconditions.checkArgument(afteradd == afteredit, "编辑后，列表数量改变");
            Preconditions.checkArgument(del == 1, "删除后，列表减少" + del);

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

            int bef = jc.carModelPage(1, 1, info.BrandIDOnline, info.CarStyleIDOnline, "", "", "").getInteger("total");
            //创建车型
            String name1 = "旧车型名称" + System.currentTimeMillis();
            String year1 = "2000年";
            String status1 = "ENABLE";
            jc.addCarModel(info.BrandIDOnline, info.CarStyleIDOnline, name1, year1, status1);
            //获取车系id
            int size = jc.carModelPage(1, 1, info.BrandIDOnline, info.CarStyleIDOnline, name1, "", "").getInteger("total");
            Long id = jc.carModelPage(1, size, info.BrandIDOnline, info.CarStyleIDOnline, name1, "", "").getJSONArray("list").getJSONObject(size - 1).getLong("id");

            int afteradd = jc.carModelPage(1, 1, info.BrandIDOnline, info.CarStyleIDOnline, "", "", "").getInteger("total");

            //修改车型
            String name2 = "新车型名称" + System.currentTimeMillis();
            String year2 = "2020年";
            String status2 = "DISABLE";
            jc.editCarModel(id, info.BrandIDOnline, info.CarStyleIDOnline, name2, year2, status2);
            int afteredit = jc.carModelPage(1, 1, info.BrandIDOnline, info.CarStyleIDOnline, "", "", "").getInteger("total");

            //删除品牌车系车型
            jc.delCarModel(id);
            int afterdel = jc.carModelPage(1, 1, info.BrandIDOnline, info.CarStyleIDOnline, "", "", "").getInteger("total");
            int add = afteradd - bef; //1
            int del = afteradd - afterdel; //1
            Preconditions.checkArgument(add == 1, "新建后，列表增加" + add);
            Preconditions.checkArgument(afteradd == afteredit, "编辑后，列表数量改变");
            Preconditions.checkArgument(del == 1, "删除后，列表减少" + del);

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

            jc.pcLogin("15711200001", "000000");
            jc.addBrand(info.stringone, info.getLogo());
            Long id = jc.brandPage(1, 10, "", "").getJSONArray("list").getJSONObject(0).getLong("id");

            jc.appletLoginToken(pp.appletTocken);
            int afteradd = jc.appletBrandList().getJSONArray("list").size();
            //删除品牌
            jc.pcLogin("15711200001", "000000");
            jc.delBrand(id);

            jc.appletLoginToken(pp.appletTocken);
            int afterdel = jc.appletBrandList().getJSONArray("list").size();


            jc.pcLogin("15711200001", "000000");
            int add = afteradd - bef;//1
            int del = afteradd - afterdel;//1
            Preconditions.checkArgument(add == 1, "创建后列表增加" + add);
            Preconditions.checkArgument(del == 1, "删除后列表减少" + add);

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
            int bef = jc.appletCarStyleList(info.BrandIDOnline).getJSONArray("list").size();

            jc.pcLogin("15711200001", "000000");
            //创建车系
            String manufacturer = "旧生产商";
            String name = "旧车系";
            String online_time = dt.getHistoryDate(0);
            jc.addCarStyle(info.BrandIDOnline, manufacturer, name, online_time);
            //获取车系id
            Long id = jc.carStylePage(1, 1, info.BrandIDOnline, name).getJSONArray("list").getJSONObject(0).getLong("id");

            jc.appletLoginToken(pp.appletTocken);
            int afteradd = jc.appletCarStyleList(info.BrandIDOnline).getJSONArray("list").size();

            jc.pcLogin("15711200001", "000000");
            //删除品牌车系
            jc.delCarStyle(id);

            jc.appletLoginToken(pp.appletTocken);
            int afterdel = jc.appletCarStyleList(info.BrandIDOnline).getJSONArray("list").size();


            jc.pcLogin("15711200001", "000000");
            int add = afteradd - bef; //1
            int del = afteradd - afterdel; //1
            Preconditions.checkArgument(add == 1, "新建后，列表增加" + add);

            Preconditions.checkArgument(del == 1, "删除后，列表减少" + del);

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
            int bef = jc.appletCarModelList(Long.toString(info.BrandIDOnline), Long.toString(info.CarStyleIDOnline)).getJSONArray("list").size();

            jc.pcLogin("15711200001", "000000");
            //创建车型
            String name1 = "旧车型名称" + System.currentTimeMillis();
            String year1 = "2000年";
            String status1 = "ENABLE";
            jc.addCarModel(info.BrandIDOnline, info.CarStyleIDOnline, name1, year1, status1);
            //获取车系id
            int size = jc.carModelPage(1, 1, info.BrandIDOnline, info.CarStyleIDOnline, name1, "", "").getInteger("total");
            Long id = jc.carModelPage(1, size, info.BrandIDOnline, info.CarStyleIDOnline, name1, "", "").getJSONArray("list").getJSONObject(size - 1).getLong("id");

            jc.appletLoginToken(pp.appletTocken);
            int afteradd = jc.appletCarModelList(Long.toString(info.BrandIDOnline), Long.toString(info.CarStyleIDOnline)).getJSONArray("list").size();

            jc.pcLogin("15711200001", "000000");
            //删除品牌车系车型
            jc.delCarModel(id);

            jc.appletLoginToken(pp.appletTocken);
            int afterdel = jc.appletCarModelList(Long.toString(info.BrandIDOnline), Long.toString(info.CarStyleIDOnline)).getJSONArray("list").size();

            jc.pcLogin("15711200001", "000000");
            int add = afteradd - bef; //1
            int del = afteradd - afterdel; //1
            Preconditions.checkArgument(add == 1, "新建后，列表增加" + add);

            Preconditions.checkArgument(del == 1, "删除后，列表减少" + del);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车型，小程序列表数+1；删除车型，小程序列表数-1");
        }
    }

}
