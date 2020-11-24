package com.haisheng.framework.testng.bigScreen.jiaochen.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.jiaoChenInfo;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;


/**
 * @author : yu
 * @date :  2020/05/30
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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_JIAOCHEN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "吕雪晴";


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "轿辰 日常 lxq");

        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getProscheShop();
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
    }

    //品牌
    @Test
    public void addBrand_name1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int code = jc.addBrandNotChk(info.stringone,info.logo).getInteger("code");
            Preconditions.checkArgument(code==1000,"状态码期待1000，实际"+code);

            //删除品牌
            Long id = jc.brandPage(1,1,info.stringone,"").getJSONArray("list").getJSONObject(0).getLong("id");
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
            Long id = jc.brandPage(1,1,info.stringone,"").getJSONArray("list").getJSONObject(0).getLong("id");
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
            String name2 = info.stringsix;
            jc.addBrand(info.stringone,info.logo).getInteger("code");
            //获取创建的品牌id
            Long id = jc.brandPage(1,1,name1,"").getJSONArray("list").getJSONObject(0).getLong("id");
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

    //品牌车系

    @Test(dataProvider = "CAR_STYLE")
    public void addCarStyle(String manufacturer, String name, String online_time) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int code = jc.addCarStyle(info.BrandID, manufacturer,  name,  online_time).getInteger("code");
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

    @Test
    public void editCarStyle() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //创建车系
            String manufacturer = "旧生产商";
            String name= "旧车系";
            String online_time= dt.getHistoryDate(0);
            jc.addCarStyle(info.BrandID, manufacturer,  name,  online_time).getInteger("code");
            //获取车系id
            Long id = jc.carStylePage(1,1,info.BrandID,name).getJSONArray("list").getJSONObject(0).getLong("id");
            //修改车系
            String manufacturer1 = "新生产商";
            String name1 = "新车系";
            String online_time1= dt.getHistoryDate(-2);
            jc.editCarStyle(id,info.BrandID,manufacturer1,name1,online_time1);
            //查看修改结果
            String search_manufacturer1 = "";
            String search_name1 = "";
            String search_online_time1= "";
            JSONArray arr = jc.carStylePage(1,30,info.BrandID,name).getJSONArray("list");
            for (int i = 0 ; i < arr.size(); i++){
                JSONObject obj = arr.getJSONObject(i);
                if (obj.getLong("id")==id){
                    search_manufacturer1 = obj.getString("manufacturer");
                    search_name1 = obj.getString("name");
                    search_online_time1= obj.getString("online_time");
                }
            }
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


    @DataProvider(name = "CAR_STYLE")
    public  Object[] carStyle() {
        return new String[][]{
                {info.stringone, info.stringone,dt.getHistoryDate(0)},
                {info.stringfifty, info.stringfifty,dt.getHistoryDate(-1)},
                {info.stringsix, info.stringsix,dt.getHistoryDate(1)},
        };
    }




}
