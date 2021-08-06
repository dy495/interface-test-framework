package com.haisheng.framework.testng.bigScreen.jiaochen.mc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.reid.AppReidReidListScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.reid.AppReidReidMarkScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.riskcontrol.NonCustomerSignScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.apache.commons.collections.ArrayStack;
import org.jooq.False;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SystemCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCE = EnumTestProduct.JC_DAILY_JD;
    private static final EnumAccount ACCOUNT = EnumAccount.JC_ALL_AUTHORITY_DAILY_NEW;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public SceneUtil util = new SceneUtil(visitor);

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "孟辰";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //放入shopId
        commonConfig.product = PRODUCE.getAbbreviation();
        commonConfig.referer = PRODUCE.getReferer();
        commonConfig.shopId = "46522";
        commonConfig.roleId = ACCOUNT.getRoleId();
        beforeClassInit(commonConfig);
        util.loginPc(ACCOUNT);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        util.loginPc(ACCOUNT);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        logger.logCaseStart(caseResult.getCaseName());
    }

    /**
     * @description :
     * prams:     customerType:"新客"
     *
     *
     **/


    public List<JSONObject> getFaceIdList(String customerType, boolean isApprove){
        Stream<JSONObject> jsonObjectStream = AppReidReidListScene.builder().isFaceOpen(true).enterType("PRE_SALE").size(100).build().invoke(visitor, true)
                .getJSONArray("list").stream().map(ele -> (JSONObject) ele)
                .filter(e -> Objects.equals(customerType, e.getString("reid_type_name")));
        if(isApprove){
            return jsonObjectStream.filter(e -> e.getBoolean("is_agreement")).collect(Collectors.toList());
        } else if(!isApprove){
            return jsonObjectStream.filter(e -> !e.getBoolean("is_agreement")).collect(Collectors.toList());
        }
        return null;
    }
    public List<JSONObject> getFaceIdList(String customerType){
        return AppReidReidListScene.builder().isFaceOpen(true).enterType("PRE_SALE").size(100).build().invoke(visitor, true)
                .getJSONArray("list").stream().map(ele -> (JSONObject) ele)
                .filter(e -> Objects.equals(customerType, e.getString("reid_type_name"))).collect(Collectors.toList());
    }
    public List<JSONObject> getFaceIdList(boolean isApprove){
        if(isApprove){
            return AppReidReidListScene.builder().isFaceOpen(true).enterType("PRE_SALE").size(100).build().invoke(visitor, true)
                    .getJSONArray("list").stream().map(ele -> (JSONObject) ele).filter(e -> e.getBoolean("is_agreement")).collect(Collectors.toList());
        } else if(!isApprove){
            return AppReidReidListScene.builder().isFaceOpen(true).enterType("PRE_SALE").size(100).build().invoke(visitor, true)
                    .getJSONArray("list").stream().map(ele -> (JSONObject) ele).filter(e -> !e.getBoolean("is_agreement")).collect(Collectors.toList());
        }
        return null;
    }





    @Test(dataProvider = "ReidType")
    public void test01Mark(String typeName,String reidType){
        try {
//            List<JSONObject> customerList = getFaceIdList(type, isApprove);
//            if(customerList != null && customerList.size()>0){
//                JSONObject customer = customerList.get(0);
//                Integer reid = customer.getInteger("reid");
//                new JSONArray(reid);
//            }
            JSONObject res1 = AppReidReidListScene.builder().isFaceOpen(true).enterType("PRE_SALE").size(100).build().invoke(visitor, true);
            Integer total1 = res1.getInteger("total");
            String reid = res1.getJSONArray("list").getJSONObject(0).getString("reid");
            JSONArray reidList = new JSONArray();
            reidList.add(reid);
            AppReidReidMarkScene.builder().enterType("PRE_SALE").reidType(reidType).reidList(reidList).build().invoke(visitor);
            JSONObject res2 = AppReidReidListScene.builder().isFaceOpen(true).enterType("PRE_SALE").size(100).build().invoke(visitor, true);
            Integer total2 = res2.getInteger("total");
            Preconditions.checkArgument(total1==total2+1,"前台标记为"+typeName+"客流列表之前:"+total1+",之后:"+total2);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("前台标记客流列表-1");
        }
    }
    @DataProvider(name = "ReidType")
    public Object[] reidType(){
        return new Object[][]{
                {},
        };
    }


}
